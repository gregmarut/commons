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

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gregmarut.commons.network.ServerState;

/**
 * A class used to represent a server whose job is to listen for and accept incoming connections.
 * This type of server does not send or receive data. Once a new connection has been accepted, a
 * new ConnectedServer object is created.
 * 
 * @author Greg
 */
public abstract class ListeningServer<L, S> extends Server
{
	// ** Objects **//
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// holds the server socket to listen for incomming connections
	protected GenericServerSocket<L, S> serverSocket;
	
	/**
	 * The constructor for Server
	 * 
	 * @param port
	 */
	public ListeningServer()
	{
		// set the state to closed
		state = ServerState.CLOSED;
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
				if (null != serverSocket)
				{
					serverSocket.close();
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
	 * Starts the listening server
	 * 
	 * @return
	 * @throws IOException
	 */
	public synchronized boolean listen() throws IOException
	{
		// holds the result to return
		boolean result;
		
		// make sure the socket is closed
		if (ServerState.CLOSED == state)
		{
			result = true;
			
			// set the server state to LISTENING
			state = ServerState.LISTENING;
			
			// assign the server listener
			serverSocket = createNewServerSocket();
			
			// create a new server activity thread
			Thread thread = new Thread(getServerActivity(), SERVER_ACTIVITY_THREAD_NAME);
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
			// while the server is listening
			while (ServerState.LISTENING == state)
			{
				try
				{
					// wait for a new connection
					S socket = serverSocket.accept();
					
					// create a new generic socket
					GenericSocket<S> genericSocket = createNewSocket(socket);
					
					// create the new connected server object
					ConnectedServer<S> connectedServer = createNewConnectedServer(genericSocket);
					
					// tell the listeners that a connection has been requested
					notifyConnectionRequest(connectedServer);
					
					// begin listening on the connected server
					connectedServer.listen();
				}
				catch (IOException e)
				{
					logger.error(e.getMessage(), e);
				}
			}
		}
		
		/**
		 * Creates a new connected server
		 * 
		 * @param socket
		 */
		protected ConnectedServer<S> createNewConnectedServer(final GenericSocket<S> socket)
		{
			// return a new instance of a connected server
			return new ConnectedServer<S>(socket,
				new ArrayList<ServerListener>(serverListeners));
		}
		
		/**
		 * Tell the listeners that a connection has been requested
		 * 
		 * @param sckNew
		 */
		protected void notifyConnectionRequest(final ConnectedServer<S> connectedServer)
		{
			// for each of the listeners
			for (final ServerListener listener : serverListeners)
			{
				// create a new thread to dispatch the event
				Thread dispatchThread = new Thread(
					new Runnable()
					{
						@Override
						public void run()
						{
							// report that a new connection has been accepted
							listener.connectionRequest(new ServerEvent(ListeningServer.this, connectedServer));
						}
					}, EVENT_DISPATCH_THREAD_NAME
					);
				
				// execute the thread
				dispatchThread.start();
			}
		}
	}
	
	public abstract GenericServerSocket<L, S> createNewServerSocket() throws IOException;
	
	protected abstract GenericSocket<S> createNewSocket(S socket) throws IOException;
}
