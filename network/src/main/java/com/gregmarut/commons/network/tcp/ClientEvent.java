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

/**
 * @author Greg Marut
 */
public class ClientEvent
{
	// create a Client to store the source
	private final Client<?> client;
	
	// stores the data
	private final byte[] data;
	
	/**
	 * The constructor for ClientEvent
	 * 
	 * @param source
	 */
	ClientEvent(final Client<?> client)
	{
		// save the source object
		this.client = client;
		
		// set the data to nothing
		data = null;
	}
	
	/**
	 * the constructor for ClientEvent
	 * 
	 * @param source
	 * @param serializable
	 */
	ClientEvent(final Client<?> source, final byte[] data)
	{
		// save the source object
		this.client = source;
		
		// save the data
		this.data = data;
	}
	
	/**
	 * Returns the client
	 * 
	 * @return Client
	 */
	public Client<?> getClient()
	{
		return client;
	}
	
	/**
	 * Returns the data
	 * 
	 * @return byte[]
	 */
	public byte[] getData()
	{
		return data;
	}
}
