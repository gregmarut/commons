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

import java.io.IOException;

public interface FileCache
{
	/**
	 * Deletes a file/folder and any children that may exist under the relative path
	 * 
	 * @param relativePath
	 */
	void delete(final String relativePath);
	
	/**
	 * Loads the contents of a file as a byte array
	 * 
	 * @param relativePath
	 * @return
	 * @throws CacheException
	 */
	byte[] load(final String relativePath) throws IOException;
	
	/**
	 * Saves the content to the file specified at the relative path
	 * 
	 * @param relativePath
	 * @param data
	 * @throws CacheException
	 */
	void save(final String relativePath, final byte[] data) throws CacheException;
	
	/**
	 * Checks to see if the file exists at this location
	 * 
	 * @param relativePath
	 * @return
	 */
	boolean exists(final String relativePath);
	
	/**
	 * Clears the entire cache
	 */
	void clear();
}
