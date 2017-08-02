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
package com.gregmarut.commons.util.weak;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows for handling clean up code on any object that is still remaining in memory. Objects are held with a
 * weak reference and can be GCed at any time without notifying this object
 *
 * @param <T>
 * @author Greg Marut
 */
public abstract class WeakReferenceCleanup<T>
{
	// holds the list of objects
	private final List<EquatableWeakReference<T>> weakObjects;
	
	public WeakReferenceCleanup()
	{
		weakObjects = new ArrayList<EquatableWeakReference<T>>();
	}
	
	public void add(T object)
	{
		synchronized (weakObjects)
		{
			weakObjects.add(new EquatableWeakReference<T>(object));
		}
	}
	
	public boolean remove(T object)
	{
		synchronized (weakObjects)
		{
			return weakObjects.remove(object);
		}
	}
	
	public void clear()
	{
		synchronized (weakObjects)
		{
			weakObjects.clear();
		}
	}
	
	/**
	 * Cleans up any weak references that are still in memory
	 *
	 * @return the number of objects that were cleaned up
	 */
	public final int cleanUp()
	{
		// holds the list of objects to remove
		List<EquatableWeakReference<T>> weakObjectsToRemove = new ArrayList<EquatableWeakReference<T>>();
		
		// holds the number of objects that were cleaned up
		int count = 0;
		
		synchronized (weakObjects)
		{
			// for each of the objects
			for (EquatableWeakReference<T> weakObject : weakObjects)
			{
				// make sure the object is not null
				if (null != weakObject)
				{
					// get a hard reference to the object
					T object = weakObject.get();
					
					// make sure the object is not null
					if (null != object)
					{
						// clean up this object
						cleanUp(object);
						count++;
					}
					else
					{
						// need to remove this from the list
						weakObjectsToRemove.add(weakObject);
					}
				}
			}
			
			// check to see if there are objects to remove
			if (!weakObjectsToRemove.isEmpty())
			{
				// remove these objects from the list
				weakObjects.removeAll(weakObjectsToRemove);
			}
		}
		
		return count;
	}
	
	protected abstract void cleanUp(T object);
}
