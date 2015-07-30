package org.spo.fw.exception;

public class TestResourceServerException extends SPOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1004017273963002014L;

	
	
	public TestResourceServerException(Throwable e){
		wrappedException=e;
		message="Connection to TestResource server failed";

	}
	
}
