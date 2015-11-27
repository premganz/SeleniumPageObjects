package org.spo.fw.exception;

import org.seleniumhq.jetty7.util.log.Log;

public class SPOException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1004017273963002017L;

	String message;
	Throwable wrappedException;
	
	@Override
	public String getMessage() {
		String message="An application exception was thrown : ";
		if(wrappedException!=null){
			message = message +'\n'+" Original Exception type "+wrappedException.getClass().getName();
			if(wrappedException.getMessage()!=null){
				message = message +'\n'+"Orginal Message: "+wrappedException.getMessage();
				message=message+" :: original exception was "+wrappedException.getClass().getName();
			}
		}
		return message;
	}

	public SPOException(String message){
		this.message = message;	
	}
	public SPOException(){

	}
	public SPOException(Throwable e){
		wrappedException=e;

	}


	public void setCause(Throwable e){
		wrappedException = e;
	}
}
