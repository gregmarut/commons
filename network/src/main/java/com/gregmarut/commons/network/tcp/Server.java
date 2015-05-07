/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *    Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.commons.network.tcp;

import java.util.ArrayList;
import java.util.List;

import com.gregmarut.commons.network.InvalidStateException;
import com.gregmarut.commons.network.ServerState;

/**
 * Acts as a base class where both of the types of servers share their common functionality
 * 
 * @author Greg Marut
 */
public abstract class Server
{
	protected static final String SERVER_ACTIVITY_THREAD_NAME = "TCP Server Activity Thread";
	protected static final String EVENT_DISPATCH_THREAD_NAME = "TCP Event Dispatch Thread";
	
	// ** Enumerations **//
	// holds the state of the server
	protected volatile ServerState state;
	
	// ** Objects **//
	// holds the list of server listeners
	protected final List<ServerListener> serverListeners;
	
	/**
	 * The constructor for Server
	 * 
	 * @param port
	 */
	protected Server()
	{
		// set the list of server listeners
		serverListeners = new ArrayList<ServerListener>();
		
		// set the state to closed
		state = ServerState.CLOSED;
	}
	
	/**
	 * The constructor for Server
	 * 
	 * @param socket
	 */
	protected Server(final GenericSocket<?> socket, final List<ServerListener> serverListeners)
		throws InvalidStateException
	{
		// save the list of server listeners
		this.serverListeners = serverListeners;
		
		// check to see if the socket passed is valid
		if (null != socket)
		{
			// set the state to requested
			state = ServerState.REQUESTED;
		}
		else
		{
			throw new InvalidStateException();
		}
	}
	
	/**
	 * Registers a listener for this Server
	 * 
	 * @param listener
	 */
	public synchronized void addServerListener(final ServerListener listener)
	{
		// make sure the list does not already contain the listener
		if (!serverListeners.contains(listener))
		{
			// add the listener to the list
			serverListeners.add(listener);
		}
	}
	
	/**
	 * Removes a listener from the list and returns whether or not it was successful
	 * 
	 * @param listener
	 * @return boolean
	 */
	public synchronized boolean removeServerListener(final ServerListener listener)
	{
		return serverListeners.remove(listener);
	}
	
	/**
	 * Called right before this object is collected by garbage collection
	 * @throws Throwable 
	 */
	@Override
	public final void finalize() throws Throwable
	{
		// make sure the server disconnects before it is garbage collected
		if (ServerState.CLOSED != state)
		{
			disconnect();
		}
		
		super.finalize();
	}
	
	//disconnects the socket
	public abstract void disconnect();
}
