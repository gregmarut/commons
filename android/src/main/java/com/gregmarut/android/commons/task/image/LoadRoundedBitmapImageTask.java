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
package com.gregmarut.android.commons.task.image;

import android.graphics.Bitmap;

import com.gregmarut.android.commons.ui.BitmapLoader;

public abstract class LoadRoundedBitmapImageTask<Params> extends LoadBitmapImageTask<Params>
{
	// holds the color of the border
	protected final int borderColor;
	protected final int borderSize;
	
	public LoadRoundedBitmapImageTask(final BitmapLoader bitmapLoader, final int imageSize, final int borderColor,
		final int borderSize)
	{
		super(bitmapLoader, imageSize);
		this.borderColor = borderColor;
		this.borderSize = borderSize;
	}
	
	@Override
	protected Bitmap loadBitmap(byte[] data)
	{
		Bitmap originalBitmap = super.loadBitmap(data);
		
		// check to see if the bitmap was loaded
		if (null != originalBitmap)
		{
			return bitmapLoader.cropRoundBitmap(originalBitmap, borderColor, borderSize);
		}
		else
		{
			return null;
		}
	}
}
