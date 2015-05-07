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
package com.gregmarut.commons.network.tcp.ip;

import java.net.Socket;

import com.gregmarut.commons.network.tcp.Client;
import com.gregmarut.commons.network.tcp.GenericSocket;

/**
 * A class used for communicating with a server over a network via TCP
 * 
 * @author Greg Marut
 */
public class IPClient extends Client<Socket>
{
	// ** Objects **//
	private final String host;
	
	// ** Primitives **//
	// holds the network port to communicate on
	private final int port;
	
	// holds the inactivity timeout
	private int inactivityTimeout;
	
	/**
	 * The constructor for TCPClient
	 * 
	 * @param host
	 * @param port
	 */
	public IPClient(final String host, final int port)
	{
		this(host, port, 0);
	}
	
	/**
	 * The constructor for TCPClient
	 * 
	 * @param host
	 * @param port
	 */
	public IPClient(final String host, final int port, final int inactivityTimeout)
	{
		// save the parameters
		this.host = host;
		this.port = port;
		
		// set the timeout
		setInactivityTimeout(inactivityTimeout);
	}
	
	/**
	 * Sets the timeout in milliseconds. If the connection is open and the specified time has
	 * elapsed with no communication, the connection will automatically close.
	 * Set to 0 to disable a timeout (Default)
	 * 
	 * @param inactivityTimeout
	 */
	public void setInactivityTimeout(final int inactivityTimeout)
	{
		this.inactivityTimeout = inactivityTimeout;
	}

	@Override
	protected GenericSocket<Socket> createNewSocket()
	{
		return new IPSocket(new Socket(), host, port, inactivityTimeout);
	}
}
