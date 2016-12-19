package com.gregmarut.commons.util;

/**
 * A simple class which maps a key to a value
 * @param <K>
 * @param <V>
 */
public class KeyValue<K, V>
{
	private final K key;
	private final V value;
	
	public KeyValue(final K key, final V value)
	{
		this.key = key;
		this.value = value;
	}
	
	public K getKey()
	{
		return key;
	}
	
	public V getValue()
	{
		return value;
	}
}
