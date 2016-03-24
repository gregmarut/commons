package com.gregmarut.commons.util.rate;

public class RateLimitException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public RateLimitException(final long millisecondsRemaining)
	{
		super(millisecondsRemaining + " milliseconds remaining until the next available execution.");
	}
}
