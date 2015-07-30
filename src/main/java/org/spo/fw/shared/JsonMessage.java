package org.spo.fw.shared;

public class JsonMessage<T> implements JsonMessageItf<T> {

	private String header;
	private T payload;
	
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public T getPayload() {
		return payload;
	}
	public void setPayload(T payload) {
		this.payload = payload;
	}
	

	@Override
	public String toString() {

		return "JsonMessageItf:{\"header\"=\"\""+header+", \"payload\"=\""+payload.getClass().getSimpleName()+":"+payload+"\"}";
	}
}
