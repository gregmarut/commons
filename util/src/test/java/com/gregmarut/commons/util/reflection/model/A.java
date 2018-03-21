package com.gregmarut.commons.util.reflection.model;

public class A
{
	private final B b;
	
	public A()
	{
		b = new B();
	}
	
	public B getB()
	{
		return b;
	}
}
