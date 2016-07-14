package org.spo.fw.specific.scripts.setup;

import org.spo.fw.config.Constants;
import org.spo.fw.runners.Node3_LaunchSeleniumScript;
/*
 * AS OF NOW APPLICATION IS SINGLE THREADED, CONSIDERING IE DRIVERFACTORY IS SINGLETHREADED
 * 
 * 
 */
//@TempCheckout

public class ScriptRunnerMYP extends Node3_LaunchSeleniumScript {
	@Override
	public void init() {
		super.init();//for default inits.
		customScriptProvider = new CustomScriptProviderMYP();
		if(System.getProperty("browser.name")==null){
			System.setProperty("browser.name", "Phantom");
		}
		if(System.getProperty("test.env")==null){
			System.setProperty("test.env", "");
		}	

		System.setProperty("phantomjs.binary.path", "C:\\pathto\\phantomjs.exe");


		//strategy = new RunStrategy(); relying on defaults
		//ignoreCustomStrategy=true;
		//strategy.isRecordMode=true;strategy.browserName="chrome";
		strategy.isRecordMode=false;strategy.browserName=System.getProperty("browser.name");

	/***/
	//strategy.testEnv="AT";//Possible Values are AT, Nightly and Local
	//strategy.testEnv="local";//Possible Values are AT, Nightly and Local
	//strategy.browserName="ie";
	strategy.logLevel=Constants.LogLevel.TRACE;
	//strategy.cleanupDrivers=true;
	/***/
	
	
		strategy.textFilesPath="C:\\path\\to\\WebRobot_WorkingDir\\";
		strategy.isProxyServerRequired=false;
		strategy.requireBasicAuthUrlPrefix=true;
	
			
		//strategy.reuseDriver=true;//USe with discretion with ie11, it causes some strange side effects.
		strategy.appConfig = new AppConfigMYP();
		if(strategy.testEnv.equals("AT")){
			strategy.appConfig.liteMode=true;
			strategy.browserName="ie";
		}

	}
}
