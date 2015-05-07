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
package com.gregmarut.commons.network.frame;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class FramedStreamWriter extends FramedStream
{
	//** Objects **//
	private final OutputStream os;
	
	public FramedStreamWriter(final OutputStream os) throws IOException
	{
		this.os = os;
		os.flush();
	}
	
	public void writeFrame(final byte[] data) throws IOException
	{
		//determine the length of the data frame
		byte[] prefix = ByteBuffer.allocate(INTEGER_SIZE).putInt(data.length).array();
		
		// write the prefix for the data frame
		os.write(prefix);
		
		// write the data to the output stream
		os.write(data);
		os.flush();
	}
}
