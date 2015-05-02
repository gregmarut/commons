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

import com.gregmarut.android.commons.task.CallBackAsyncTask;
import com.gregmarut.android.commons.ui.BitmapLoader;

public abstract class LoadBitmapImageTask<Params> extends CallBackAsyncTask<Params, Bitmap>
{
	// holds the bitmap loader
	protected final BitmapLoader bitmapLoader;
	
	// holds the image size
	protected final int imageWidth;
	protected final int imageHeight;
	
	public LoadBitmapImageTask(final BitmapLoader bitmapLoader, final int imageSize)
	{
		this(bitmapLoader, imageSize, imageSize);
	}
	
	public LoadBitmapImageTask(final BitmapLoader bitmapLoader, final int imageWidth, final int imageHeight)
	{
		// save the bitmap loader
		this.bitmapLoader = bitmapLoader;
		
		// save the image size
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}
	
	protected int getImageSize()
	{
		return Math.min(imageWidth, imageHeight);
	}
	
	protected Bitmap loadBitmap(byte[] data)
	{
		return bitmapLoader.decodeSampledBitmap(data, imageWidth, imageHeight);
	}
}
