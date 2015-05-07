/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.commons.network.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gregmarut.commons.network.InvalidStateException;
import com.gregmarut.commons.network.ServerState;
import com.gregmarut.commons.network.frame.FramedStreamReader;
import com.gregmarut.commons.network.frame.FramedStreamWriter;

/**
 * A class used to represent a server that contains an active connection. This type
 * of server is used once a connection has already been established and is able to
 * send and receive data.
 * 
 * @author Greg
 */
public class ConnectedServer<S> extends Server
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// holds the socket used for communicating
	protected final GenericSocket<S> socket;
	
	// holds the streams used for communication
	protected FramedStreamWriter out;
	protected FramedStreamReader in;
	
	/**
	 * The constructor for Server
	 * 
	 * @param socket
	 */
	public ConnectedServer(final GenericSocket<S> socket, final List<ServerListener> serverListeners)
		throws InvalidStateException
	{
		// call the server's constructor
		super(socket, serverListeners);
		
		// save the server socket
		this.socket = socket;
		
		try
		{
			// set up a new output stream object
			out = new FramedStreamWriter(new BufferedOutputStream(socket.getOutputStream()));
			
			// setup the input stream object
			in = new FramedStreamReader(new BufferedInputStream(socket.getInputStream()));
		}
		catch (IOException IOE)
		{
			throw new InvalidStateException(IOE);
		}
	}
	
	/**
	 * Tells the server to start listening for communication
	 * 
	 * @return
	 */
	synchronized void listen()
	{
		// make sure the state is in requested
		if (ServerState.REQUESTED == state)
		{
			// change the state to connected
			state = ServerState.CONNECTED;
			
			// start the server's activity
			Thread thread = new Thread(getServerActivity(), SERVER_ACTIVITY_THREAD_NAME);
			thread.start();
		}
	}
	
	/**
	 * Closes the connection
	 */
	@Override
	public synchronized void disconnect()
	{
		// check to see if a connection exists
		if (ServerState.CLOSED != state)
		{
			try
			{
				// attempt to close the server
				if (null != socket)
				{
					socket.close();
				}
			}
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
			}
			finally
			{
				// set the state to closed
				state = ServerState.CLOSED;
			}
		}
	}
	
	/**
	 * Send data across the socket
	 * 
	 * @param data
	 * @throws InvalidStateException
	 */
	public void sendData(final byte[] data) throws InvalidStateException
	{
		// make sure the client is connected
		if (ServerState.CONNECTED == state)
		{
			// try to send the object
			try
			{
				// write the data to the output stream
				writeData(data);
			}
			catch (IOException IOE)
			{
				throw new InvalidStateException(IOE);
			}
		}
		else
		{
			throw new InvalidStateException();
		}
	}
	
	/**
	 * Writes the data to the output stream
	 * 
	 * @param data
	 * @throws IOException
	 */
	protected void writeData(final byte[] data) throws IOException
	{
		// write the data to the output stream
		out.writeFrame(data);
	}
	
	/**
	 * Returns the server activity to use for this server
	 * 
	 * @return
	 */
	protected ServerActivity getServerActivity()
	{
		return new ServerActivity();
	}
	
	/**
	 * A class used to handle the listening process of the server
	 * 
	 * @author Greg Marut
	 */
	protected class ServerActivity implements Runnable
	{
		/**
		 * Called when a new thread is executed
		 */
		@Override
		public final void run()
		{
			// for each listener
			for (ServerListener listener : serverListeners)
			{
				// tell the listener that a connection has been established
				listener.connectionEstablished(new ServerEvent(ConnectedServer.this));
			}
			
			// while the server is connected
			while (ServerState.CONNECTED == state)
			{
				// try to get the data
				try
				{
					// get the data from the socket buffer
					final byte[] data = readData();
					
					// make sure the data is not null
					if (null == data)
					{
						throw new IOException();
					}
					else
					{
						// for each listener
						for (final ServerListener listener : serverListeners)
						{
							// create a new thread to dispatch the event
							Thread dispatchThread = new Thread(
								new Runnable()
								{
									@Override
									public void run()
									{
										// tell the listener that data has been received
										listener.dataReceived(new ServerEvent(ConnectedServer.this, data));
									}
								}, EVENT_DISPATCH_THREAD_NAME
								);
							
							// execute the thread
							dispatchThread.start();
						}
					}
				}
				catch (IOException e)
				{
					logger.error(e.getMessage(), e);
					
					// disconnect this server
					disconnect();
					
					// for each listener
					for (final ServerListener listener : serverListeners)
					{
						// create a new thread to dispatch the event
						Thread dispatchThread = new Thread(
							new Runnable()
							{
								@Override
								public void run()
								{
									// tell the listener that a connection has been established
									listener.connectionLost(new ServerEvent(ConnectedServer.this));
								}
							}, EVENT_DISPATCH_THREAD_NAME
							);
						
						dispatchThread.start();
					}
				}
			}
		}
		
		/**
		 * Waits for data to be read from the reader
		 * 
		 * @return
		 * @throws IOException
		 */
		protected byte[] readData() throws IOException
		{
			return in.readFrame();
		}
	}
}
