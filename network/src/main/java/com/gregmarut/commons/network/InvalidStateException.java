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
package com.gregmarut.commons.network;

public class InvalidStateException extends RuntimeException
{
	// ** Finals **//
	private static final long serialVersionUID = 314233364934174888L;
	
	public InvalidStateException()
	{
		
	}
	
	/**
	 * Constructs an InvalidStateException with a root cause
	 * @param cause
	 */
	public InvalidStateException(final Throwable cause)
	{
		super(cause);
	}
}
