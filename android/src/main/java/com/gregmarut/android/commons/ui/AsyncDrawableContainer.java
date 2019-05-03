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

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Defines that a class is able to be used as a container for asynchronous loading calls for loading
 * images
 * 
 * @author Greg
 */
public abstract class AsyncDrawableContainer<ID, Entity> implements Serializable
{
	private transient Drawable drawable;
	
	public void setDrawable(Drawable drawable)
	{
		this.drawable = drawable;
	}
	
	public Drawable getDrawable()
	{
		return drawable;
	}
	
	public abstract ID getID();
	
	public abstract Entity getEntity();
}
