/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.meta.fixture.runner;

import org.spo.fw.config.Constants;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.runners.Node3_LaunchSeleniumScript;
/*
 * AS OF NOW APPLICATION IS SINGLE THREADED, CONSIDERING IE DREIVERFACTORY IS SINGLETHREADED
 */

public class StubScriptRunner extends Node3_LaunchSeleniumScript {
	@Override
	public  void init() {
	
		
		//Requires Firefox 14 or 12
		
		//1.INITIATING DEFAULT STRATEGY
				strategy = new RunStrategy();
		ignoreCustomStrategy=false;
		//strategy.isRecordMode=true;strategy.browserName="chrome";
				strategy.isRecordMode=false;strategy.browserName=System.getProperty("browser.name");
				strategy.testEnv=System.getProperty("test.env");
		//strategy.testEnv="AT";
		strategy.browserName="phantom";		
		strategy.isBrowserLess=false;
				strategy.textFilesPath="C:/works/spoworks/";
				strategy.isProxyServerRequired=false;
				strategy.requireBasicAuthUrlPrefix=true;
//		strategy.logLevel=Constants.LogLevel.TRACE;
				strategy.requireBasicAuthUrlPrefix=true;
				strategy.appConfig =new AppConfigStub();
	
	}
}
