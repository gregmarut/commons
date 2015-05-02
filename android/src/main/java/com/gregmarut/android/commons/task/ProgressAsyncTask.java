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
package com.gregmarut.android.commons.task;

import java.util.HashSet;
import java.util.Set;

import com.gregmarut.android.commons.task.progress.ProgressHandler;
import com.gregmarut.android.commons.task.progress.TaskProgress;

public abstract class ProgressAsyncTask<Params, Result> extends CallBackAsyncTask<Params, Result>
{
	public static final String DEFAULT_MESSAGE = "Please wait";
	
	// holds progress handler
	private final Set<ProgressHandler> progressHandlers;
	
	// holds the default message
	private String defaultMessage;
	
	// determines if the default message is always displayed
	private boolean alwaysUseDefaultMessage;
	
	// determines if ellipses should automatically be appended to each message
	private boolean appendEllipsis;
	
	public ProgressAsyncTask()
	{
		progressHandlers = new HashSet<ProgressHandler>();
		
		setDefaultMessage(DEFAULT_MESSAGE);
		setAlwaysUseDefaultMessage(false);
		setAppendEllipsis(true);
	}
	
	@Override
	protected void onPreExecute()
	{
		// for each of the progress handlers
		for (ProgressHandler progressHandler : progressHandlers)
		{
			// check to see if ellipses should be added
			if (appendEllipsis)
			{
				// notify the progress handler to begin
				progressHandler.begin(defaultMessage + "...");
			}
			else
			{
				// notify the progress handler to begin
				progressHandler.begin(defaultMessage);
			}
		}
		
		super.onPreExecute();
	}
	
	@Override
	protected void onProgressUpdate(final TaskProgress... values)
	{
		super.onProgressUpdate(values);
		
		// for each of the progress handlers
		for (ProgressHandler progressHandler : progressHandlers)
		{
			TaskProgress taskProgress = values[0];
			
			StringBuilder sb = new StringBuilder();
			
			// check to see if the default message should be used
			if (alwaysUseDefaultMessage || null == taskProgress.getMessage())
			{
				// display the default message
				sb.append(defaultMessage);
			}
			else
			{
				// display the message
				sb.append(taskProgress.getMessage());
			}
			
			// check to see if ellipses should be added
			if (appendEllipsis)
			{
				sb.append("...");
			}
			
			progressHandler.update(sb.toString(), taskProgress);
		}
	}
	
	@Override
	protected void onPostExecute(final Result result)
	{
		super.onPostExecute(result);
		
		// for each of the progress handlers
		for (ProgressHandler progressHandler : progressHandlers)
		{
			// end the progress handler
			progressHandler.end();
		}
	}
	
	@Override
	protected void onCancelled()
	{
		super.onCancelled();
		
		// for each of the progress handlers
		for (ProgressHandler progressHandler : progressHandlers)
		{
			// end the progress handler
			progressHandler.end();
		}
	}
	
	public void addProgressHandler(final ProgressHandler progressHandler)
	{
		progressHandlers.add(progressHandler);
	}
	
	public boolean removeProgressHandler(final ProgressHandler progressHandler)
	{
		return progressHandlers.remove(progressHandler);
	}
	
	public String getDefaultMessage()
	{
		return defaultMessage;
	}
	
	public void setDefaultMessage(String defaultMessage)
	{
		this.defaultMessage = defaultMessage;
	}
	
	public boolean isAppendEllipsis()
	{
		return appendEllipsis;
	}
	
	public void setAppendEllipsis(boolean appendEllipsis)
	{
		this.appendEllipsis = appendEllipsis;
	}
	
	public boolean isAlwaysUseDefaultMessage()
	{
		return alwaysUseDefaultMessage;
	}
	
	public void setAlwaysUseDefaultMessage(boolean alwaysUseDefaultMessage)
	{
		this.alwaysUseDefaultMessage = alwaysUseDefaultMessage;
	}
}
