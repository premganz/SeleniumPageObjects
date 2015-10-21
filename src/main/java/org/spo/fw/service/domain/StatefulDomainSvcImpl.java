package org.spo.fw.service.domain;

import java.util.ArrayList;
import java.util.List;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.TestResourceServerConnector;

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
		//String result = "";
		ArrayList<String> resultList = new ArrayList<String>();
		if(!eventExpression.contains("?")){
			eventExpression = eventExpression +"?meta=None";
		}
		try {

			TestResourceServerConnector<ArrayList<String>> con = new TestResourceServerConnector<ArrayList<String>>(resultList);
			resultList = con.queryServer(SessionContext.appConfig.TEST_SERVER_BASE_URL+"event/"+actor+"/"+eventExpression);
		} catch (Exception e) {
			log.info(e);
			throw e;
		}

		return resultList.toString();	

	}


	public List<String> getPage(String expression){		
		List<String> expectedPageContent= new ArrayList<String>();
		if(!expression.contains("?")){
			expression= expression+"?meta=None";	
		}
		ArrayList<String> resultList = new ArrayList<String>();
		TestResourceServerConnector<ArrayList<String>> con = new TestResourceServerConnector<ArrayList<String>>(resultList);
		resultList = con.queryServer(expression);
		if(resultList.size()==1 && resultList.get(0).equals("ERROR")){
			throw new SPOException("ERROR received for "+expression);
		}
		return expectedPageContent;
	}

}
