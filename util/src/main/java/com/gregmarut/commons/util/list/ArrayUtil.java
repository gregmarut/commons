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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ArrayUtil
{
	private ArrayUtil()
	{
		
	}
	
	/**
	 * Converts an array from one type of object to another
	 * 
	 * @param source
	 *            the original array
	 * @param targetClass
	 *            the class of the target array to return
	 * @param convert
	 *            the blueprint for how to convert the object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> T[] convert(final S[] sourceArray, final Class<T> targetClass, final Convert<S, T> convert)
	{
		// holds the array of target objects to return
		final T[] targetArray = (T[]) Array.newInstance(targetClass, sourceArray.length);
		
		// for every item in the source array
		for (int i = 0; i < sourceArray.length; i++)
		{
			// convert the object to a target object and add it to the array
			targetArray[i] = convert.convert(sourceArray[i]);
		}
		
		return targetArray;
	}
	
	/**
	 * Converts an array of values into a map using the {@link Convert} interface as a template for how to extract the
	 * map key from the value object
	 * 
	 * @param values
	 * @param extractKey
	 * @return
	 */
	public static <K, V> Map<K, V> toMap(final V[] values, final Convert<V, K> extractKey)
	{
		// holds the result map to be built from the array of values
		final Map<K, V> map = new HashMap<K, V>();
		
		// for each of the values in the array
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
	 * Finds an object in the array and replaces it with another
	 * 
	 * @param array
	 *            the array of objects
	 * @param obj
	 *            the object to replace or add
	 * @return whether or not the object was replaced in the array
	 */
	public static <T> boolean replace(final T[] array, final T obj)
	{
		return replace(array, obj, new DefaultEquals<T>());
	}
	
	/**
	 * Finds an object in the array and replaces it with another
	 * 
	 * @param array
	 *            the array of objects
	 * @param obj
	 *            the object to replace or add
	 * @param equals
	 *            an interface which provides the ability to override the default equals logic
	 * @return whether or not the object was replaced in the array
	 */
	public static <T> boolean replace(final T[] array, final T obj, final Equals<T> equals)
	{
		// place a lock on the array as we modify it
		synchronized (array)
		{
			// for each of the items in the array
			for (int i = 0; i < array.length; i++)
			{
				if (equals.equals(obj, array[i]))
				{
					array[i] = obj;
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Searches a array to find an object that matches
	 * 
	 * @param array
	 * @param obj
	 * @return
	 */
	public static <T> T find(final T[] array, final T obj, final Equals<T> equals)
	{
		// make sure the object is not null
		if (null != obj)
		{
			// for each of the items in the array
			for (T t : array)
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
