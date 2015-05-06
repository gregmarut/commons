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
 * Represents a template for how to convert one object to another
 * 
 * @author Greg Marut
 * @param <S>
 * @param <T>
 */
public interface Convert<S, T>
{
	T convert(S source);
}
