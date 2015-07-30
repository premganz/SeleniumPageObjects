package org.spo.fw.exception;

public class ServiceLifeCycleException extends SPOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1004017273963002017L;

	
	@Override
	public String getMessage() {
	
		return "A life Cycle Violation Occured";
	}
	
}
