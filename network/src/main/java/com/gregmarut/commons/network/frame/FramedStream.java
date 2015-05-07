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

public abstract class FramedStream
{
	// ** Finals **//
	// holds the buffer size for incoming data
	public static final int BUFFER_SIZE = 4096;
	
	// identifies when the end of the stream has been reached
	public static final int END_OF_STREAM = -1;
	
	// holds the number of bytes to represent an integer
	public static final int INTEGER_SIZE = Integer.SIZE / Byte.SIZE;
}
