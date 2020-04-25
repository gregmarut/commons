package com.gregmarut.android.commons.task.chain;

import android.os.AsyncTask;
import android.util.Log;
import com.gregmarut.android.commons.task.CallBackAsyncTask;
import com.gregmarut.android.commons.task.TaskCallBackListener;
import com.gregmarut.android.commons.task.progress.ProgressHandler;
import com.gregmarut.android.commons.task.progress.TaskProgress;
import com.gregmarut.android.commons.weak.WeakTaskCanceler;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Holds the task chain that will execute tasks in order and report when all have successfully finished
 *
 * @author Greg
 */
public class TaskChain implements TaskCallBackListener<Object>
{
	// holds the queue of tasks that will be
	private final Queue<CallBackAsyncTask<Void, ?>> taskQueue;
	
	// holds the task canceler that will cancel any running tasks
	private final WeakTaskCanceler weakTaskCanceler;
	
	private final TaskChainListener taskChainListener;
	
	// determines if this task chain is paused or canceled
	private volatile boolean paused;
	private volatile boolean cancelled;
	
	private ProgressHandler progressHandler;
	private int totalTasks;
	
	public TaskChain(final TaskChainListener taskChainListener)
	{
		// make sure that the parameter is not null
		if (null == taskChainListener)
		{
			throw new IllegalArgumentException("TaskChainListener cannot be null");
		}
		
		// assign the queue of tasks
		taskQueue = new LinkedList<CallBackAsyncTask<Void, ?>>();
		
		// assign the weak task canceler
		weakTaskCanceler = new WeakTaskCanceler();
		
		this.taskChainListener = taskChainListener;
	}
	
	/**
	 * Adds a task to the task chain
	 *
	 * @param task
	 */
	@SuppressWarnings(
		{
			"rawtypes", "unchecked"
		})
	public void addTask(CallBackAsyncTask<Void, ?> task)
	{
		task.addTaskCallBackListener((TaskCallBackListener) this);
		taskQueue.add(task);
	}
	
	@Override
	public void onCancelled(Object result)
	{
		// cancel any remaining tasks
		cancel();
	}
	
	@Override
	public void onPostExecute(Object result)
	{
		// execute the next task
		executeNextTask();
	}
	
	public void start()
	{
		totalTasks = taskQueue.size();
		
		if (null != progressHandler)
		{
			progressHandler.begin(null);
		}
		
		executeNextTask();
	}
	
	/**
	 * Executes the next task in the queue asynchronously
	 */
	private void executeNextTask()
	{
		// make sure that the tasks are to be canceled
		if (!cancelled)
		{
			// make sure the chain is not paused
			if (!paused)
			{
				if (null != progressHandler)
				{
					int currentTask = totalTasks - taskQueue.size() + 1;
					TaskProgress taskProgress = new TaskProgress(currentTask, totalTasks);
					progressHandler.update(null, taskProgress);
				}
				
				// pull the next task from the chain
				CallBackAsyncTask<Void, ?> task = taskQueue.poll();
				
				// make sure that the next task is not null
				if (null != task)
				{
					// add this task to the canceler
					weakTaskCanceler.add(task);
					
					Log.d(getClass().getSimpleName(), "Executing Task: " + task.getClass().getSimpleName());
					
					// execute the task
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else
				{
					// there are no more tasks to complete
					taskChainListener.taskChainCompleted();
					
					if (null != progressHandler)
					{
						progressHandler.end();
						progressHandler.destroy();
					}
				}
			}
		}
		else
		{
			cancel();
		}
	}
	
	/**
	 * Pauses the task chain until executeNextTask is called again
	 */
	public void pause()
	{
		paused = true;
	}
	
	/**
	 * Resumes the task chain if it is paused
	 */
	public void resume()
	{
		// make sure that the task chain is paused
		if (paused)
		{
			paused = false;
			executeNextTask();
		}
	}
	
	/**
	 * Completely cancel this task chain
	 */
	public void cancel()
	{
		// make sure that the chain is not already canceled
		if (!cancelled)
		{
			cancelled = true;
			
			Log.d(getClass().getSimpleName(), "Canceling Task Chain");
			
			weakTaskCanceler.cleanUp();
			taskQueue.clear();
			taskChainListener.taskChainCanceled();
			
			if (null != progressHandler)
			{
				progressHandler.end();
				progressHandler.destroy();
			}
		}
	}
	
	public ProgressHandler getProgressHandler()
	{
		return progressHandler;
	}
	
	public void setProgressHandler(final ProgressHandler progressHandler)
	{
		this.progressHandler = progressHandler;
	}
}
