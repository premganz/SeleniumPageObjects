package org.spo.fw.web;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.spo.fw.exception.SPOException;
import org.spo.fw.service.TestResourceServerConnector;



public class Lib_Messaging extends Lib_KeyWordsCore{


	public Lib_Messaging(WebDriver driver) {
		super(driver);
	}

	


	public List<String> util_checkWithServer(String actor, String eventName){
		//String result = "";
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			String queryUrl = actor+"/"+eventName;
			return util_checkWithServer(queryUrl);
		} catch (Exception e) {
			log.info(e);
		}

		return resultList;	

	}
	
	public List<String> util_checkWithServer(String queryUrl) throws Exception{
		//String result = "";
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			log.debug("calling server on url "+queryUrl);
			TestResourceServerConnector<ArrayList<String>> con = new TestResourceServerConnector<ArrayList<String>>(resultList);
			resultList = con.queryServer("event", queryUrl);
		} catch (Exception e) {
			log.info(e);
			//SessionContainer.storeLogToSession("WARN : Server Connection for url failed for : "+pageName);
			//log.debug("Recovering silently");
			throw e;
		}

		return resultList;	

	}
	
	


	public void setDomainEvent(String actor, String eventExpression) throws SPOException {
		if(!eventExpression.contains("?")){
			eventExpression = eventExpression +"?meta=None";
		}
		List<String> expectedPageContent= util_checkWithServer(actor, eventExpression);
		if(expectedPageContent.size()==1 && expectedPageContent.get(0).equals("ERROR")){
			throw new SPOException("Error Returned by Doc Server server msg :"+expectedPageContent.get(0));
		}
		
		
		
	}
}
