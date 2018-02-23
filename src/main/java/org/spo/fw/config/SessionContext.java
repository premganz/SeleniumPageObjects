package org.spo.fw.config;

import org.spo.fw.config.Constants.LogLevel;
import org.spo.fw.log.Logger1;
//TODO To rename as SessionContext
public class SessionContext {
	
	/** This has to implemented like a notice board full of static variables, because SessionDriverControllers that use it 
	 * are capable of being instatantized from RobotFramework
	 * Note that the design is always single threaded and single user. 
	 * No session specific data is there.
	 * 
	 */
	
	public static  boolean isVisibleBrowser;
	public static  String browserName;
	public static  String textFilesPath;
	public  static boolean isRecordMode;
	public  static boolean requireBasicAuthUrlPrefix;
	public static boolean isProxyServerRequired;
	public static String testEnv;
	public static String currentTestClass;
	public static boolean isAuthoizationFailed;// an across app alarm button.
	public static boolean cleanupDrivers;
	public static boolean reuseDriver;
	public static boolean isBrowserLess;
	protected static Logger1 log;
	public static LogLevel logLevel;
	public static AppConfig appConfig;
	public static int sharedCounter =0;
	
	
	
	
	public static void publishStrategy(RunStrategy strategy) {
		log =  new Logger1("org.spo.fw.config.RunStrategyNoticeBoard");
		isVisibleBrowser = strategy.isVisibleBrowser;
		browserName = strategy.browserName;
		textFilesPath= strategy.textFilesPath;
		isRecordMode = strategy.isRecordMode;
		requireBasicAuthUrlPrefix = strategy.requireBasicAuthUrlPrefix;
		isProxyServerRequired= strategy.isProxyServerRequired;
		logLevel = strategy.logLevel;
		cleanupDrivers=strategy.cleanupDrivers;
		testEnv=strategy.testEnv;
		reuseDriver=strategy.reuseDriver;
		isBrowserLess=strategy.isBrowserLess;
		appConfig=strategy.appConfig;
		if(appConfig!=null)appConfig.init();
		
		log.info("Publishing a strategy with the follosing properties : "+'\n'+
		"isVisibleBrowser = "+strategy.isVisibleBrowser+'\n'+
		"browserName = "+strategy.browserName+'\n'+
		"isBrowserLess = "+strategy.isBrowserLess+'\n'+
		"textFilesPath= "+strategy.textFilesPath+'\n'+
		"isRecordMode = "+strategy.isRecordMode+'\n'+
		"requireBasicAuthUrlPrefix = "+strategy.requireBasicAuthUrlPrefix+'\n'+
		"logLevel ="+ strategy.logLevel+'\n'+
		"cleanupDrivers ="+ strategy.cleanupDrivers+'\n'+
		"reuseDriver ="+ strategy.reuseDriver+'\n'+
		"isProxyServerRequired= "+strategy.isProxyServerRequired+'\n'+
		"testENV="+strategy.testEnv);
		

	}
	
	public static RunStrategy snapshotStrategy(){
		RunStrategy strategy = new RunStrategy();
		strategy.isVisibleBrowser=isVisibleBrowser ;
		strategy.browserName=browserName  ;
		strategy.textFilesPath=textFilesPath ;
		strategy.isRecordMode=isRecordMode;
		 strategy.requireBasicAuthUrlPrefix=requireBasicAuthUrlPrefix;
		 strategy.isProxyServerRequired= isProxyServerRequired;
		 strategy.testEnv=testEnv;
		 strategy.reuseDriver=reuseDriver;
		 strategy.appConfig=appConfig;
		 return strategy;

	}
	

}
