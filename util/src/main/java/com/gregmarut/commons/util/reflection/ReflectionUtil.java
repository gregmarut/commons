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
package com.gregmarut.commons.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil
{
	private ReflectionUtil()
	{
		
	}
	
	public static Field findField(final Object target, final String fieldName) throws NoSuchFieldException
	{
		// holds the class to inspect
		Class<?> clazz = target.getClass();
		
		// while the class is not null
		while (null != clazz)
		{
			try
			{
				//attempt to retrieve the declared field for this class
				return clazz.getDeclaredField(fieldName);
			}
			catch (NoSuchFieldException e)
			{
				// read this class' superclass next if one exists
				clazz = clazz.getSuperclass();
			}
		}
		
		throw new NoSuchFieldException("Unable to find " + fieldName + " on class " + target.getClass() + " or any of it's parent classes");
	}
	
	/**
	 * Retrieve all of the fields for a given object including any parent classes
	 *
	 * @param object
	 * @return
	 */
	public static Field[] getAllFields(final Object object)
	{
		// holds the list of fields
		List<Field> allFields = new ArrayList<Field>();
		
		// holds the class to inspect
		Class<?> clazz = object.getClass();
		
		// while the class is not null
		while (null != clazz)
		{
			// retrieve all of the declared field for this class and add them to the list
			Field[] fields = clazz.getDeclaredFields();
			allFields.addAll(Arrays.asList(fields));
			
			// read this class' superclass next if one exists
			clazz = clazz.getSuperclass();
		}
		
		return allFields.toArray(new Field[allFields.size()]);
	}
	
	/**
	 * Retrieve a method on the given class including any parent class
	 *
	 * @param object
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static Method getDeclaredMethod(final Object object, final String name, final Class<?>... parameterTypes)
		throws NoSuchMethodException
	{
		// holds the class to inspect
		Class<?> clazz = object.getClass();
		
		// while the class is not null
		while (null != clazz)
		{
			// retrieve the method from this class
			try
			{
				return clazz.getDeclaredMethod(name, parameterTypes);
			}
			catch (NoSuchMethodException e)
			{
				// ignore
			}
			catch (SecurityException e)
			{
				// ignore
			}
			
			// read this class' superclass next if one exists
			clazz = clazz.getSuperclass();
		}
		
		// build the error message
		StringBuilder sb = new StringBuilder();
		sb.append(object.getClass().getName());
		sb.append(".");
		sb.append(name);
		sb.append("()");
		
		throw new NoSuchMethodException(sb.toString());
	}
	
	/**
	 * Extracts the list of generic classes from a field if they are defined
	 *
	 * @param field
	 * @return
	 */
	public static List<Class<?>> extractGenericClasses(final Field field)
	{
		// hold the list of generic classes for the object defined in this field
		List<Class<?>> generics = new ArrayList<Class<?>>();
		
		// get the generic type for this collection
		Type genericType = field.getGenericType();
		
		// make sure there is a generic type assigned for this collection
		if (genericType instanceof ParameterizedType)
		{
			// attempt to extract the generic from this collection
			ParameterizedType parameterizedTypes = (ParameterizedType) genericType;
			
			// for each of the actual type arguments
			for (Type type : parameterizedTypes.getActualTypeArguments())
			{
				final Class<?> clazz = (Class<?>) type;
				generics.add(clazz);
			}
		}
		
		return generics;
	}
}
