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
import org.spo.fw.web.ServiceHub;

/**
 * 
 * @author Prem
 * Stateful Proxy to a domian Server , only closeSession will reset it to ground state
 *
 */
public class StatefulDomainSvcImpl implements StatefulDomainService {
	Logger1 log = new Logger1("StatefulDomainSvcImpl");
	private ServiceHub kw;
	public String getState(){
		return "";
	}
	
	public void openSession(){
		event_domain("init", "init&");
		if(SessionContext.testEnv.equals("AT")){
			event_domain("SettingsActor", "AT&");	
		}else{
			event_domain("SettingsActor", "Reset&quarter=1");
		}

	}

	public void reset(){
		openSession();
	}
	public void closeSession(){

	}
	
	public Map<String,Object> getDomainState(){
		LinkedHashMap<String,Object> resultMap=new LinkedHashMap<String,Object>();
		try{
		resultMap= kw.serviceFactory.<LinkedHashMap<String,Object>>getExternalScriptSvc().queryTRS("datasync/nightly", resultMap);
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
		return kw
				.serviceFactory.<String>getExternalScriptSvc().queryTRSString("event/"+actor+"/"+eventExpression);
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
		resultList = kw
				.serviceFactory.<ArrayList<String>>getExternalScriptSvc()
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

	public ServiceHub getKw() {
		return kw;
	}

	public void setKw(ServiceHub kw) {
		this.kw = kw;
	}
	
	

}
