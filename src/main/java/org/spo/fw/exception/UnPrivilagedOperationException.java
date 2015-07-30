package org.spo.fw.exception;

public class UnPrivilagedOperationException extends SPOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1004017273963002017L;

	
	@Override
	public String getMessage() {
	
		return "An unprivilaged operation was attempted";
	}
	
}
