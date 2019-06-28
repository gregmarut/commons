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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogProgressHandler implements ProgressHandler
{
	// holds the progress bar to display on the screen
	private ProgressDialog progress;
	
	public DialogProgressHandler(final Context context)
	{
		progress = new ProgressDialog(context);
		progress.setCancelable(false);
	}
	
	public DialogProgressHandler(final Context context, final DialogInterface.OnCancelListener onCancelListener)
	{
		progress = new ProgressDialog(context);
		progress.setOnCancelListener(onCancelListener);
		progress.setCancelable(true);
		progress.setCanceledOnTouchOutside(false);
	}
	
	@Override
	public void begin(String message)
	{
		// make sure the progress bar has not been destroyed
		if (null != progress)
		{
			progress.setMessage(message);
			progress.show();
		}
	}
	
	@Override
	public void update(String message, TaskProgress taskProgress)
	{
		// make sure the progress bar has not been destroyed
		if (null != progress)
		{
			progress.setMessage(message);
		}
	}
	
	@Override
	public void end()
	{
		// make sure the progress bar has not been destroyed
		if (null != progress && progress.isShowing())
		{
			try
			{
				progress.dismiss();
			}
			catch (Exception e)
			{
				//ignore
			}
		}
	}
	
	@Override
	public void destroy()
	{
		progress = null;
	}
}
