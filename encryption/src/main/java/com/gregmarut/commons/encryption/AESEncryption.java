/*******************************************************************************
 * <pre>
 * Copyright (c) 2015 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Greg Marut - initial API and implementation
 * </pre>
 ******************************************************************************/
package com.gregmarut.commons.encryption;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class AESEncryption extends Encryption
{
	private static final String DEFAULT_ALGORITHM = "AES";
	
	public AESEncryption(final String encryptionKey) throws EncryptionException
	{
		this(encryptionKey.getBytes());
	}
	
	public AESEncryption(final byte[] encryptionKey) throws EncryptionException
	{
		super(encryptionKey, DEFAULT_ALGORITHM);
	}
	
	/**
	 * Determines if the encryption is supported by this device. Therefore a device can test whether or not to use this
	 * filesystem before causing errors later.
	 * 
	 * @return
	 */
	public static boolean isSupported()
	{
		try
		{
			Cipher.getInstance(DEFAULT_ALGORITHM);
			
			return true;
		}
		catch (NoSuchAlgorithmException e)
		{
			return false;
		}
		catch (NoSuchPaddingException e)
		{
			return false;
		}
	}
}
