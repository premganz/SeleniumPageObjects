package org.spo.fw.runners;

import java.awt.Toolkit;
import java.util.LinkedHashMap;
import java.util.Map;

import org.spo.fw.config.Constants;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.launch.robot.RobotProxy;
import org.spo.fw.log.Logger1;


public class Node2_TimedLaunch {
	public static Map<String,String> driverPathMap = new LinkedHashMap<String,String>();
	static String basePath="";
	static String browser="phantom";
	public static RunStrategy defaultStrategy; 
	static Logger1 log = new Logger1("Node3_LaunchSeleniumScript");
	static {
		setSystemProps();
		System.getProperties().put("textScreens.path", "");
		basePath="";
		browser="phantom";
		
		

	}
	public  void launchRobotScript(String[] args) throws Exception {
		
		try{				
			RobotProxy.run(defaultStrategy,(new String[]{		
					"--include", "debug",	
					//"--exclude", "Ignore",							 
					//"-browser","ie", 
					"-browser","phantom",//"-browser","chrome",//"-browser","firefox",
					"-browserPath",driverPathMap.get("phantom"),
					//"-noBasicAuth","-isProxyServerRequired",
					"-logLevel","TRACE",
					//"--DefaultTimeOut","1200",
					"--flattenKeywords","name:*",
					"-textScreensPath", "",
					"-o",basePath+"output","-l",basePath+"log","-r",basePath+"report",
					"",
					
			}));



		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}finally{
			String stopProxyCommand = "reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" ^ More?    /v ProxyEnable /t REG_DWORD /d 0 /f";
			Runtime rt = Runtime.getRuntime();
			rt.exec(stopProxyCommand);
			Toolkit.getDefaultToolkit().beep();
		}
	}
	public static  void setSystemProps(){
		if(System.getProperty("phantomjs.binary.path")==null)
			if(System.getProperty("phantomjs.binary.path")==null)
				log.info("System.getProperty phantomjs.binary.path not set");
			if(System.getProperty("webdriver.ie.driver")==null)
				log.info("System.getProperty webdriver.ie.driver not set");
			if(System.getProperty("webdriver.chrome.driver")==null)
				log.info("System.getProperty webdriver.chrome.driver not set");
			if(System.getProperty("webdriver.firefox.bin")==null)
				log.info("System.getProperty webdriver.firefox.bin not set");
			if(System.getProperty("webdriver.development")==null)
				log.info("System.getProperty webdriver.development not set");
		driverPathMap.put("ie", "C:/Drivers/IEDriverServer.exe");
		driverPathMap.put("phantom", "C:\\Resources\\phantomjs.exe");
		driverPathMap.put("chrome", "C:/Selenium/chromedriver.exe");
		driverPathMap.put("firefox", "C:/Selenium/chromedriver.exe");

	}
}


