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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gregmarut.commons.network.ClientState;
import com.gregmarut.commons.network.InvalidStateException;
import com.gregmarut.commons.network.frame.FramedStreamReader;
import com.gregmarut.commons.network.frame.FramedStreamWriter;

/**
 * A class used for communicating with a server over a network via TCP
 * 
 * @author Greg Marut
 */
public abstract class Client<S>
{
	protected static final String CLIENT_ACTIVITY_THREAD_NAME = "TCP Client Activity Thread";
	protected static final String EVENT_DISPATCH_THREAD_NAME = "TCP Event Dispatch Thread";
	
	// ** Enumerations **//
	// holds the state of this client
	private volatile ClientState state;
	
	// ** Objects **//
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected GenericSocket<S> socket;
	protected FramedStreamWriter out;
	protected FramedStreamReader in;
	
	// holds the list of all client listeners
	private final List<ClientListener> clientListeners;
	
	/**
	 * The constructor for TCPClient
	 * 
	 * @param host
	 * @param port
	 */
	public Client()
	{
		// assign the arraylist of client listeners
		clientListeners = new ArrayList<ClientListener>();
		
		// set the default state for this client
		state = ClientState.CLOSED;
	}
	
	/**
	 * Registers a listener for this Client
	 * 
	 * @param listener
	 */
	public synchronized void addClientListener(final ClientListener listener)
	{
		// make sure the list does not already contain the listener
		if (!clientListeners.contains(listener))
		{
			// add the listener to the list
			clientListeners.add(listener);
		}
	}
	
	/**
	 * Removes a listener from the list and returns whether or not it was successful
	 * 
	 * @param listener
	 * @return boolean
	 */
	public synchronized boolean removeClientListener(final ClientListener listener)
	{
		return clientListeners.remove(listener);
	}
	
	/**
	 * Attempts a connection to the server
	 * 
	 * @return Whether or not the connection is being attempted
	 */
	public synchronized boolean connect()
	{
		// holds the result to return
		boolean result;
		
		// make sure the socket is closed
		if (ClientState.CLOSED == state)
		{
			result = true;
			
			// create a new client activity thread
			Thread thread = new Thread(getClientActivity(), CLIENT_ACTIVITY_THREAD_NAME);
			thread.start();
		}
		else
		{
			result = false;
		}
		
		// return the result
		return result;
	}
	
	/**
	 * Determines if this client is currently connected
	 * 
	 * @return
	 */
	public boolean isConnected()
	{
		return (state == ClientState.CONNECTED);
	}
	
	/**
	 * Closes the connection
	 */
	public synchronized void disconnect()
	{
		// make sure there is a socket
		if (null != socket)
		{
			try
			{
				// make sure the socket is not null
				if (null != socket)
				{
					// attempt to close the socket
					socket.close();
				}
			}
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
			}
			finally
			{
				// remove these references
				out = null;
				in = null;
				socket = null;
				
				// set the state to closed
				state = ClientState.CLOSED;
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
		if (ClientState.CONNECTED == state)
		{
			// try to send the object
			try
			{
				// write the data to the output stream
				writeData(data);
			}
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
				
				throw new InvalidStateException(e);
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
	 * Returns the client activity to use for this client
	 * 
	 * @return
	 */
	protected ClientActivity getClientActivity()
	{
		return new ClientActivity();
	}
	
	/**
	 * A class used to handle the connection process to the server
	 * 
	 * @author Greg
	 */
	protected class ClientActivity implements Runnable
	{
		/**
		 * Called when a new thread is executed
		 */
		@Override
		public final void run()
		{
			try
			{
				// try to make a connection
				attemptConnection();
				
				// get the input and output streams
				getStreams();
				
				// process the connection
				processConnection();
			}
			// catch any io exception that gets thrown
			catch (IOException e)
			{
				logger.error(e.getMessage(), e);
			}
			finally
			{
				// switch based off of the current state
				switch (state)
				{
					case CONNECTED:
						// for each of the listeners
						for (final ClientListener listener : clientListeners)
						{
							// create a new thread to dispatch the event
							Thread dispatchThread = new Thread(
								new Runnable()
								{
									@Override
									public void run()
									{
										// tell the listener that the connection was lost
										listener.connectionLost(new ClientEvent(Client.this));
									}
								}, EVENT_DISPATCH_THREAD_NAME
								);
							
							// execute the thread
							dispatchThread.start();
						}
						break;
					
					case CLOSED:
					case CONNECTING:
					default:
						// for each of the listeners
						for (final ClientListener objListener : clientListeners)
						{
							// create a new thread to dispatch the event
							Thread dispatchThread = new Thread(
								new Runnable()
								{
									@Override
									public void run()
									{
										// tell the listener that the connection was lost
										objListener.connectionError(new ClientEvent(Client.this));
									}
								}, EVENT_DISPATCH_THREAD_NAME
								);
							
							// execute the thread
							dispatchThread.start();
						}
						break;
				}
				
				// close the connection
				disconnect();
			}
		}
		
		/**
		 * Attempt to connect to the server
		 */
		private void attemptConnection() throws IOException
		{
			// set the state to connecting
			state = ClientState.CONNECTING;
			
			// create the new socket
			socket = createNewSocket();
			
			// connect the socket
			socket.connect();
		}
		
		/**
		 * get the input and output streams has the possibility to throw an IO
		 * exception
		 */
		private void getStreams() throws IOException
		{
			// set up a new output stream object
			out = new FramedStreamWriter(new BufferedOutputStream(socket.getOutputStream()));
			
			// setup the input stream object
			in = new FramedStreamReader(new BufferedInputStream(socket.getInputStream()));
		}
		
		/**
		 * processes the connection has the possibility to throw an IO exception
		 */
		private void processConnection() throws IOException, SocketTimeoutException
		{
			// set the state to connected
			state = ClientState.CONNECTED;
			
			// for each of the listeners
			for (final ClientListener listener : clientListeners)
			{
				// create a new thread to dispatch the event
				Thread dispatchThread = new Thread(
					new Runnable()
					{
						@Override
						public void run()
						{
							// tell the listener that a connection has been established
							listener.connectionEstablished(new ClientEvent(Client.this));
						}
					}, EVENT_DISPATCH_THREAD_NAME
					);
				
				// execute the thread
				dispatchThread.start();
			}
			
			// loop while there is a connection
			while (ClientState.CONNECTED == state)
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
					for (final ClientListener listener : clientListeners)
					{
						// create a new thread to dispatch the event
						Thread dispatchThread = new Thread(
							new Runnable()
							{
								@Override
								public void run()
								{
									// tell the listener that data has been received
									listener.dataReceived(new ClientEvent(Client.this, data));
								}
							}, EVENT_DISPATCH_THREAD_NAME
							);
						
						// execute the thread
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
	
	protected abstract GenericSocket<S> createNewSocket() throws IOException;
}
