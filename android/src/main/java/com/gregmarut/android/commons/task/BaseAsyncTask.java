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

import android.os.AsyncTask;

import com.gregmarut.android.commons.task.progress.TaskProgress;

public abstract class BaseAsyncTask<Params, Result> extends AsyncTask<Params, TaskProgress, Result>
{
	// holds the total number of steps
	private int totalSteps;
	
	private int currentStep;
	
	protected final void incrementStep()
	{
		if (currentStep < totalSteps)
		{
			currentStep++;
		}
	}
	
	protected final void incrementStep(final int count)
	{
		for (int i = 0; i < count; i++)
		{
			incrementStep();
		}
	}
	
	protected final int getCurrentStep()
	{
		return currentStep;
	}
	
	protected void setTotalSteps(final int totalSteps)
	{
		this.totalSteps = totalSteps;
	}
	
	protected final void publishProgress(final String message, boolean incrementStep)
	{
		// check to see if a step should automatically be incremented first
		if (incrementStep)
		{
			// increment a step
			incrementStep();
		}
		
		// create a new task progress object
		TaskProgress taskProgress = new TaskProgress(currentStep, totalSteps, message);
		
		// publish the progress
		super.publishProgress(taskProgress);
	}
	
	protected final void publishProgress(final String message)
	{
		publishProgress(message, true);
	}
}
