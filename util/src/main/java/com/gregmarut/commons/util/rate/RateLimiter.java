package com.gregmarut.commons.util.rate;

public abstract class RateLimiter
{
	// holds the limit unit
	protected final long unitMilliseconds;
	
	// holds the number of executions allow for the given unit
	protected final int limitPerUnit;
	
	/**
	 * @param unitMilliseconds
	 * @param limitPerUnit
	 */
	public RateLimiter(final long unitMilliseconds, final int limitPerUnit)
	{
		// make sure the millisecond units is greater than 0
		if (unitMilliseconds <= 0)
		{
			throw new IllegalArgumentException("unitMilliseconds must be greater than 0");
		}
		
		// make sure the limit per unit is greater than 0
		if (limitPerUnit <= 0)
		{
			throw new IllegalArgumentException("limitPerUnit must be greater than 0");
		}
		
		this.unitMilliseconds = unitMilliseconds;
		this.limitPerUnit = limitPerUnit;
	}
	
	/**
	 * Waits for and acquires a lock for the next execution
	 */
	public synchronized void acquire()
	{
		try
		{
			// calculate the number of milliseconds remaining
			long millisecondsRemaining = calculateMillisecondsRemaining();
			
			// check to see if there is time remaining
			if (millisecondsRemaining > 0)
			{
				try
				{
					// wait for the remaining seconds
					Thread.sleep(millisecondsRemaining);
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		finally
		{
			synchronized (this)
			{
				// update the next execution
				updateNextExecution(System.currentTimeMillis());
			}
		}
	}
	
	/**
	 * Immediately attempts to acquire a lock for the next execution. If one could not be obtained,
	 * an exception will be thrown instead of waiting
	 * 
	 * @throws RateLimitException
	 */
	public synchronized void acquireOrFail() throws RateLimitException
	{
		// calculate the number of milliseconds remaining
		long millisecondsRemaining = calculateMillisecondsRemaining();
		
		// check to see if there is time remaining
		if (millisecondsRemaining > 0)
		{
			throw new RateLimitException(millisecondsRemaining);
		}
		else
		{
			synchronized (this)
			{
				// update the next execution
				updateNextExecution(System.currentTimeMillis());
			}
		}
	}
	
	/**
	 * Calculates the remaining time until the next execution is allowed.
	 * 
	 * @return
	 */
	protected abstract long calculateMillisecondsRemaining();
	
	protected abstract void updateNextExecution(final long currentExecution);
}
