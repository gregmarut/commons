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

public class IPServerSocket extends GenericServerSocket<ServerSocket, Socket>
{
	public IPServerSocket(final ServerSocket serverSocket)
	{
		super(serverSocket);
	}

	@Override
	public Socket accept() throws IOException
	{
		return serverSocket.accept();
	}

	@Override
	public void close() throws IOException
	{
		serverSocket.close();
	}
}
