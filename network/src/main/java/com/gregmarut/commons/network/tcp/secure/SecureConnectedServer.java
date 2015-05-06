/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.commons.network.tcp.secure;

import java.io.IOException;
import java.util.List;

import com.gregmarut.commons.encryption.Encryption;
import com.gregmarut.commons.encryption.EncryptionException;
import com.gregmarut.commons.network.InvalidStateException;
import com.gregmarut.commons.network.tcp.ConnectedServer;
import com.gregmarut.commons.network.tcp.GenericSocket;
import com.gregmarut.commons.network.tcp.ServerListener;

/**
 * Provides the exact same functionality as a ConnectedServer but also provides added
 * security by encrypting data that is transmitted over the sockets.
 * 
 * @author Greg Marut
 */
public class SecureConnectedServer<S> extends ConnectedServer<S>
{
	// ** Objects **//
	// holds the cipher object responsible for encrypting and decrypting the data
	private final Encryption encryption;
	
	/**
	 * The constructor for SecureConnectedServer
	 * 
	 * @param socket
	 * @param serverListeners
	 * @throws InvalidStateException
	 */
	SecureConnectedServer(final GenericSocket<S> socket, final List<ServerListener> serverListeners,
		final Encryption encryption)
	{
		super(socket, serverListeners);
		
		// save the encryption object
		this.encryption = encryption;
	}
	
	/**
	 * Writes the data to the output stream
	 * 
	 * @param objData
	 * @throws IOException
	 */
	@Override
	protected void writeData(final byte[] decryptedData) throws IOException
	{
		// holds the encrypted data
		byte[] encryptedData;
		
		try
		{
			// encrypt the data
			encryptedData = encryption.encrypt(decryptedData);
			
			// write the data to the output stream
			out.writeFrame(encryptedData);
		}
		catch (EncryptionException e)
		{
			throw new IOException(e);
		}
	}
	
	/**
	 * Returns the server activity to use for this server
	 * 
	 * @return
	 */
	@Override
	protected ServerActivity getServerActivity()
	{
		return new SecureServerActivity();
	}
	
	/**
	 * A class used to handle the listening process of the server
	 * 
	 * @author Greg Marut
	 */
	protected class SecureServerActivity extends ConnectedServer<S>.ServerActivity
	{
		/**
		 * Waits for data to be read from the reader
		 * 
		 * @return
		 * @throws IOException
		 */
		@Override
		protected byte[] readData() throws IOException
		{
			// holds the encrypted data
			byte[] encryptedData = super.readData();
			
			try
			{
				// decrypt the data
				byte[] decryptedData = encryption.decrypt(encryptedData);
				
				return decryptedData;
			}
			catch (EncryptionException e)
			{
				throw new IOException(e);
			}
		}
	}
}
