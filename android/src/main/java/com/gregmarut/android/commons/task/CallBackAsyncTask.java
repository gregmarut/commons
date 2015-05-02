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

/**
 * Allows listeners to be attached to tasks to be notified when the task finishes executing
 * 
 * @author Greg Marut
 * @param <Params>
 * @param <Result>
 */
public abstract class CallBackAsyncTask<Params, Result> extends BaseAsyncTask<Params, Result>
{
	// holds the list of task callback listeners
	protected final Set<TaskCallBackListener<Result>> taskCallBackListeners;
	
	public CallBackAsyncTask()
	{
		taskCallBackListeners = new HashSet<TaskCallBackListener<Result>>();
	}
	
	public void addTaskCallBackListener(final TaskCallBackListener<Result> listener)
	{
		taskCallBackListeners.add(listener);
	}
	
	public boolean removeTaskCallBackListener(final TaskCallBackListener<Result> listener)
	{
		return taskCallBackListeners.remove(listener);
	}
	
	@Override
	protected void onCancelled(Result result)
	{
		super.onCancelled(result);
		
		// for each of the task call back listeners
		for (TaskCallBackListener<Result> listener : taskCallBackListeners)
		{
			// notify the listener
			listener.onCancelled(result);
		}
	}
	
	@Override
	protected void onPostExecute(Result result)
	{
		super.onPostExecute(result);
		
		// for each of the task call back listeners
		for (TaskCallBackListener<Result> listener : taskCallBackListeners)
		{
			// notify the listener
			listener.onPostExecute(result);
		}
	}
}
