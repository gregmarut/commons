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
package com.gregmarut.commons.util.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtil
{
	private ListUtil()
	{
		
	}
	
	/**
	 * Converts a list from one type of object to another
	 * 
	 * @param source
	 *            the original list
	 * @param convert
	 *            the blueprint for how to convert the object
	 * @return
	 */
	public static <S, T> List<T> convert(final List<S> sourceList, final Convert<S, T> convert)
	{
		// holds the list of target objects to return
		final List<T> targetList = new ArrayList<T>(sourceList.size());
		
		// for each of the items in the list
		for (S source : sourceList)
		{
			// convert the object to a target object and add it to the list
			targetList.add(convert.convert(source));
		}
		
		return targetList;
	}
	
	/**
	 * Converts a list of values into a map using the {@link Convert} interface as a template for how to extract the map
	 * key from the value object
	 * 
	 * @param values
	 * @param extractKey
	 * @return
	 */
	public static <K, V> Map<K, V> toMap(final List<V> values, final Convert<V, K> extractKey)
	{
		// holds the result map to be built from the list of values
		final Map<K, V> map = new HashMap<K, V>();
		
		// for each of the values in the list
		for (V value : values)
		{
			// extract the key from the value object
			K key = extractKey.convert(value);
			
			// add this entry to the map
			map.put(key, value);
		}
		
		return map;
	}
	
	/**
	 * Finds an object in the list and replaces it with another
	 * 
	 * @param list
	 *            the list of objects
	 * @param obj
	 *            the object to replace or add
	 * @return whether or not the object was replaced in the list
	 */
	public static <T> boolean replace(final List<T> list, final T obj)
	{
		return replace(list, obj, new DefaultEquals<T>());
	}
	
	/**
	 * Finds an object in the list and replaces it with another
	 * 
	 * @param list
	 *            the list of objects
	 * @param obj
	 *            the object to replace or add
	 * @param equals
	 *            an interface which provides the ability to override the default equals logic
	 * @return whether or not the object was replaced in the list
	 */
	public static <T> boolean replace(final List<T> list, final T obj, final Equals<T> equals)
	{
		// place a lock on the list as we modify it
		synchronized (list)
		{
			// find the target object in the list
			T target = find(list, obj, equals);
			
			// check to see if the target object is not null
			if (null != target)
			{
				// retrieve the index of this element
				int index = list.indexOf(target);
				list.add(index, obj);
				
				// remove the original target object
				list.remove(target);
				
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	/**
	 * Finds an object in the list and replaces it with another. If no match is found, the object is instead added
	 * 
	 * @param list
	 *            the list of objects
	 * @param obj
	 *            the object to replace or add
	 */
	public static <T> void replaceOrAdd(final List<T> list, final T obj)
	{
		replaceOrAdd(list, obj, new Equals<T>()
		{
			@Override
			public boolean equals(T obj1, T obj2)
			{
				return obj1.equals(obj2);
			}
		});
	}
	
	/**
	 * Finds an object in the list and replaces it with another. If no match is found, the object is instead added
	 * 
	 * @param list
	 *            the list of objects
	 * @param obj
	 *            the object to replace or add
	 * @param equals
	 *            an interface which provides the ability to override the default equals logic
	 */
	public static <T> void replaceOrAdd(final List<T> list, final T obj, final Equals<T> equals)
	{
		// place a lock on the list as we modify it
		synchronized (list)
		{
			// find the target object in the list
			T target = find(list, obj, equals);
			
			// check to see if the target object is not null
			if (null != target)
			{
				// retrieve the index of this element
				int index = list.indexOf(target);
				list.add(index, obj);
				
				// remove the original target object
				list.remove(target);
			}
			else
			{
				list.add(obj);
			}
		}
	}
	
	/**
	 * Searches a list to find an object that matches
	 * 
	 * @param list
	 * @param obj
	 * @return
	 */
	public static <T> T find(final List<T> list, final T obj, final Equals<T> equals)
	{
		// make sure the object is not null
		if (null != obj)
		{
			// for each of the items in the list
			for (T t : list)
			{
				if (equals.equals(obj, t))
				{
					return t;
				}
			}
		}
		
		return null;
	}
}
