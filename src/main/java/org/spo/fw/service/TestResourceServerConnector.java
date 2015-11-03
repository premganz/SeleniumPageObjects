package org.spo.fw.service;

import org.spo.fw.exception.TestResourceServerException;
import org.spo.fw.log.Logger1;
import org.springframework.web.client.RestTemplate;


public class TestResourceServerConnector<T> {
	private RestTemplate restTemplate = new RestTemplate();
	private T result;
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	Class<T> resultType;

	public TestResourceServerConnector(T result) {
		this.result=result;
	}
	public TestResourceServerConnector(Class<T> resultType) {
		this.resultType=resultType;
	}


	@SuppressWarnings("unchecked")
	public T queryServer(String query ) throws TestResourceServerException{
		
		try {
			if(!query.contains("?")){
				query= query+"?meta=None";	
			}
			if(!query.startsWith("http") && !query.startsWith("/")){
				query="/"+query;
			}
			log.debug("calling server on url "+query);
			result= (T)restTemplate.getForObject(query , result.getClass());
		} catch (Exception e) {	
			log.error("A Server Exception occured for query "+query);
			//log.info(e);
			throw new TestResourceServerException(e);
		}if(result==null){
			log.error("A Server Exception occured for query "+query+" null value of result ");
			throw new TestResourceServerException(new NullPointerException());
		}
		return result;

	}

	

}
