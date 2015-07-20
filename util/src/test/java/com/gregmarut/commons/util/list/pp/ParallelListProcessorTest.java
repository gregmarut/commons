package com.gregmarut.commons.util.list.pp;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class ParallelListProcessorTest
{
	@Test
	public void testList()
	{
		// create a list of 100 numbers and populate it
		List<Integer> numbers = new ArrayList<Integer>();
		
		for (int i = 0; i < 100; i++)
		{
			numbers.add(i);
		}
		
		// create a new parallel processing object
		ParallelListProcessor<Integer, Integer> processor = new ParallelListProcessor<Integer, Integer>(10);
		
		// process the list of numbers and define a task for how to processes each individual number
		List<Integer> results = processor.process(numbers, new Task<Integer, Integer>()
		{
			@Override
			public Integer execute(Integer p)
			{
				return p + 1000;
			}
		});
		
		// for each of the numbers
		for (int i = 0; i < numbers.size(); i++)
		{
			int number = numbers.get(i);
			int result = results.get(i);
			
			// verify that the results of the list confirm what actually happened as defined by the processing task
			Assert.assertEquals(number + 1000, result);
		}
	}
}
