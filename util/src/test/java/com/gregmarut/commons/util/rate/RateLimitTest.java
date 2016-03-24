package com.gregmarut.commons.util.rate;

import org.junit.Test;

public class RateLimitTest
{
	@Test
	public void evenSpaceRateLimitTest()
	{
		RateLimiter rateLimiter = new EvenlySpacedRateLimiter(RateLimitUnit.SECOND, 10);
		
		long start = System.currentTimeMillis();
		long last = 0;
		
		for (int i = 0; i < 10; i++)
		{
			rateLimiter.acquire();
			
			last = System.currentTimeMillis();
			long time = last - start;
			System.out.println(time);
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidInput()
	{
		new EvenlySpacedRateLimiter(RateLimitUnit.SECOND, 1001);
	}
	
	@Test
	public void validnput()
	{
		new EvenlySpacedRateLimiter(RateLimitUnit.SECOND, 100);
	}
}
