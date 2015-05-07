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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class FramedStreamReader extends FramedStream
{
	//** Objects **//
	private final InputStream is;
	
	public FramedStreamReader(final InputStream is)
	{
		this.is = is;
	}
	
	public byte[] readFrame() throws IOException
	{
		// holds the incoming data
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		// create a new buffer
		byte[] buffer = new byte[BUFFER_SIZE];
		
		// holds the length of the data
		int dataLength = 0;
		
		// holds the total number of bytes read
		int totalRead = 0;
		
		// holds the number of bytes that have been read
		int n = 0;
		
		// read the prefix
		while ((n = is.read(buffer, 0, INTEGER_SIZE - totalRead)) != END_OF_STREAM)
		{
			//increment the total bytes read
			totalRead += n;
			
			//check to see if the total read is equal to the integer size
			if(INTEGER_SIZE == totalRead)
			{
				ByteBuffer bb = ByteBuffer.allocate(INTEGER_SIZE).put(buffer, 0, INTEGER_SIZE);
				dataLength = bb.getInt(0);
				
				break;
			}
		}
		
		//make sure the stream did not end
		if(END_OF_STREAM != n)
		{
			//reset the total read
			totalRead = 0;
			
			int remaining = dataLength;
			
			// while there is more data to be read
			while ((n = is.read(buffer, 0, (remaining > INTEGER_SIZE ? INTEGER_SIZE : remaining))) != END_OF_STREAM && remaining > 0)
			{
				//increment the total bytes read
				totalRead += n;
				remaining = dataLength - totalRead;
				
				// write the buffer to the data stream
				baos.write(buffer, 0, n);
			}
			
			return baos.toByteArray();
		}
		else
		{
			return null;
		}
	}
}
