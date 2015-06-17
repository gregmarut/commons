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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * An extension of WeakReference that implements a same equals and hashcode method.
 * 
 * @param <T>
 *            The type of object that this reference contains
 */
public class EquatableWeakReference<T> extends WeakReference<T>
{
	/**
	 * Creates a new instance of EquatableWeakReference.
	 * 
	 * @param referent
	 *            The object that this weak reference should reference.
	 */
	public EquatableWeakReference(T referent)
	{
		super(referent);
	}
	
	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object other)
	{
		// retrieve this object from the reference
		T obj = get();
		
		// make sure the object and the other object are not null
		if (null != obj && null != other)
		{
			// check to see if this object is a reference object
			if (other instanceof Reference)
			{
				// compare this object to the object in the other reference
				return obj.equals(((Reference) other).get());
			}
			else
			{
				// compare this object to the other object
				return obj.equals(other);
			}
		}
		else
		{
			return false;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode()
	{
		// retrieve this object from the reference
		T obj = get();
		
		// make sure this object is not null
		if (null != obj)
		{
			return get().hashCode();
		}
		else
		{
			return 0;
		}
	}
}
