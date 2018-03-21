package com.gregmarut.commons.util.reflection;

import java.lang.reflect.Field;

public class FieldReflectionUtil
{
	public static <T> T extract(final Object target, final String... fieldPaths) throws NoSuchFieldException, IllegalAccessException
	{
		//make sure the field paths are not null or empty
		if (null == fieldPaths || fieldPaths.length == 0)
		{
			throw new IllegalArgumentException("fieldPaths cannot be null or empty");
		}
		
		//holds the current object to check
		Object currentObject = target;
		
		//for each of the field paths
		for (String fieldPath : fieldPaths)
		{
			//find the field on the current object
			Field field = ReflectionUtil.findField(currentObject, fieldPath);
			field.setAccessible(true);
			currentObject = field.get(currentObject);
		}
		
		return (T) currentObject;
	}
}
