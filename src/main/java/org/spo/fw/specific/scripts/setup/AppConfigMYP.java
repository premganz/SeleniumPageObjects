package org.spo.fw.specific.scripts.setup;

import org.spo.fw.config.AppConfig;
import org.spo.fw.service.precommit.TempCheckout;

public class AppConfigMYP extends AppConfig{


	public static final String ZIP_INSTALLATION_DIRECTORY = "\"C:/Yo/7";

	public void init() {
		basicAuth_userId="user";
		basicAuth_pwd="password";
		BASIC_AUTH_DIGEST= "Basic AAAAAAAA";
		SEARCH_WAIT_TIME = 4;//ideal 4 More wait time causes stale elem exception
		DISPLAY_WAIT_TIME = 2;//Advice less than 2 More wait time causes stale elem exception
		WEBDRIVER_TIMEOUT = 25;
		UNSTABLE_PCS= "IEDriverServer.exe,IExplore.exe,Werfault.exe";//For killing long running processes, comma sep.
		
		BASE_URL="http://hostname/build_name/";
		URL_UNIT_TEST_MODE=BASE_URL+"cm_home.aspx"; 
		URL_AT="http://hostname/build_name/"; 

		DRIVER_POOL_SIZE =2;
		eclipseMode= false;

		

		TEST_SERVER_BASE_URL="http://hostname:8081/xyz/";

		//USE WITH CARE
		atMode=false;
		domainPackageName="org.spo";
		String[]  whiteListedHosts1= {"host1","host11", };
		whiteListedHosts=whiteListedHosts1;
		URL_UNIT_TEST_MODE=BASE_URL+"home.jsp";
		customProperties.put("DB_NAME","holla");
	}
	
}
