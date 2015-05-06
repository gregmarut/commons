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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.gregmarut.commons.network.tcp.GenericSocket;

public class IPSocket extends GenericSocket<Socket>
{
	//** Objects **//
	//holds the host to connect to
	private final String host;
	
	//holds the port to use
	private final int port;
	
	//holds the timeout
	private final int timeout;
	
	public IPSocket(final Socket socket)
	{
		this(socket, null, 0, 0);
	}
	
	public IPSocket(final Socket socket, final String host, final int port, final int timeout)
	{
		super(socket);
		
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}

	@Override
	public void connect() throws IOException
	{
		//set the timeout on the socket
		socket.setSoTimeout(timeout);
		
		//connect the socket
		socket.connect(new InetSocketAddress(host, port));
	}

	@Override
	public void close() throws IOException
	{
		socket.close();
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return socket.getOutputStream();
	}

	@Override
	public boolean isConnected()
	{
		return socket.isConnected();
	}
	
}
