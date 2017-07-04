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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.gregmarut.android.commons.weak.WeakBitmapRecycler;

/**
 * One class that is responsible for loading and tracking (with weak references) bitmaps. This
 * allows for all created bitmaps to be easily destroyed by calling {@link destroy()}.
 * <p/>
 * Due to the fact that bitmaps are very resource hungry and mobile devices may not have much memory
 * to spare, this class helps manage and clean up bitmaps to prevent memory leaks.
 *
 * @author Greg Marut
 */
public class BitmapLoader
{
	// holds the object that will recycle all bitmaps
	private WeakBitmapRecycler weakBitmapRecycler;
	
	public BitmapLoader()
	{
		weakBitmapRecycler = new WeakBitmapRecycler();
	}
	
	public Bitmap decodeSampledBitmap(final byte[] data)
	{
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		
		// Decode bitmap with inSampleSize set
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		
		// make sure that this bitmap is not null
		if (null != bitmap)
		{
			// add this bitmap to the list to keep track of it
			weakBitmapRecycler.add(bitmap);
		}
		
		return bitmap;
	}
	
	public Bitmap decodeSampledBitmap(final byte[] data, final int requiredSize)
	{
		return decodeSampledBitmap(data, requiredSize, requiredSize);
	}
	
	public Bitmap decodeSampledBitmap(final byte[] data, final int requiredWidth, final int requiredHeight)
	{
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		
		// make sure that this bitmap is not null
		if (null != bitmap)
		{
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, false);
			
			// add this bitmap to the list to keep track of it
			weakBitmapRecycler.add(scaledBitmap);
			
			return scaledBitmap;
		}
		else
		{
			return null;
		}
	}
	
	public Bitmap decodeSampledBitmap(final Resources resources, final int id, final int requiredWidth,
		final int requiredHeight)
	{
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, id, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, id, options);
		
		// make sure that this bitmap is not null
		if (null != bitmap)
		{
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, false);
			
			// add this bitmap to the list to keep track of it
			weakBitmapRecycler.add(scaledBitmap);
			
			return scaledBitmap;
		}
		else
		{
			return null;
		}
	}
	
	public Bitmap createBitmap(final int[] pixels, final int offset, final int stride, final int width,
		final int height,
		final Config config)
	{
		Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, config);
		
		// add this bitmap to the list to keep track of it
		weakBitmapRecycler.add(bitmap);
		
		return bitmap;
	}
	
	private int calculateInSampleSize(final BitmapFactory.Options options, final int requiredWidth,
		final int requiredHeight)
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > requiredHeight || width > requiredWidth)
		{
			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) requiredHeight);
			final int widthRatio = Math.round((float) width / (float) requiredWidth);
			
			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		
		return inSampleSize;
	}
	
	/**
	 * Crops the original bitmap into a round one
	 *
	 * @param bitmap
	 * @param borderColor
	 * @param borderSize
	 * @return
	 */
	public Bitmap cropRoundBitmap(final Bitmap bitmap, final int borderColor, final int borderSize)
	{
		return cropRoundBitmap(bitmap, borderColor, borderSize, true);
	}
	
	/**
	 * Crops the original bitmap into a round one
	 *
	 * @param bitmap
	 * @param recycleOriginal
	 * @return
	 */
	public Bitmap cropRoundBitmap(final Bitmap bitmap, final int borderColor, final int borderSize,
		final boolean recycleOriginal)
	{
		final int halfBorderSize = borderSize / 2;
		
		// create a target bitmap to draw the circular image to
		Bitmap targetBitmap = Bitmap.createBitmap(bitmap.getWidth(),
			bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		
		// create the canvas that will restrict the image to an ellipse
		Canvas canvas = new Canvas(targetBitmap);
		
		// set up the paint for copying over the bitmap
		final Paint bitmapPaint = new Paint();
		bitmapPaint.setAntiAlias(true);
		
		// create the paint for the border around the image
		final Paint borderPaint = new Paint();
		borderPaint.setStrokeWidth(borderSize);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setColor(borderColor);
		borderPaint.setAntiAlias(true);
		
		// create the rect bounds of the original bitmap
		final Rect rect = new Rect(halfBorderSize, halfBorderSize, bitmap.getWidth() - halfBorderSize,
			bitmap.getHeight() - halfBorderSize);
		final RectF rectF = new RectF(rect);
		
		// clear the canvas and draw an oval for where the image should be copied
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawOval(new RectF(rect), bitmapPaint);
		
		// paint the original bitmap onto the canvas
		bitmapPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, bitmapPaint);
		canvas.drawOval(rectF, borderPaint);
		
		// check to see if the original bitmap should be immediately recycled
		if (recycleOriginal)
		{
			bitmap.recycle();
		}
		
		// add this bitmap to the list to keep track of it
		weakBitmapRecycler.add(targetBitmap);
		
		return targetBitmap;
	}
	
	/**
	 * Crops the original bitmap into a round one
	 *
	 * @param bitmap
	 * @param recycleOriginal
	 * @return
	 */
	public Bitmap cropRoundedCornersBitmap(final Bitmap bitmap, final int roundCornerPixels,
		final boolean recycleOriginal)
	{
		// create a target bitmap to draw the circular image to
		Bitmap targetBitmap = Bitmap.createBitmap(bitmap.getWidth(),
			bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		
		// create the canvas that will restrict the image to an ellipse
		Canvas canvas = new Canvas(targetBitmap);
		
		// set up the paint for copying over the bitmap
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		// create the rect bounds of the original bitmap
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, roundCornerPixels, roundCornerPixels, paint);
		
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		
		// check to see if the original bitmap should be immediately recycled
		if (recycleOriginal)
		{
			bitmap.recycle();
		}
		
		// add this bitmap to the list to keep track of it
		weakBitmapRecycler.add(targetBitmap);
		
		return targetBitmap;
	}
	
	/**
	 * Destroys any references to a bitmap that was created and never destroyed
	 */
	public void destroy()
	{
		// clean up the unrecycled bitmaps
		int leakedBitmaps = weakBitmapRecycler.cleanUp();
		weakBitmapRecycler.clear();
		
		if (leakedBitmaps > 0)
		{
			Log.i(getClass().getSimpleName(), "Cleaned up " + leakedBitmaps + " potentially leaked bitmap(s).");
		}
	}
}
