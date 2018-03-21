package com.gregmarut.commons.util.reflection;

import com.gregmarut.commons.util.reflection.model.A;
import com.gregmarut.commons.util.reflection.model.B;
import com.gregmarut.commons.util.reflection.model.C;
import com.gregmarut.commons.util.reflection.model.D;
import com.gregmarut.commons.util.reflection.model.E;
import org.junit.Assert;
import org.junit.Test;

public class FieldReflectionUtilTest
{
	@Test
	public void testFieldExtraction1() throws NoSuchFieldException, IllegalAccessException
	{
		A a = new A();
		
		Assert.assertEquals(B.class, FieldReflectionUtil.extract(a, "b").getClass());
	}
	
	@Test
	public void testFieldExtraction2() throws NoSuchFieldException, IllegalAccessException
	{
		A a = new A();
		
		Assert.assertEquals(C.class, FieldReflectionUtil.extract(a, "b", "c").getClass());
	}
	
	@Test
	public void testFieldExtraction3() throws NoSuchFieldException, IllegalAccessException
	{
		A a = new A();
		
		Assert.assertEquals(D.class, FieldReflectionUtil.extract(a, "b", "c", "d").getClass());
	}
	
	@Test
	public void testFieldExtraction4() throws NoSuchFieldException, IllegalAccessException
	{
		A a = new A();
		
		Assert.assertEquals(E.class, FieldReflectionUtil.extract(a, "b", "c", "d", "e").getClass());
	}
	
	@Test
	public void testFieldExtraction5() throws NoSuchFieldException, IllegalAccessException
	{
		A a = new A();
		
		Assert.assertEquals("Test", FieldReflectionUtil.extract(a, "b", "c", "d", "e", "value"));
	}
}
