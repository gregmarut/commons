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

import com.gregmarut.commons.encryption.AESEncryption;
import com.gregmarut.commons.encryption.Encryption;
import com.gregmarut.commons.encryption.EncryptionException;
import com.gregmarut.commons.network.tcp.Client;

/**
 * Provides the exact same functionality as a Client but also provides added
 * security by encrypting data that is transmitted over the sockets.
 * 
 * @author Greg Marut
 */
public abstract class SecureClient<S> extends Client<S>
{
	// ** Finals **//
	// holds the algorithm to use for this encryption
	public static final int ITERATION_COUNT = 1024;
	public static final int SALT_LENGTH = 8;
	
	// ** Objects **//
	// holds the object responsible for encrypting and decrypting the data
	private final Encryption encryption;
	
	/**
	 * The constructor for SecureClient
	 * 
	 * @param host
	 * @param port
	 * @param encryptionKey
	 * @throws EncryptionException
	 */
	public SecureClient(final String encryptionKey)
		throws EncryptionException
	{
		this(encryptionKey.getBytes());
	}
	
	/**
	 * The constructor for SecureClient
	 * 
	 * @param host
	 * @param port
	 * @param encryptionKey
	 * @throws EncryptionException
	 */
	public SecureClient(final byte[] encryptionKey) throws EncryptionException
	{
		encryption = new AESEncryption(encryptionKey);
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
	 * Returns the client activity to use for this client
	 * 
	 * @return
	 */
	@Override
	protected ClientActivity getClientActivity()
	{
		return new SecureClientActivity();
	}
	
	/**
	 * A class used to handle the connection process to the server
	 * 
	 * @author Greg
	 */
	protected class SecureClientActivity extends Client<S>.ClientActivity
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
			
			// make sure the data is not null
			if (null != encryptedData)
			{
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
			else
			{
				return null;
			}
		}
	}
}
