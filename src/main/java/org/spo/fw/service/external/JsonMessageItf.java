package org.spo.fw.service.external;

public interface JsonMessageItf<T> {
	
	
	public String getHeader() ;

	public T getPayload() ;

	
	
}
