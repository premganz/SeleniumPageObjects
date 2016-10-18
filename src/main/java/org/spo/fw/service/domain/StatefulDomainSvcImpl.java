package org.spo.fw.service.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.exception.TestResourceServerException;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.util.StateExpressionWrapper;
import org.spo.fw.service.external.ExternalScriptSvc;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * 
 * @author Prem
 * Stateful Proxy to a domian Server , only closeSession will reset it to ground state
 *
 */
public class StatefulDomainSvcImpl implements StatefulDomainService {
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
		LinkedHashMap<String,Object> resultMap=new LinkedHashMap<String,Object>();
		try{
		resultMap = this.<LinkedHashMap<String,Object>>getExternalScriptSvc().queryTRS("datasync/nightly", resultMap);
		}catch(TestResourceServerException e){
			log.error("A TRS Error was recieved in StatefulDomainModel ");
			throw e;
		}
		return resultMap;
	}

	public String event_domain(String actor, String eventExpression){
		String toReturn="";
		eventExpression=eventExpression.replaceAll(" ","%20").replaceAll("/", "%2F");
		try{
		return this.<String>getExternalScriptSvc().queryTRSString("event/"+actor+"/"+eventExpression);
		}catch(TestResourceServerException e){
			log.error("A TRS Error was recieved in StatefulDomainModel ");
			throw e;
			//return "ERROR";
		}
	}

	public String event_domain(String actor, String eventExpression,String syntaxStyleCode){
		return event_domain(actor,refactorEventExpr(eventExpression));
	}
	
	private String refactorEventExpr(String abbr){
		abbr=abbr.replaceAll(", ", "&").replaceAll(",", "&");
		StringBuffer raw = new StringBuffer();
		StateExpressionWrapper expr = new StateExpressionWrapper(abbr);
		raw.append(expr.getName()+"&");
		for(String key:expr.keys()){
			if(!key.startsWith("--")){
			raw.append("fldName="+key+"&fldValue="+expr.value(key)+"&");
			}else{				
				raw.append(key.replaceAll("--","")+"="+expr.value(key)+"&");
			}
		}
		return raw.toString();
	}

	public List<String> getPage(String expression){		
		ArrayList<String> resultList= new ArrayList<String>();
		try{
		resultList = this.<ArrayList<String>>getExternalScriptSvc()
				.queryTRS("pages/"+expression,resultList);
		if(resultList.size()==1 && resultList.get(0).equals("ERROR") && resultList.get(0).startsWith("URL Not Found")){
			throw new SPOException("ERROR received for "+expression+" error was : "+resultList.get(0));
		}
		}catch(TestResourceServerException e){
			log.error("A TRS Error was recieved in StatefulDomainModel ");
			throw e;
			//return resultList;
		}
		return resultList;
	}

	

	@Override
	public void setPage(String expression, String content) {		
		ArrayList<String> resultList= new ArrayList<String>();
		//content=content.replaceAll("%","").replaceAll("/", "%2F");
		//content = UrlUtils.encodeAnchor(content);
		
		try{
			Map<String,String> paramsMap = new LinkedHashMap<String,String>();
			//paramsMap.put("content", content);
		this.<ArrayList<String>>getExternalScriptSvc()
				.postTRS("pages/cachedPageWrite/"+expression,resultList,content, paramsMap);
		if(resultList.size()==1 && resultList.get(0).equals("ERROR") && resultList.get(0).startsWith("URL Not Found")){
			throw new SPOException("ERROR received for "+expression+" error was : "+resultList.get(0));
		}
		}catch(TestResourceServerException e){
			log.error("A TRS Error was recieved in StatefulDomainModel ");
			throw e;
			//return resultList;
		}
	
	}
	
	

}
