package org.spo.fw.config;

import org.spo.fw.service.domain.StatefulDomainService;
import org.spo.fw.service.domain.StatefulDomainSvcImpl;
import org.spo.fw.service.external.ExternalScriptSvc;

public class ServiceFactory {

		
	public <T> ExternalScriptSvc<T> getExternalScriptSvc(){
		ExternalScriptSvc<T> svc = new ExternalScriptSvc<T>( );
		svc.setServerRoot(SessionContext.appConfig.TEST_SERVER_BASE_URL);
		return svc;
		
	}
	
	public StatefulDomainService getDomainSvc(){
		return new StatefulDomainSvcImpl();
	}
	
	
}
