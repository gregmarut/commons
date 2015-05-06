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
public class ServerEvent
{
	// create a Server to store the source
	private final ListeningServer<?, ?> listeningServer;
	
	// stores the data
	private final byte[] data;
	
	// stores a connected server
	private final ConnectedServer<?> connectedServer;
	
	/**
	 * The constructor for ServerEvent
	 * 
	 * @param svrSource
	 */
	public ServerEvent(final ConnectedServer<?> connectedServer)
	{
		// save the source object
		this.connectedServer = connectedServer;
		
		// set the objects to null
		this.data = null;
		this.listeningServer = null;
	}
	
	/**
	 * the constructor for ServerEvent
	 * 
	 * @param theSource
	 * @param objData
	 */
	public ServerEvent(final ConnectedServer<?> connectedServer, final byte[] data)
	{
		// save the source object
		this.connectedServer = connectedServer;
		
		// save the data
		this.data = data;
		
		// set the listeningserver to null
		this.listeningServer = null;
	}
	
	/**
	 * the constructor for ServerEvent
	 * 
	 * @param theSource
	 * @param objLiveSocket
	 */
	public ServerEvent(final ListeningServer<?, ?> listeningServer, final ConnectedServer<?> connectedServer)
	{
		// save the source object
		this.listeningServer = listeningServer;
		
		// set the connected server
		this.connectedServer = connectedServer;
		
		// set the data to null
		this.data = null;
	}
	
	/**
	 * Returns the listening server
	 * 
	 * @return
	 */
	public Server getListeningServer()
	{
		return listeningServer;
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
	
	/**
	 * Returns the connected server
	 * 
	 * @return
	 */
	public ConnectedServer<?> getConnectedServer()
	{
		return connectedServer;
	}
}
