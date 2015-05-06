package com.gregmarut.commons.util;

import junit.framework.Assert;

import org.junit.Test;

public class ByteArrayTest
{
	private static final String HELLO = "Hello";
	private static final String WORLD = "World";
	
	@Test
	public void stringTest()
	{
		ByteArray byteArray = new ByteArray();
		byteArray.append(HELLO.getBytes());
		byteArray.append(WORLD.getBytes());
		
		Assert.assertEquals(HELLO + WORLD, byteArray.toString());
	}
	
	@Test
	public void hexTest()
	{
		ByteArray byteArray = new ByteArray();
		byteArray.append((byte) 255);
		byteArray.append((byte) 254);
		byteArray.append((byte) 253);
		
		Assert.assertEquals("FFFEFD", byteArray.toHexString());
	}
}
