/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.service.domain.StatefulDomainService;
import org.spo.fw.service.domain.StatefulDomainSvcImpl;
//TODO : Avoid public variables
//TODO : Avoid public variables
public class AppConfig implements ExtensibleService{
	/*
	 * @author prem
	 * 
	 * This is the config that is not generally lmited to a single run, but streaches across for a longer time and sequence of run, 
	 * for instance the base url fo the application anunder test.
	 * This could be overridden in the init method
	 * The init method could also be used to do stuff that is required at the beginning like setting path to webdrivers.
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	//Configs
	public   String basicAuth_userId="";
	public   String basicAuth_pwd="";
	public   String BASIC_AUTH_DIGEST= "";
	public   int SEARCH_WAIT_TIME = 4;//ideal 4 More wait time causes stale elem exception
	public   int DISPLAY_WAIT_TIME = 2;//Advice less than 2 More wait time causes stale elem exception
	public   int WEBDRIVER_TIMEOUT = 15;
	public   String UNSTABLE_PCS= "IEDriverServer.exe";//For killing long running processes, comma sep.
	public 	String SITE_IDENTIFIER="";
	public   String BASE_URL="https://en.wikipedia.org/wiki/Main_Page";
	public   String URL_UNIT_TEST_MODE=BASE_URL+""; 
	public   String URL_AT=""; 

	
	public   int DRIVER_POOL_SIZE =2;
	public   boolean eclipseMode= false;

	public boolean debugMode;
	public boolean liteMode;
	public boolean criticalOnlyMode;
	
	public   String TEST_SERVER_BASE_URL="";

	//USE WITH CARE
	public   boolean atMode=false;
	public  String domainPackageName="org.spo";
	public    String[]  whiteListedHosts = {"ML-PREMGANESH"};
	//public Map<String,String> systemProperties = System.getProperties();
	public Map<String,String> customProperties = new LinkedHashMap<String,String>();

	public void init(){
		System.getProperties().put("webdriver.chrome.driver", "C:\\works\\chromedriver.exe");
		System.setProperty("phantomjs.binary.path", "C:\\Selenium\\phantomjs-1.9.7-windows\\phantomjs.exe");
	}

	//More Services to come here
	//public StatefulDomainService domainSvc = new StatefulDomainSvcImpl();
	//public ServiceFactory serviceFactory = new ServiceFactory();
	
}
