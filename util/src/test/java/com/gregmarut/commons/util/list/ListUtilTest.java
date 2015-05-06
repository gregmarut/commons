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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ListUtilTest
{
	private List<TestObject> list;
	private DefaultIDEquals equals;
	
	@Before
	public void init()
	{
		list = new ArrayList<TestObject>();
		list.add(new TestObject(1, "One"));
		list.add(new TestObject(2, "Two"));
		list.add(new TestObject(3, "Three"));
		list.add(new TestObject(4, "Four"));
		list.add(new TestObject(5, "Five"));
		
		equals = new DefaultIDEquals();
	}
	
	@Test
	public void findTest()
	{
		TestObject target = ListUtil.find(list, new TestObject(4, "Four"), equals);
		
		Assert.assertNotNull(target);
		Assert.assertEquals(4, target.getId());
		Assert.assertEquals("Four", target.getValue());
	}
	
	@Test
	public void replaceTest()
	{
		boolean replaced = ListUtil.replace(list, new TestObject(4, "SomethingElse"), equals);
		
		Assert.assertTrue(replaced);
		
		Assert.assertEquals(5, list.size());
		
		Assert.assertEquals(1, list.get(0).getId());
		Assert.assertEquals("One", list.get(0).getValue());
		Assert.assertEquals(2, list.get(1).getId());
		Assert.assertEquals("Two", list.get(1).getValue());
		Assert.assertEquals(3, list.get(2).getId());
		Assert.assertEquals("Three", list.get(2).getValue());
		Assert.assertEquals(4, list.get(3).getId());
		Assert.assertEquals("SomethingElse", list.get(3).getValue());
		Assert.assertEquals(5, list.get(4).getId());
		Assert.assertEquals("Five", list.get(4).getValue());
	}
	
	@Test
	public void replaceFailTest()
	{
		boolean replaced = ListUtil.replace(list, new TestObject(6, "Six"), equals);
		
		Assert.assertFalse(replaced);
		
		Assert.assertEquals(5, list.size());
		
		Assert.assertEquals(1, list.get(0).getId());
		Assert.assertEquals("One", list.get(0).getValue());
		Assert.assertEquals(2, list.get(1).getId());
		Assert.assertEquals("Two", list.get(1).getValue());
		Assert.assertEquals(3, list.get(2).getId());
		Assert.assertEquals("Three", list.get(2).getValue());
		Assert.assertEquals(4, list.get(3).getId());
		Assert.assertEquals("Four", list.get(3).getValue());
		Assert.assertEquals(5, list.get(4).getId());
		Assert.assertEquals("Five", list.get(4).getValue());
	}
	
	@Test
	public void replaceOrAddTest()
	{
		ListUtil.replaceOrAdd(list, new TestObject(6, "Six"), equals);
		
		Assert.assertEquals(6, list.size());
		
		Assert.assertEquals(1, list.get(0).getId());
		Assert.assertEquals("One", list.get(0).getValue());
		Assert.assertEquals(2, list.get(1).getId());
		Assert.assertEquals("Two", list.get(1).getValue());
		Assert.assertEquals(3, list.get(2).getId());
		Assert.assertEquals("Three", list.get(2).getValue());
		Assert.assertEquals(4, list.get(3).getId());
		Assert.assertEquals("Four", list.get(3).getValue());
		Assert.assertEquals(5, list.get(4).getId());
		Assert.assertEquals("Five", list.get(4).getValue());
		Assert.assertEquals(6, list.get(5).getId());
		Assert.assertEquals("Six", list.get(5).getValue());
	}
	
	@Test
	public void convertToStringTest()
	{
		List<String> strings = ListUtil.convert(list, new DefaultConvertString());
		
		Assert.assertEquals("One", strings.get(0));
		Assert.assertEquals("Two", strings.get(1));
		Assert.assertEquals("Three", strings.get(2));
		Assert.assertEquals("Four", strings.get(3));
		Assert.assertEquals("Five", strings.get(4));
	}
	
	@Test
	public void convertToMapTest()
	{
		// convert this list to a map
		Map<Integer, TestObject> map = ListUtil.toMap(list, new Convert<TestObject, Integer>()
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
