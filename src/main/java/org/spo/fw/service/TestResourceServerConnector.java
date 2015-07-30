package org.spo.fw.service;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.TestResourceServerException;
import org.spo.fw.log.Logger1;
import org.springframework.web.client.RestTemplate;


public class TestResourceServerConnector<T> {

	T result;
	Logger1 log = new Logger1(this.getClass().getSimpleName());

	public TestResourceServerConnector(T result) {
		this.result=result;
	}


	@SuppressWarnings("unchecked")
	public T queryServer(String serviceModuleName, String query ) throws TestResourceServerException{
		try {
			if(!query.contains("?")){
				query= query+"?meta=None";	
			}
			String url = SessionContext.appConfig.TEST_SERVER_BASE_URL+""+serviceModuleName+"/"+query;
			RestTemplate restTemplate = new RestTemplate();
			log.debug("calling server on url "+url);
			result= (T)restTemplate.getForObject(url , result.getClass());
		} catch (Exception e) {			
			throw new TestResourceServerException(e);
		}
		return result;

	}
	

}
