package com.gregmarut.commons.util.rate;

public class EvenlySpacedRateLimiter extends RateLimiter
{
	// holds the array of timestamps for the last execution
	private long lastExecution;
	
	/**
	 * @param unit
	 * @param limitPerUnit
	 */
	public EvenlySpacedRateLimiter(final RateLimitUnit unit, final int limitPerUnit)
	{
		super(calculateUnitMilliseconds(unit, limitPerUnit), 1);
	}
	
	/**
	 * Calculates the remaining time until the next execution is allowed.
	 * 
	 * @return
	 */
	protected long calculateMillisecondsRemaining()
	{
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
	protected void updateNextExecution(long currentExecution)
	{
		// update the current execution and increment the index
		lastExecution = currentExecution;
	}
	
	private static long calculateUnitMilliseconds(final RateLimitUnit unit, final int limitPerUnit)
	{
		double unitMilliseconds = unit.getMillisecondsPerUnit() / (double) limitPerUnit;
		
		// make sure the unit milliseconds is not less than 1
		if (unitMilliseconds < 1f)
		{
			throw new IllegalArgumentException("calculated rate limit cannot be less than 1 per millisecond");
		}
		
		return (long) unitMilliseconds;
	}
}
