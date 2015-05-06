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
 * Represents a template for comparing two objects to determine if they are equal
 * 
 * @author Greg Marut
 * @param <T>
 */
public interface Equals<T>
{
	public boolean equals(final T obj1, final T obj2);
}
