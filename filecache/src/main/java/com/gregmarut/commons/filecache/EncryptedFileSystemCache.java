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
package com.gregmarut.commons.filecache;

import java.io.File;

import com.gregmarut.commons.encryption.AESEncryption;
import com.gregmarut.commons.encryption.Encryption;
import com.gregmarut.commons.encryption.EncryptionException;

public class EncryptedFileSystemCache extends FileSystemCache
{
	// holds the encryption object
	private Encryption encryption;
	
	public EncryptedFileSystemCache(final File root, final String encryptionKey) throws EncryptionException
	{
		this(root, encryptionKey.getBytes());
	}
	
	public EncryptedFileSystemCache(final File root, final byte[] encryptionKey) throws EncryptionException
	{
		super(root);
		
		encryption = new AESEncryption(encryptionKey);
	}
	
	@Override
	public byte[] load(String relativePath)
	{
		try
		{
			// load the encrypted data from the file system
			byte[] encryptedData = super.load(relativePath);
			
			// make sure the data is not null
			if (null != encryptedData)
			{
				try
				{
					// return the decrypted data
					return encryption.decrypt(encryptedData);
				}
				catch (EncryptionException e)
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
		catch (CacheException e)
		{
			return null;
		}
	}
	
	@Override
	public void save(String relativePath, byte[] data) throws CacheException
	{
		try
		{
			// encrypt the data
			byte[] encryptedData = encryption.encrypt(data);
			
			// save the encrypted data to the file system
			super.save(relativePath, encryptedData);
		}
		catch (EncryptionException e)
		{
			throw new CacheException(e);
		}
	}
}
