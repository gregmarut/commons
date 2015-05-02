package com.gregmarut.commons.filecache;

public class IllegalFileException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public IllegalFileException(final String message)
	{
		super(message);
	}
}
