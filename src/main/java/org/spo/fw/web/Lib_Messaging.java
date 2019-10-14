/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.web;

import java.util.ArrayList;
import java.util.List;

import org.spo.fw.config.TRSModules;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.TestResourceServerConnector;


@Deprecated
public class Lib_Messaging{


Logger1 log = new Logger1(this.getClass().getName());
	


//	public List<String> util_checkWithServer(String actor, String eventName){
//		//String result = "";
//		ArrayList<String> resultList = new ArrayList<String>();
//		try {
//			String queryUrl = actor+"/"+eventName;
//			return util_checkWithServer(queryUrl);
//		} catch (Exception e) {
//			log.info(e);
//		}
//
//		return resultList;	
//
//	}
//	
//	public List<String> util_checkWithServer(String queryUrl) throws Exception{
//		//String result = "";
//		ArrayList<String> resultList = new ArrayList<String>();
//		try {
//			log.debug("calling server on url "+queryUrl);
//			TestResourceServerConnector<ArrayList<String>> con = new TestResourceServerConnector<ArrayList<String>>(resultList);
//			resultList = con.queryServer(TRSModules.mod_doc_domain_event, queryUrl);
//		} catch (Exception e) {
//			log.info(e);
//			//SessionContainer.storeLogToSession("WARN : Server Connection for url failed for : "+pageName);
//			//log.debug("Recovering silently");
//			throw e;
//		}
//
//		return resultList;	
//
//	}
	
	

//
//	public void setDomainEvent(String actor, String eventExpression) throws SPOException {
//		if(!eventExpression.contains("?")){
//			eventExpression = eventExpression +"?meta=None";
//		}
//		List<String> expectedPageContent= util_checkWithServer(actor, eventExpression);
//		if(expectedPageContent.size()==1 && expectedPageContent.get(0).equals("ERROR")){
//			throw new SPOException("Error Returned by Doc Server server msg :"+expectedPageContent.get(0));
//		}
//		
//		
//		
//	}
}
