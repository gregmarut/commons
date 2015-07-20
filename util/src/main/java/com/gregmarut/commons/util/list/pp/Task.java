package com.gregmarut.commons.util.list.pp;

/**
 * A blueprint for how to define a task
 * 
 * @author Gregory Marut
 * @param <P>
 * @param <V>
 */
public interface Task<P, V>
{
	/**
	 * Executes the
	 * 
	 * @param p
	 *            the item to be processed
	 * @return the result of the task once the item is processed
	 */
	V execute(P p);
}
