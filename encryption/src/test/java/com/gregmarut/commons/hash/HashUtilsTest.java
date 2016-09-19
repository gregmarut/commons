package com.gregmarut.commons.hash;

import junit.framework.Assert;

import org.junit.Test;

public class HashUtilsTest
{
	@Test
	public void md5Hash()
	{
		Assert.assertEquals("0cbc6611f5540bd0809a388dc95a615b", HashUtils.md5("Test").toLowerCase());
	}
	
	@Test
	public void sha1Hash()
	{
		Assert.assertEquals("640ab2bae07bedc4c163f679a746f7ab7fb5d1fa", HashUtils.sha1("Test").toLowerCase());
	}
	
	@Test
	public void sha256Hash()
	{
		Assert.assertEquals("532eaabd9574880dbf76b9b8cc00832c20a6ec113d682299550d7a6e0f345e25", HashUtils
			.sha256("Test").toLowerCase());
	}
}
