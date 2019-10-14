/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.service.external;

public interface JsonMessageItf<T> {
	
	
	public String getHeader() ;

	public T getPayload() ;

	
	
}
