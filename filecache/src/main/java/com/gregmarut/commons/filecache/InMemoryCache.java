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

import java.util.HashMap;
import java.util.Map;

public class InMemoryCache implements FileCache
{
	// holds the map that will contain the in-memory cache
	private final Map<String, byte[]> cache;
	
	public InMemoryCache()
	{
		cache = new HashMap<String, byte[]>();
	}
	
	@Override
	public void delete(String relativePath)
	{
		cache.remove(relativePath);
	}
	
	@Override
	public byte[] load(String relativePath)
	{
		return cache.get(relativePath);
	}
	
	@Override
	public void save(String relativePath, byte[] data)
	{
		cache.put(relativePath, data);
	}
	
	@Override
	public boolean exists(String relativePath)
	{
		return cache.containsKey(relativePath);
	}
	
	@Override
	public void clear()
	{
		cache.clear();
	}
}
