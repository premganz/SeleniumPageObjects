package org.spo.fw.exception;

public class TestDataException extends SPOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1004017273963002017L;

	
	@Override
	public String getMessage() {
	
		return "Test Data Not Suitable";
	}
	
}
