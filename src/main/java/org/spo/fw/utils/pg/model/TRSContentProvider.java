package org.spo.fw.utils.pg.model;

import java.util.ArrayList;
import java.util.List;

import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.TestResourceServerConnector;
import org.spo.fw.utils.pg.itf.StaticContentProvider;

public class TRSContentProvider implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	@Override
	public List<String> getContent(String expression) {
		return core_getFileFromServer(  expression);
	}
	
	//Split by path delimiters
		public List<String> core_getFileFromServer( String expression) {
			List<String> expectedPageContent= new ArrayList<String>();

			if(!expression.contains("?")){
				expression= expression+"?meta=None";	
			}

			try{
				expectedPageContent= util_checkWithServer(expression);
			}catch(Exception e){
				log.info(e);
				throw new AssertionError(e.getClass().getName()+" was thrown");
			}
			//FileContent pageContent= core_processFileContent(expectedPageContent, null);
			return expectedPageContent;

		}



		public List<String> util_checkWithServer(String scenario, String pageName){
			//String result = "";
			ArrayList<String> resultList = new ArrayList<String>();
			try {
				String queryUrl = scenario+"/"+pageName;
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
				resultList = con.queryServer("pages", queryUrl);
			} catch (Exception e) {
				log.info(e);
				//SessionContainer.storeLogToSession("WARN : Server Connection for url failed for : "+pageName);
				//log.debug("Recovering silently");
				throw e;
			}
			if(resultList.size()==1 && resultList.get(0).equals("ERROR")){
				throw new SPOException("ERROR received for "+queryUrl);
			}
			return resultList;	

		}
	
	
}
