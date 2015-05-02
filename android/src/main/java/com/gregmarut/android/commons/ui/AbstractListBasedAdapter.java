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
package com.gregmarut.android.commons.ui;

import java.util.List;

import android.widget.BaseAdapter;

/**
 * Handles some of the base functionality for using a list
 * 
 * @author Greg Marut
 * @param <E>
 */
public abstract class AbstractListBasedAdapter<E> extends BaseAdapter
{
	// holds the list of objects
	private final List<E> list;
	
	public AbstractListBasedAdapter(final List<E> list)
	{
		this.list = list;
	}
	
	@Override
	public int getCount()
	{
		return list.size();
	}
	
	@Override
	public E getItem(int position)
	{
		return list.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	/**
	 * Returns the list of objects
	 * 
	 * @return
	 */
	public List<E> getList()
	{
		return list;
	}
}
