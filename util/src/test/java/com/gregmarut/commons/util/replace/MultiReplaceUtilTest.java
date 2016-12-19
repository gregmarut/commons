package com.gregmarut.commons.util.replace;

import com.gregmarut.commons.util.KeyValue;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MultiReplaceUtilTest
{
	private static final String KEY_CAR = "{car}";
	private static final String KEY_COLOR = "{color}";
	
	@Test
	public void replaceSingleItems()
	{
		final String input = "{car}";
		
		final List<String> carValues = Arrays.asList("BMW", "Jeep", "Toyota");
		final KeyValue<String, List<String>> cars = new KeyValue<String, List<String>>(KEY_CAR, carValues);
		
		List<String> results = MultiReplaceUtil.replaceAll(input, cars);
		
		Assert.assertEquals(3, results.size());
		Assert.assertEquals("BMW", results.get(0));
		Assert.assertEquals("Jeep", results.get(1));
		Assert.assertEquals("Toyota", results.get(2));
	}
	
	@Test
	public void replaceMultipleItems()
	{
		final String input = "{color} {car}";
		
		final List<String> carValues = Arrays.asList("BMW", "Jeep", "Toyota");
		final List<String> colorValues = Arrays.asList("Red", "Green", "Blue");
		final KeyValue<String, List<String>> cars = new KeyValue<String, List<String>>(KEY_CAR, carValues);
		final KeyValue<String, List<String>> colors = new KeyValue<String, List<String>>(KEY_COLOR, colorValues);
		
		List<String> results = MultiReplaceUtil.replaceAll(input, cars, colors);
		
		Assert.assertEquals(9, results.size());
		Assert.assertEquals("Red BMW", results.get(0));
		Assert.assertEquals("Green BMW", results.get(1));
		Assert.assertEquals("Blue BMW", results.get(2));
		Assert.assertEquals("Red Jeep", results.get(3));
		Assert.assertEquals("Green Jeep", results.get(4));
		Assert.assertEquals("Blue Jeep", results.get(5));
		Assert.assertEquals("Red Toyota", results.get(6));
		Assert.assertEquals("Green Toyota", results.get(7));
		Assert.assertEquals("Blue Toyota", results.get(8));
	}
}
