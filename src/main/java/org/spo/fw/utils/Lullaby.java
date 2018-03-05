package org.spo.fw.utils;

public class Lullaby {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param millis
	 * @param className
	 * @return
	 * 
	 * We are thinking of putting up this central handler for sleep, because the sleep is dependent onthe environment
	 * and on the load of the SUT. IT should ideally be configurable. 
	 * The configuration we foresee is to be based on static params
	 * We do not prefer a generic params map or a central configuration
	 * The calling class should know its possible reuse, size of the buffer that it can afford to trim in different circumstances
	 * We think it is a better design than to allow the system to trace the actual behaviour and move for a central config
	 * 
	 * So we are going to encode the degree of reuse, assumed buffer and env sensitivity as leading factors
	 * Can be combined into a composite score?
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static long config(long millis,String className) {
		return millis;
	}
	
}
