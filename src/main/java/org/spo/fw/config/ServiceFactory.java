package org.spo.fw.config;

import org.spo.fw.service.domain.StatefulDomainService;
import org.spo.fw.service.domain.StatefulDomainSvcImpl;
import org.spo.fw.service.external.ExternalScriptSvc;
import org.spo.fw.web.ServiceHub;

public class ServiceFactory {

	ServiceHub kw; 
	public ServiceFactory(ServiceHub kw){
		this.kw=kw;
	}
		
	public <T> ExternalScriptSvc<T> getExternalScriptSvc(){
		ExternalScriptSvc<T> svc = new ExternalScriptSvc<T>("9999" );
		svc.setServerRoot(SessionContext.appConfig.TEST_SERVER_BASE_URL);		
		return svc;
		
	}
	
	public StatefulDomainService getDomainSvc(){
		StatefulDomainService svc = new StatefulDomainSvcImpl();
		svc.setKw(kw);
		return svc;
	}
	
	
}
