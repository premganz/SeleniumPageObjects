package org.spo.fw.service.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.spo.fw.config.AppConfig;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.exception.TestResourceServerException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.external.ExternalScriptSvc;
import org.spo.fw.utils.pg.model.FlatFileStaticContentProvider;

/**
 * 
 * @author Prem
 * Stateful Proxy to a domian Server , only closeSession will reset it to ground state
 *
 */
public class StatefulDomainSvcSimpleImpl implements StatefulDomainService {
	Logger1 log = new Logger1("StatefulDomainSvcImpl");
	//private ServiceHub kw;
	public String getState(){
		return "";
	}
	
	public void openSession(){
		event_domain("init", "init&");
	}

	public <T> ExternalScriptSvc<T> getExternalScriptSvc(){
		ExternalScriptSvc<T> svc = new ExternalScriptSvc<T>("9999" );
		svc.setServerRoot(SessionContext.appConfig.TEST_SERVER_BASE_URL);		
		return svc;
		
	}	
	
	
	public void reset(){
		openSession();
	}
	public void closeSession(){

	}
	
	public Map<String,Object> getDomainState(){
		log.error("This is unsupported in this implementation");
		throw new SPOException();
	}

	public String event_domain(String actor, String eventExpression){
		log.error("This is unsupported in this implementation");
		throw new SPOException();
			//return "ERROR";
		
	}

	public String event_domain(String actor, String eventExpression,String syntaxStyleCode){
		log.error("This is unsupported in this implementation");
		throw new SPOException();
	}
	
	private String refactorEventExpr(String abbr){
		log.error("This is unsupported in this implementation");
		throw new SPOException();
	}

	public List<String> getPage(String expression){	
		
		String filePath = SessionContext.appConfig.TEST_SERVER_BASE_URL.replaceAll("file://", "")+"pages/"+expression;
		
		FlatFileStaticContentProvider dynaValUtils = new FlatFileStaticContentProvider();
		return dynaValUtils.getContent(filePath, null);
		
		
	}

	

	@Override
	public void setPage(String expression, String content) {		
		log.error("This is unsupported in this implementation");
		throw new SPOException();
	
	}
	
	

}
