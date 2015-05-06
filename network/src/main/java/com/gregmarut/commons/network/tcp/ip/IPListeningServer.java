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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.gregmarut.commons.network.tcp.GenericServerSocket;
import com.gregmarut.commons.network.tcp.GenericSocket;
import com.gregmarut.commons.network.tcp.ListeningServer;

/**
 * A class used to represent a server whose job is to listen for and accept incoming connections. 
 * This type of server does not send or receive data. Once a new connection has been accepted, a
 * new ConnectedServer object is created.
 * 
 * @author Greg
 */
public class IPListeningServer extends ListeningServer<ServerSocket, Socket>
{
	// ** Objects **//
	// holds the server socket to listen for incomming connections
	protected ServerSocket serverSocket;
	
	// ** Primitives **//
	// holds the port to use for network communication
	protected final int port;
	
	/**
	 * The constructor for Server
	 * 
	 * @param port
	 */
	public IPListeningServer(final int port)
	{
		//save the port
		this.port = port;
	}

	@Override
	public GenericServerSocket<ServerSocket, Socket> createNewServerSocket() throws IOException
	{
		return new IPServerSocket(new ServerSocket(port));
	}

	@Override
	protected GenericSocket<Socket> createNewSocket(final Socket socket)
	{
		return new IPSocket(socket);
	}
}
