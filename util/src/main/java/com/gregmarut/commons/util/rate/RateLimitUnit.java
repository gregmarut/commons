package com.gregmarut.commons.util.rate;

public enum RateLimitUnit
{
	HOUR(3600000L),
	MINUTE(60000L),
	SECOND(1000L),
	MILLISECOND(1L);
	
	// holds the number of milliseconds that there are in each unit
	private final long millisecondsPerUnit;
	
	private RateLimitUnit(final long millisecondsPerUnit)
	{
		this.millisecondsPerUnit = millisecondsPerUnit;
	}
	
	public long getMillisecondsPerUnit()
	{
		return millisecondsPerUnit;
	}
}
