package org.spo.fw.service.domain;

import java.util.ArrayList;
import java.util.List;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.exception.TestResourceServerException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.TestResourceServerConnector;
import org.spo.fw.service.external.ExternalScriptSvc;

/**
 * 
 * @author Prem
 * Stateful Proxy to a domian Server , only closeSession will reset it to ground state
 *
 */
public class StatefulDomainSvcImpl implements StatefulDomainService {
	Logger1 log = new Logger1("StatefulDomainSvcImpl");
	public String getState(){
		return "";
	}
	public void openSession(){
		if(SessionContext.testEnv.equals("AT")){
			event_domain("SettingsActor", "AT&");	
		}else{
			event_domain("SettingsActor", "Reset&quarter=4");
		}

	}

	public void reset(){
		openSession();
	}
	public void closeSession(){

	}

	public String event_domain(String actor, String eventExpression){
		String toReturn="";
		try{
		return SessionContext.appConfig
				.serviceFactory.<String>getExternalScriptSvc().queryTRSString("event/"+actor+"/"+eventExpression);
		}catch(TestResourceServerException e){
			log.error("A TRS Error was recieved in StatefulDomainModel ");
			throw e;
			//return "ERROR";
		}
	}


	public List<String> getPage(String expression){		
		ArrayList<String> resultList= new ArrayList<String>();
		try{
		resultList = SessionContext.appConfig
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

}
