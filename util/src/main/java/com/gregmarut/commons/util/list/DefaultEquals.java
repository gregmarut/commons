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

/**
 * Represents the default java equals method
 * 
 * @author greg
 * @param <T>
 */
public class DefaultEquals<T> implements Equals<T>
{
	@Override
	public boolean equals(T obj1, T obj2)
	{
		return obj1.equals(obj2);
	}
}
