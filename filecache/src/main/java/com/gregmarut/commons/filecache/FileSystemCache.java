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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class FileSystemCache implements FileCache
{
	// holds the root directory
	protected final File root;
	
	public FileSystemCache(final File root)
	{
		this.root = root;
		
		// make sure the file is not null
		if (null == root)
		{
			throw new IllegalArgumentException("Root cache cannot be null.");
		}
		
		// make the directories if needed
		root.mkdirs();
		
		// make sure the file is a directory
		if (!root.isDirectory())
		{
			throw new IllegalArgumentException("Root cache must be a directory.");
		}
	}
	
	@Override
	public byte[] load(final String relativePath) throws IOException
	{
		// retrieve the file for this path
		File file = getFile(relativePath);
		
		// load the data from the file
		return IOUtils.toByteArray(new FileInputStream(file));
	}
	
	@Override
	public void save(final String relativePath, final byte[] data) throws CacheException
	{
		// retrieve the file for this path
		File file = getFile(relativePath);
		
		// make the necessary directories if needed
		file.getParentFile().mkdirs();
		
		// holds the output stream to write to
		FileOutputStream out = null;
		
		try
		{
			// write the data to the file
			out = new FileOutputStream(file);
			out.write(data);
		}
		catch (IOException e)
		{
			// wrap the exception
			throw new CacheException(e);
		}
		finally
		{
			// make sure the output stream is not null
			if (null != out)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					// ignore this exception
				}
			}
		}
	}
	
	@Override
	public boolean exists(String relativePath)
	{
		// retrieve the file for this path
		File file = getFile(relativePath);
		return file.exists();
	}
	
	@Override
	public void delete(final String relativePath)
	{
		// retrieve the file for this path
		File file = getFile(relativePath);
		
		// delete the file/folder and all of its contents
		recursiveDelete(file);
	}
	
	@Override
	public void clear()
	{
		// recursively delete the root folder
		recursiveDelete(root.listFiles());
	}
	
	/**
	 * Recursively deletes the file/folder and any of its children
	 * 
	 * @param file
	 */
	private void recursiveDelete(final File... files)
	{
		// make sure the array of files is not null
		if (null != files)
		{
			// for each of the files
			for (File file : files)
			{
				// make sure this file is not null
				if (null != file)
				{
					// check to see if this file is a directory
					if (file.isDirectory())
					{
						// get all of the files in this folder
						File[] children = file.listFiles();
						
						// make sure the files are not null
						if (null != children)
						{
							// recursively delete the files
							recursiveDelete(children);
						}
					}
					
					// delete the file
					file.delete();
				}
			}
		}
	}
	
	private File getFile(final String relativePath)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(root.getAbsolutePath());
		
		// check to see if a / is needed
		if (!relativePath.startsWith("/") && !relativePath.startsWith("\\"))
		{
			sb.append("/");
		}
		
		// append the relative path
		sb.append(relativePath);
		
		return new File(sb.toString());
	}
	
	public File getRoot()
	{
		return root;
	}
}
