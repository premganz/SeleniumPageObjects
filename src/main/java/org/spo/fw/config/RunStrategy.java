package org.spo.fw.config;

public class RunStrategy {
	
	
	public  boolean isVisibleBrowser;
	public  String browserName;
	public  String textFilesPath;
	public  boolean isRecordMode;
	public  boolean requireBasicAuthUrlPrefix;
	public boolean isProxyServerRequired;
	public boolean cleanupDrivers;//WARNING:USe with discretion with ie11, it kills of iexplore.exe and iedriverserver.exe. This can be bad if you wan to use iexplore in your machine in parallel or you are running on some shared machine. 
	public boolean reuseDriver;//WARNING:USe with discretion with ie11, it causes some strange side effects, such as "StaleElementExceptions" particularly with ie.
	public String testEnv;
	
	public String driverPath;
	public Constants.LogLevel logLevel;
	public Constants.LogMode logMode;
	public AppConfig appConfig;


	
	
	
	
	
}
