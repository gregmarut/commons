package com.gregmarut.commons.util.list.pp;

/**
 * An interface that will allow for updates whenever tasks are completed
 * 
 * @author Greg Marut
 */
public interface TaskCallbackListener
{
	void preExecute(int total);
	
	void processed(int processed, int total);
	
	void finished();
}
