package com.gregmarut.commons.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.gregmarut.commons.util.ByteArray;

public class HashUtils
{
	public static final String MD5 = "MD5";
	public static final String SHA_1 = "SHA-1";
	public static final String SHA_256 = "SHA-256";
	
	public static String md5(final String input)
	{
		return hash(input.getBytes(), MD5);
	}
	
	public static String sha1(final String input)
	{
		return hash(input.getBytes(), SHA_1);
	}
	
	public static String sha256(final String input)
	{
		return hash(input.getBytes(), SHA_256);
	}
	
	private static String hash(final byte[] input, final String algorithm)
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
		return new ByteArray(bytes).toHexString();
	}
}
