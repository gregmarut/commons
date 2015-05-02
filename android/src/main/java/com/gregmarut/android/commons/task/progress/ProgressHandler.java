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

/**
 * Allows for handling progress updates. These methods are called on the UI thread and therefore are safe to interact
 * with UI elements
 * 
 * @author Greg
 */
public interface ProgressHandler
{
	void begin(String message);
	
	void update(String message, TaskProgress taskProgress);
	
	void end();
	
	void destroy();
}
