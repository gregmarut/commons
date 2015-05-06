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
package com.gregmarut.commons.util;

import java.util.Arrays;

/**
 * Provides additional functionality to the byte array
 */
public class ByteArray
{
	// holds the array of bytes
	protected byte[] bytes;
	
	/**
	 * The constructor for ByteArray
	 */
	public ByteArray()
	{
		// initialize the byte array
		bytes = new byte[0];
	}
	
	/**
	 * Constructs a ByteArray with a default size
	 * 
	 * @param length
	 */
	public ByteArray(final int length)
	{
		// initialize the byte array
		bytes = new byte[length];
		
		// fill the array with byte 0
		Arrays.fill(bytes, (byte) 0);
	}
	
	/**
	 * Constructs a ByteArray with content
	 * 
	 * @param bytes
	 */
	public ByteArray(final byte[] bytes)
	{
		// save the byte array
		this.bytes = bytes;
	}
	
	/**
	 * Appends a byte to the end of this byte array
	 * 
	 * @param byte
	 */
	public void append(byte b)
	{
		append(new byte[] { b });
	}
	
	/**
	 * Appends an array of bytes to the end of this byte array
	 * 
	 * @param bytes
	 */
	public void append(byte[] bytes)
	{
		// create a new byte array object
		ByteArray combined = combineWith(bytes);
		
		// reset the array of bytes
		this.bytes = combined.getBytes();
	}
	
	/**
	 * Appends a boolean to the byte array
	 * 
	 * @param bool
	 */
	public void append(boolean bool)
	{
		// determine the byte to append
		byte b = (bool ? (byte) 1 : (byte) 0);
		
		// append the byte
		append(b);
	}
	
	/**
	 * Combines two byte arrays
	 * 
	 * @param bytesToAppend
	 * @return
	 */
	public ByteArray combineWith(final byte[] bytesToAppend)
	{
		// holds the byte array to return
		byte[] byteReturn = new byte[bytes.length + bytesToAppend.length];
		
		// copy the first array
		for (int i = 0; i < bytes.length; i++)
		{
			byteReturn[i] = bytes[i];
		}
		
		// append the second array
		for (int i = 0; i < bytesToAppend.length; i++)
		{
			byteReturn[i + bytes.length] = bytesToAppend[i];
		}
		
		// return the new combined array
		return new ByteArray(byteReturn);
	}
	
	/**
	 * Copies a byte array with a specified length
	 * 
	 * @return
	 */
	public ByteArray copy()
	{
		return copy(bytes.length);
	}
	
	/**
	 * Copies a byte array with a specified length
	 * 
	 * @param length
	 * @return
	 */
	public ByteArray copy(int length)
	{
		// holds the byte array to return
		byte[] byteReturn = new byte[length];
		
		// fill the array with byte 0
		Arrays.fill(byteReturn, (byte) 0);
		
		// determine the smaller number
		int smallerLength = (bytes.length < length ? bytes.length : length);
		
		// loop until the smaller length
		for (int i = 0; i < smallerLength; i++)
		{
			byteReturn[i] = bytes[i];
		}
		
		return new ByteArray(byteReturn);
	}
	
	/**
	 * Extracts a sub array from a byte array
	 * 
	 * @param index
	 * @param length
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public ByteArray subArray(final int index, final int length) throws ArrayIndexOutOfBoundsException
	{
		// holds the byte array to return
		byte[] result = new byte[length];
		
		// make sure the index is within the bounds
		if (index < 0 || index >= bytes.length)
		{
			throw new ArrayIndexOutOfBoundsException("Index is not within the array bounds");
		}
		
		// make sure the length is not 0
		else if (length < 0)
		{
			throw new ArrayIndexOutOfBoundsException("Length must be greater than 0");
		}
		
		// make sure the length does not exceed the byte array length
		else if (index + length > bytes.length)
		{
			throw new ArrayIndexOutOfBoundsException("Index + Length is not within the array bounds");
		}
		
		// loop through the array
		for (int i = index; i < index + length; i++)
		{
			// copy the byte from the array
			result[i - index] = bytes[i];
		}
		
		// return the byte array
		return new ByteArray(result);
	}
	
	/**
	 * Extracts a sub array from a byte array
	 * 
	 * @param index
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public ByteArray subArray(final int index) throws ArrayIndexOutOfBoundsException
	{
		// return the byte array
		return subArray(index, bytes.length - index);
	}
	
	/**
	 * Resizes the current byte array
	 * 
	 * @param size
	 */
	public void resize(final int size)
	{
		// get the difference
		int intDifference = size - bytes.length;
		
		// check to see if the difference is positive
		if (intDifference > 0)
		{
			// add to the size of this byte array
			append(new byte[intDifference]);
		}
		// check to see if the difference is negative
		else if (intDifference < 0)
		{
			bytes = subArray(0, size).getBytes();
		}
	}
	
