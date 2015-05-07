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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.gregmarut.commons.util.ByteArray;

public class Encryption
{
	// holds the objects to encrypt and decrypt
	private final Cipher encrypt;
	private final Cipher decrypt;
	
	// holds the key spec that will be used for the encryption
	private final SecretKeySpec keySpec;
	
	public Encryption(final byte[] encryptionKey, final String algorithm) throws EncryptionException
	{
		try
		{
			// create the key spec using the provided encryption key
			keySpec = new SecretKeySpec(encryptionKey, algorithm);
			
			encrypt = Cipher.getInstance(algorithm);
			encrypt.init(Cipher.ENCRYPT_MODE, keySpec);
			
			decrypt = Cipher.getInstance(algorithm);
			decrypt.init(Cipher.DECRYPT_MODE, keySpec);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new EncryptionException(e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new EncryptionException(e);
		}
		catch (InvalidKeyException e)
		{
			throw new EncryptionException(e);
		}
	}
	
	/**
	 * Encrypts the input and returns the result as a byte array
	 * 
	 * @param input
	 * @return
	 * @throws EncryptionException
	 */
	public final byte[] encrypt(final byte[] input) throws EncryptionException
	{
		try
		{
			return encrypt.doFinal(input);
		}
		catch (IllegalBlockSizeException e)
		{
			throw new EncryptionException(e);
		}
		catch (BadPaddingException e)
		{
			throw new EncryptionException(e);
		}
	}
	
	/**
	 * Decrypts the input and returns the result as a byte array
	 * 
	 * @param input
	 * @return
	 * @throws EncryptionException
	 */
	public final byte[] decrypt(final byte[] input) throws EncryptionException
	{
		try
		{
			return decrypt.doFinal(input);
		}
		catch (IllegalBlockSizeException e)
		{
			throw new EncryptionException(e);
		}
		catch (BadPaddingException e)
		{
			throw new EncryptionException(e);
		}
	}
	
	/**
	 * Encrypts an input to a hex string
	 * 
	 * @param input
	 * @return
	 * @throws EncryptionException
	 */
	public final String encryptToHex(final byte[] input) throws EncryptionException
	{
		return toHexString(encrypt(input));
	}
	
	/**
	 * Decrypts an input from a hex string
	 * 
	 * @param hexString
	 * @return
	 * @throws EncryptionException
	 */
	public final byte[] decryptFromHex(final String hexString) throws EncryptionException
	{
		try
		{
			return decrypt(fromHexString(hexString));
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new EncryptionException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new EncryptionException(e);
		}
	}
	
	/**
	 * Converts the byte array to a hex string
	 * 
	 * @param bytes
	 * @return
	 */
	protected String toHexString(final byte[] bytes)
	{
		return new ByteArray(bytes).toHexString();
	}
	
	/**
	 * Converts the hex string to a byte array
	 * 
	 * @param hexString
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	protected byte[] fromHexString(final String hexString) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if (null == hexString)
		{
			throw new IllegalArgumentException("Hex string cannot be null");
		}
		
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
		{
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		
		return result;
		
	}
}
