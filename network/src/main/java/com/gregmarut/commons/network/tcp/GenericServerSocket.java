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

import java.io.IOException;

public abstract class GenericServerSocket<L, S>
{
	protected final L serverSocket;
	
	public GenericServerSocket(final L serverSocket)
	{
		this.serverSocket = serverSocket;
	}
	
	public L getServerSocket()
	{
		return serverSocket;
	}
	
	public abstract S accept() throws IOException;
	
	public abstract void close() throws IOException;
}
