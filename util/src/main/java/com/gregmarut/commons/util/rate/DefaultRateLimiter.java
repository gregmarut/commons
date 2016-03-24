package com.gregmarut.commons.util.rate;

public class DefaultRateLimiter extends RateLimiter
{
	// holds the array of timestamps for the last execution
	private final long[] executions;
	private int executionIndex;
	
	/**
	 * @param unit
	 * @param limitPerUnit
	 */
	public DefaultRateLimiter(final RateLimitUnit unit, final int limitPerUnit)
	{
		super(unit.getMillisecondsPerUnit(), limitPerUnit);
		
		this.executions = new long[limitPerUnit];
	}
	
	/**
	 * Calculates the remaining time until the next execution is allowed.
	 * 
	 * @return
	 */
	protected long calculateMillisecondsRemaining()
	{
		long lastExecution = executions[executionIndex];
		long nextExecution = lastExecution + unitMilliseconds;
		long now = System.currentTimeMillis();
		
		// check to see if the next execution is less than now
		if (nextExecution < now)
		{
			// there is no time left
			return 0L;
		}
		else
		{
			// calculate the number of milliseconds left until the next executions is allowed
			return nextExecution - now;
		}
	}
	
	@Override
	protected void updateNextExecution(final long currentExecution)
	{
		// update the current execution and increment the index
		executions[executionIndex] = currentExecution;
		
		// increment the executionIndex but do not allow it to go over the length of the array
		if (executionIndex >= executions.length - 1)
		{
			executionIndex = 0;
		}
		else
		{
			// increment the execution index
			executionIndex++;
		}
	}
}
