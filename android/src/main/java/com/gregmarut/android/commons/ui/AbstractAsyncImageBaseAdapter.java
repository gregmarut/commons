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

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gregmarut.android.commons.task.TaskCallBackListener;
import com.gregmarut.android.commons.task.image.LoadBitmapImageTask;

public abstract class AbstractAsyncImageBaseAdapter<ID, Entity> extends BaseAdapter
{
	// holds the set of image ids that are already being retrieved so that the same image is not
	// retrieved twice
	private Set<ID> loadingIDs;
	
	// holds the map of views to image ids so that when a picture is finished downloading, it only
	// replaces the image on the view if it was the most recent one to access that view
	private Map<View, ID> viewImageMap;
	
	public AbstractAsyncImageBaseAdapter()
	{
		// assign the set of ids
		loadingIDs = new HashSet<ID>();
		
		// assign the map of views
		viewImageMap = new WeakHashMap<View, ID>();
	}
	
	/**
	 * Loads the image for this AsyncDrawableContainer and assigns it to the image view. If the
	 * image does not exist, it is attempted to be loaded asynchronously
	 * 
	 * @param dataContainer
	 * @param imageView
	 * @param loadingView
	 */
	protected void loadImage(final AsyncDrawableContainer<ID, Entity> dataContainer, final ImageView imageView,
		final LinearLayout imageWrapper, final ProgressBar loadingView)
	{
		// make sure the image is not null
		if (null != dataContainer.getDrawable())
		{
			// check to see if the image view is visible
			if (imageWrapper.getVisibility() != View.VISIBLE)
			{
				// swap out the loading view with the image view
				imageWrapper.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
			}
			
			// update the image view
			imageView.setImageDrawable(dataContainer.getDrawable());
		}
		else
		{
			// asynchronously load the images for this cell
			loadImageAsync(dataContainer, imageView, imageWrapper, loadingView);
		}
	}
	
	/**
	 * Asynchronously loads the images for the image sets
	 * 
	 * @param ratedImageSet
	 * @param imageView
	 */
	private void loadImageAsync(final AsyncDrawableContainer<ID, Entity> dataContainer, final ImageView imageView,
		final LinearLayout imageWrapper, final ProgressBar loadingView)
	{
		// holds the facebook id that will be loading
		final ID id = dataContainer.getID();
		
		// hold onto a weak reference of the view
		final WeakReference<ImageView> imageViewReference = new WeakReference<ImageView>(imageView);
		
		// add this image set id to the set.
		// true means that the id was successfully added to the set
		// false means that the id already existed in the set
		if (loadingIDs.add(id))
		{
			// synchronize on this map so that no other thread can access it until we
			// are done
			synchronized (viewImageMap)
			{
				// put this view in the map with this id
				viewImageMap.put(imageView, id);
			}
			
			// swap out the loading view with the image view
			loadingView.setVisibility(View.VISIBLE);
			imageWrapper.setVisibility(View.GONE);
			
			// create a new task
			LoadBitmapImageTask<Entity> loadBitmapImageTask = createNewLoadBitmapImageTask();
			loadBitmapImageTask.addTaskCallBackListener(new TaskCallBackListener<Bitmap>()
			{
				@Override
				public void onPostExecute(Bitmap result)
				{
					// make sure the result is not null
					if (null != result)
					{
						// create the drawable
						Drawable drawable = new BitmapDrawable(result);
						
						// synchronize on this map so that no other thread can access it until we
						// are done
						synchronized (viewImageMap)
						{
							// retrieve the expected from the table
							ID expectedID = viewImageMap.get(imageViewReference.get());
							
							// make sure the expected id matches this one
							if (null != expectedID && expectedID.equals(id))
							{
								// remove this view from the map
								viewImageMap.remove(imageView);
								
								// check to see if the image view is visible
								if (imageWrapper.getVisibility() != View.VISIBLE)
								{
									// swap out the loading view with the image view
									imageWrapper.setVisibility(View.VISIBLE);
									loadingView.setVisibility(View.GONE);
								}
								
								// update the image view
								imageView.setImageDrawable(drawable);
							}
						}
						
						// save the drawable to the RecentOpponent
						dataContainer.setDrawable(drawable);
					}
					
					// remove this id from the set
					loadingIDs.remove(id);
				}
				
				@Override
				public void onCancelled(Bitmap result)
				{
					
				}
			});
			
			// cast the entity as an array of that type. This is required because at runtime, the
			// parameter is considered to be an object array and therefore not able to be casted as
			// the type required for the argument
			Entity[] params = castEntityAsArray(dataContainer.getEntity());
			loadBitmapImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		}
	}
	
	/**
	 * Casts an entity into an array of only that element. This is required because at runtime, the
	 * parameter is considered to be an object array and therefore not able to be casted as the type
	 * required for the argument
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Entity[] castEntityAsArray(final Entity entity)
	{
		// determine the type of class this is at runtime and create an array for it
		Class<? extends Entity> anyTypeClass = (Class<? extends Entity>) entity.getClass();
		Entity[] newArray = (Entity[]) Array.newInstance(anyTypeClass, 1);
		newArray[0] = entity;
		return newArray;
	}
	
	/**
	 * Creates a new background task to load the image
	 * 
	 * @return
	 */
	protected abstract LoadBitmapImageTask<Entity> createNewLoadBitmapImageTask();
	
	protected class DataContainer
	{
		private final ID id;
		private final Drawable drawable;
		
		public DataContainer(final ID id, final Drawable drawable)
		{
			this.id = id;
			this.drawable = drawable;
		}
		
		public ID getID()
		{
			return id;
		}
		
		public Drawable getDrawable()
		{
			return drawable;
		}
	}
}
