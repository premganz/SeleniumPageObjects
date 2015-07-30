package org.spo.fw.exception;

public class SPOException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1004017273963002017L;

	String message;
	Throwable wrappedException;
	@Override
	public String getMessage() {
		if(wrappedException!=null){
			message = message +'\n'+" Original Exception type "+wrappedException.getClass().getName();
			if(wrappedException.getMessage()!=null){
				message = message +'\n'+"Orginal Message: "+wrappedException.getMessage();
			}
		}
		return "An Application Exception was thrown"+ " "+message;
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
