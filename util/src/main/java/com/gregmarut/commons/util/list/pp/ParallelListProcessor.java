package com.gregmarut.commons.util.list.pp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allows for processing a list in parallel. This allows multiple threads to work on a single list at once instead of
 * linearly
 * 
 * @author Gregory Marut
 * @param <P>
 * @param <V>
 */
public class ParallelListProcessor<P, V>
{
	// holds the logger for this class
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	// determines the size of the thread pool
	private final int threadPoolSize;
	
	// holds the list of task callback that will be informed whenever a task is completed
	private final List<TaskCallbackListener> listeners;
	
	/**
	 * @param threadPoolSize
	 *            the number of simultaneous threads that will be allowed to run to process the list
	 */
	public ParallelListProcessor(final int threadPoolSize)
	{
		this.threadPoolSize = threadPoolSize;
		this.listeners = new ArrayList<TaskCallbackListener>();
	}
	
	public void addTaskCallbackListener(final TaskCallbackListener listener)
	{
		listeners.add(listener);
	}
	
	public boolean removeTaskCallbackListener(final TaskCallbackListener listener)
	{
		return listeners.remove(listener);
	}
	
	/**
	 * Processes each record in parallel to each other and returns control to the entering thread once all other
	 * parallel threads have completed
	 * 
	 * @param list
	 *            the list to be processed
	 * @param task
	 *            the definition for what to do with each entry in the list
	 * @return the list of resulting objects that correlate to the order of the items that were passed in
	 */
	public List<V> process(final List<P> list, final Task<P, V> task)
	{
		// create a new executor service with a given number of threads in the thread pool
		ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
		
		// retrieve the total records
		final int total = list.size();
		
		// holds the list of future results
		List<V> results = new ArrayList<V>(total);
		List<Future<V>> futureResults = new ArrayList<Future<V>>(total);
		
		// for each of the task callback listeners
		for (TaskCallbackListener listener : listeners)
		{
			// notify the listener
			listener.preExecute(total);
		}
		
		// holds the atomic integer that will be used to increment for when a task is completed
		final AtomicInteger completed = new AtomicInteger();
		
		// for each item in the list
		for (final P p : list)
		{
			// submit this task to be executed on a thread in the thread pool. If all threads are active, the task will
			// be queued for the next available thread
			Future<V> futureResult = executorService.submit(new Callable<V>()
			{
				@Override
				public V call() throws Exception
				{
					try
					{
						return task.execute(p);
					}
					finally
					{
						// increment the counter
						int value = completed.getAndIncrement();
						
						// for each of the task callback listeners
						for (TaskCallbackListener listener : listeners)
						{
							// notify the listener of the number of tasks that have been processed
							listener.processed(value, total);
						}
					}
				}
			});
			
			// hold onto the future object so that we can retrieve the result from the list once it completes
			futureResults.add(futureResult);
		}
		
		// for each of the future results
		for (int i = 0; i < total; i++)
		{
			Future<V> futureResult = futureResults.get(i);
			
			try
			{
				// retrieve the result of the task call. If the thread is still actively working on this object, this
				// call will block until the result becomes available.
				results.add(futureResult.get());
			}
			catch (InterruptedException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (ExecutionException e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		
		// for each of the task callback listeners
		for (TaskCallbackListener listener : listeners)
		{
			// notify the listener that the tasks have finished
			listener.finished();
		}
		
		// shutdown the executor service and ask all remaining threads to shut down when they have completed
		executorService.shutdown();
		
		return results;
	}
}
