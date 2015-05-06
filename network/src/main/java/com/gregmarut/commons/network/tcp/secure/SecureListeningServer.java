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

import java.util.ArrayList;

import com.gregmarut.commons.encryption.AESEncryption;
import com.gregmarut.commons.encryption.Encryption;
import com.gregmarut.commons.encryption.EncryptionException;
import com.gregmarut.commons.network.tcp.ConnectedServer;
import com.gregmarut.commons.network.tcp.GenericSocket;
import com.gregmarut.commons.network.tcp.ListeningServer;
import com.gregmarut.commons.network.tcp.ServerListener;

/**
 * Provides the exact same functionality as a ListeningServer but also provides added
 * security by encrypting data that is transmitted over the sockets.
 * 
 * @author Greg Marut
 */
public abstract class SecureListeningServer<L, S> extends ListeningServer<L, S>
{
	// ** Finals **//
	// holds the algorithm to use for this encryption
	public static final String ALGORITHM = "PBEWithMD5AndDES";
	public static final int ITERATION_COUNT = 1024;
	public static final int SALT_LENGTH = 8;
	
	// ** Objects **//
	// holds the object responsible for encrypting and decrypting the data
	private final Encryption encryption;
	
	/**
	 * The constructor for SecureListeningServer
	 * 
	 * @param port
	 * @param encryptionKey
	 * @throws EncryptionException
	 */
	public SecureListeningServer(final String encryptionKey) throws EncryptionException
	{
		this(encryptionKey.getBytes());
	}
	
	/**
	 * The constructor for SecureListeningServer
	 * 
	 * @param port
	 * @param strEncryptionKey
	 * @throws EncryptionException
	 */
	public SecureListeningServer(final byte[] encryptionKey) throws EncryptionException
	{
		encryption = new AESEncryption(encryptionKey);
	}
	
	/**
	 * Returns the server activity to use for this server
	 * 
	 * @return
	 */
	protected ServerActivity getServerActivity()
	{
		return new ServerActivity();
	}
	
	/**
	 * A class used to handle the listening process of the server
	 * 
	 * @author Greg Marut
	 */
	protected class ServerActivity extends ListeningServer<L, S>.ServerActivity
	{
		@Override
		protected ConnectedServer<S> createNewConnectedServer(GenericSocket<S> socket)
		{
			// return a new instance of a connected server
			return new SecureConnectedServer<S>(socket,
				new ArrayList<ServerListener>(serverListeners),
				encryption);
		}
	}
}
