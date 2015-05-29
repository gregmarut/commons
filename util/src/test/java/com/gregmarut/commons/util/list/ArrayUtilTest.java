/*******************************************************************************
 * <pre>
 * Copyright (c) 2015 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Greg Marut - initial API and implementation
 * </pre>
 ******************************************************************************/
package com.gregmarut.commons.util.list;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArrayUtilTest
{
	private TestObject[] array;
	private DefaultIDEquals equals;
	
	@Before
	public void init()
	{
		array = new TestObject[5];
		array[0] = new TestObject(1, "One");
		array[1] = new TestObject(2, "Two");
		array[2] = new TestObject(3, "Three");
		array[3] = new TestObject(4, "Four");
		array[4] = new TestObject(5, "Five");
		
		equals = new DefaultIDEquals();
	}
	
	@Test
	public void findTest()
	{
		TestObject target = ArrayUtil.find(array, new TestObject(4, "Four"), equals);
		
		Assert.assertNotNull(target);
		Assert.assertEquals(4, target.getId());
		Assert.assertEquals("Four", target.getValue());
	}
	
	@Test
	public void replaceTest()
	{
		boolean replaced = ArrayUtil.replace(array, new TestObject(4, "SomethingElse"), equals);
		
		Assert.assertTrue(replaced);
		
		Assert.assertEquals(5, array.length);
		
		Assert.assertEquals(1, array[0].getId());
		Assert.assertEquals("One", array[0].getValue());
		Assert.assertEquals(2, array[1].getId());
		Assert.assertEquals("Two", array[1].getValue());
		Assert.assertEquals(3, array[2].getId());
		Assert.assertEquals("Three", array[2].getValue());
		Assert.assertEquals(4, array[3].getId());
		Assert.assertEquals("SomethingElse", array[3].getValue());
		Assert.assertEquals(5, array[4].getId());
		Assert.assertEquals("Five", array[4].getValue());
	}
	
	@Test
	public void replaceFailTest()
	{
		boolean replaced = ArrayUtil.replace(array, new TestObject(6, "Six"), equals);
		
		Assert.assertFalse(replaced);
		
		Assert.assertEquals(5, array.length);
		
		Assert.assertEquals(1, array[0].getId());
		Assert.assertEquals("One", array[0].getValue());
		Assert.assertEquals(2, array[1].getId());
		Assert.assertEquals("Two", array[1].getValue());
		Assert.assertEquals(3, array[2].getId());
		Assert.assertEquals("Three", array[2].getValue());
		Assert.assertEquals(4, array[3].getId());
		Assert.assertEquals("Four", array[3].getValue());
		Assert.assertEquals(5, array[4].getId());
		Assert.assertEquals("Five", array[4].getValue());
	}
	
	@Test
	public void convertToStringTest()
	{
		String[] strings = ArrayUtil.convert(array, String.class, new DefaultConvertString());
		
		Assert.assertEquals("One", strings[0]);
		Assert.assertEquals("Two", strings[1]);
		Assert.assertEquals("Three", strings[2]);
		Assert.assertEquals("Four", strings[3]);
		Assert.assertEquals("Five", strings[4]);
	}
	
	@Test
	public void convertToMapTest()
	{
		// convert this list to a map
		Map<Integer, TestObject> map = ArrayUtil.toMap(array, new Convert<TestObject, Integer>()
		{
			@Override
			public Integer convert(TestObject source)
			{
				return source.getId();
			}
		});
		
		Assert.assertEquals("One", map.get(1).getValue());
		Assert.assertEquals("Two", map.get(2).getValue());
		Assert.assertEquals("Three", map.get(3).getValue());
		Assert.assertEquals("Four", map.get(4).getValue());
		Assert.assertEquals("Five", map.get(5).getValue());
	}
	
	private class DefaultIDEquals implements Equals<TestObject>
	{
		@Override
		public boolean equals(TestObject obj1, TestObject obj2)
		{
			return obj1.getId() == obj2.getId();
		}
	}
	
	private class DefaultConvertString implements Convert<TestObject, String>
	{
		@Override
		public String convert(TestObject source)
		{
			return source.getValue();
		}
	}
	
	private class TestObject
	{
		private final int id;
		private final String value;
		
		public TestObject(final int id, final String value)
		{
			this.id = id;
			this.value = value;
		}
		
		public int getId()
		{
			return id;
		}
		
		public String getValue()
		{
			return value;
		}
	}
}