	/**
	 * Sets a value in the byte array at a specified index
	 * 
	 * @param index
	 * @param b
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void setByte(int index, byte b) throws ArrayIndexOutOfBoundsException
	{
		bytes[index] = b;
	}
	
	/**
	 * Returns a byte at a specified location
	 * 
	 * @param index
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public byte getByte(int index) throws ArrayIndexOutOfBoundsException
	{
		return bytes[index];
	}
	
	/**
	 * Sets a value in the byte array at a specified index
	 * 
	 * @param index
	 * @param value
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void setBoolean(int index, boolean value) throws ArrayIndexOutOfBoundsException
	{
		bytes[index] = (byte) (value ? 1 : 0);
	}
	
	/**
	 * Returns a bit at a specified location
	 * 
	 * @param index
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public boolean getBoolean(int index) throws ArrayIndexOutOfBoundsException
	{
		return bytes[index] > 0;
	}
	
	/**
	 * Returns a char at a specified location
	 * 
	 * @param index
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public char getChar(int index) throws ArrayIndexOutOfBoundsException
	{
		return (char) bytes[index];
	}
	
	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 17 * hash + Arrays.hashCode(this.bytes);
		return hash;
	}
	
	/**
	 * Returns an int at a specified location
	 * 
	 * @param index
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public int getInt(int index) throws ArrayIndexOutOfBoundsException
	{
		return bytes[index] & 0xFF;
	}
	
	/**
	 * Returns the array of bytes
	 * 
	 * @return
	 */
	public byte[] getBytes()
	{
		return bytes;
	}
	
	/**
	 * Returns the size of the byte array
	 * 
	 * @return
	 */
	public int getSize()
	{
		return bytes.length;
	}
	
	/**
	 * Converts the ByteArray to a string
	 */
	@Override
	public String toString()
	{
		return new String(bytes);
	}
	
	/**
	 * Returns the byte array as a hex string
	 * 
	 * @return
	 */
	public String toHexString()
	{
		// holds the string builder object to return
		StringBuilder sb = new StringBuilder();
		
		// for every byte
		for (int i = 0; i < bytes.length; i++)
		{
			int intCurrent = bytes[i] & 0xFF;
			
			// check to see if a leading 0 is needed
			if (intCurrent < 0x10)
			{
				// append a 0
				sb.append(0);
			}
			
			String s = Integer.toHexString(intCurrent).toUpperCase();
			
			// append the character
			sb.append(s);
		}
		
		// return the string
		return sb.toString();
	}
	
	/**
	 * Converts a byte array to an int value
	 * 
	 * @return
	 */
	public int toInt()
	{
		return (int) toLong();
	}
	
	/**
	 * Converts a byte array to a long value
	 * 
	 * @return
	 */
	public long toLong()
	{
		// holds the int to return
		long result = 0;
		
		// for every byte value
		for (int i = 0; i < bytes.length; i++)
		{
			// Extract the bits out of the array by "and"ing them with the
			// maximum value of
			// a byte. This is done because java does not support unsigned
			// types. Now that the
			// unsigned byte has been extracted, shift it to the right as far as
			// it is needed.
			// Examples:
			// byte array int
			// {0x01, 0x00} = 256
			//
			// byte array int
			// {0x01, 0x8C 0xF0} = 0x018CF0
			result += (byteToLong(bytes[i]) << (Byte.SIZE * (bytes.length - i - 1)));
		}
		
		// return the int
		return result;
	}
	
	/**
	 * Converts a byte to a long
	 * 
	 * @param b
	 * @return
	 */
	private long byteToLong(byte b)
	{
		return (b & 0xFF);
	}
	
	/**
	 * Override the equals
	 * 
	 * @param object
	 */
	@Override
	public boolean equals(final Object object)
	{
		// holds the result to return
		boolean result = false;
		
		// check to see if this is an instance of byte array
		if (object instanceof ByteArray)
		{
			// recast the array
			ByteArray other = (ByteArray) object;
			
			// check to see if the arrays are the same
			result = Arrays.equals(bytes, other.getBytes());
		}
		
		// return the result
		return result;
	}
}
