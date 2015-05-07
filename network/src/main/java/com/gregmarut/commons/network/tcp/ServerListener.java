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
public interface ServerListener
{
	/**
	 * invoked when data has been received
	 * @param event
	 */
	void dataReceived(ServerEvent event);
	
	/**
	 * invoked when the server has made a connection
	 * @param event
	 */
	void connectionEstablished(ServerEvent event);
	
	/**
	 * invoked when the server has lost a connection
	 * @param event
	 */
	void connectionLost(ServerEvent event);
	
	/**
	 * invoked when a server recives a connection request
	 * @param event
	 */
	void connectionRequest(ServerEvent event);
}
