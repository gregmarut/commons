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
import java.io.InputStream;
import java.io.OutputStream;

public abstract class GenericSocket<S>
{
	protected final S socket;
	
	public GenericSocket(final S socket)
	{
		this.socket = socket;
	}
	
	public S getSocket()
	{
		return socket;
	}
	
	public abstract boolean isConnected();
	
	public abstract void connect() throws IOException;
	
	public abstract void close() throws IOException;
	
	public abstract InputStream getInputStream() throws IOException;
	
	public abstract OutputStream getOutputStream() throws IOException;
}
