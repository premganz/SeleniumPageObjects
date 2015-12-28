
package org.spo.fw.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.service.domain.StatefulDomainService;
import org.spo.fw.service.domain.StatefulDomainSvcImpl;
//TODO : Avoid public variables
//TODO : Avoid public variables
public class AppConfig implements ExtensibleService{
	
	//Configs
	public   String basicAuth_userId="";
	public   String basicAuth_pwd="";
	public   String BASIC_AUTH_DIGEST= "";
	public   int SEARCH_WAIT_TIME = 4;//ideal 4 More wait time causes stale elem exception
	public   int DISPLAY_WAIT_TIME = 2;//Advice less than 2 More wait time causes stale elem exception
	public   int WEBDRIVER_TIMEOUT = 15;
	public   String UNSTABLE_PCS= "IEDriverServer.exe";//For killing long running processes, comma sep.

	public   String BASE_URL="https://en.wikipedia.org/wiki/Main_Page";
	public   String URL_UNIT_TEST_MODE=BASE_URL+""; 
	public   String URL_AT=""; 


	public   int DRIVER_POOL_SIZE =2;
	public   boolean eclipseMode= false;

	public boolean debugMode;

	public   String TEST_SERVER_BASE_URL="";

	//USE WITH CARE
	public   boolean atMode=false;
	public  String domainPackageName="org.spo";
	public    String[]  whiteListedHosts = {"ML-PREMGANESH"};
	public Map<String,String> systemProperties = new LinkedHashMap<String,String>();
	public Map<String,String> customProperties = new LinkedHashMap<String,String>();

	public void init(){
		systemProperties.put("webdriver.chrome.driver", "C:\\works\\chromedriver.exe");
	}

	//More Services to come here
	//public StatefulDomainService domainSvc = new StatefulDomainSvcImpl();
	//public ServiceFactory serviceFactory = new ServiceFactory();
	
}
