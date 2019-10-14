
/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.meta.fixture.runner;

import org.spo.fw.config.AppConfig;
//TODO : Avoid public variables
//TODO : Avoid public variables
public class AppConfigStub extends AppConfig{
	
	public void init(){
		System.getProperties().put("webdriver.chrome.driver", "C:\\works\\chromedriver.exe");
		System.setProperty("phantomjs.binary.path", "C:\\Selenium\\phantomjs-1.9.7-windows\\phantomjs.exe");
		SEARCH_WAIT_TIME = 0;//ideal 4 More wait time causes stale elem exception
		DISPLAY_WAIT_TIME = 0;//Advice less than 2 More wait time causes stale elem exception
		WEBDRIVER_TIMEOUT = 15;
	}

	//More Services to come here
	//public StatefulDomainService domainSvc = new StatefulDomainSvcImpl();
	//public ServiceFactory serviceFactory = new ServiceFactory();
	
}
