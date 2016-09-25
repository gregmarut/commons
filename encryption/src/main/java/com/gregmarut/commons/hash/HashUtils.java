package com.gregmarut.commons.hash;

import com.gregmarut.commons.util.ByteArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils
{
	public static final String MD5 = "MD5";
	public static final String SHA_1 = "SHA-1";
	public static final String SHA_256 = "SHA-256";
	
	public static String md5(final String input)
	{
		return hashString(input.getBytes(), MD5);
	}
	
	public static byte[] md5(final byte[] input)
	{
		return hashBytes(input, MD5);
	}
	
	public static String sha1(final String input)
	{
		return hashString(input.getBytes(), SHA_1);
	}
	
	public static byte[] sha1(final byte[] input)
	{
		return hashBytes(input, SHA_1);
	}
	
	public static String sha256(final String input)
	{
		return hashString(input.getBytes(), SHA_256);
	}
	
	public static byte[] sha256(final byte[] input)
	{
		return hashBytes(input, SHA_256);
	}
	
	private static String hashString(final byte[] input, final String algorithm)
	{
		// return the byte array
		return new ByteArray(hashBytes(input, algorithm)).toHexString();
	}
	
	private static byte[] hashBytes(final byte[] input, final String algorithm)
	{
		// create the message digest object
		MessageDigest mdMD5;
		
		// holds the byte array to return
		byte[] bytes = null;
		
		try
		{
			// get an instance of the algorithm
			mdMD5 = MessageDigest.getInstance(algorithm);
			
			// reset the digest
			mdMD5.reset();
			
			// assign the byte array to return
			bytes = mdMD5.digest(input);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
		
		// return the byte array
		return bytes;
	}
}
