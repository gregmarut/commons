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
package com.gregmarut.android.commons.task.progress;

public class TaskProgress
{
	private final int completed;
	private final int total;
	
	private final String message;
	
	public TaskProgress(final int completed, final int total)
	{
		this(completed, total, null);
	}
	
	public TaskProgress(final int completed, final int total, final String message)
	{
		this.completed = completed;
		this.total = total;
		this.message = message;
	}
	
	public int getCompleted()
	{
		return completed;
	}
	
	public int getTotal()
	{
		return total;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public float getPercentComplete()
	{
		// make sure the total is greater than 0 to prevent a divide by 0
		if (total > 0)
		{
			return completed / (float) total;
		}
		else
		{
			return 0f;
		}
	}
}
