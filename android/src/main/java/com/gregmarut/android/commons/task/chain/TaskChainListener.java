package com.gregmarut.android.commons.task.chain;

public interface TaskChainListener
{
	void taskChainCompleted();
	
	void taskChainCanceled();
}
