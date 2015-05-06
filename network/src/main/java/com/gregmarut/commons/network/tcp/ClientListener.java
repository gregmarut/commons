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
public interface ClientListener
{
	/**
	 * invoked when data has been received
	 * @param event
	 */
	void dataReceived(ClientEvent event);
	
	/**
	 * invoked when the client has made a connection
	 * @param event
	 */
	void connectionEstablished(ClientEvent event);
	
	/**
	 * invoked when the client has lost a connection
	 * @param event
	 */
	void connectionLost(ClientEvent event);
	
	/**
	 * invoked when a client times out
	 * @param event
	 */
	void connectionError(ClientEvent event);
}
