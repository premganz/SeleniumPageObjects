package org.spo.fw.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.spo.fw.config.Constants;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.exception.ServiceLifeCycleException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.proxy.ProxyServerController;


//@TempCheckout
public class DriverFactory{
	public static LinkedList<WebDriver> driverQ = new LinkedList<WebDriver>();


	private static boolean isHtmlUnit=false; 
	private static boolean isIE=false;
	private static boolean isFireFox = false;
	private static boolean isChrome=false;
	public static DesiredCapabilities capabilitiesPhantom = DesiredCapabilities.phantomjs();
	static DesiredCapabilities capabilitiesChrome = DesiredCapabilities.chrome();
	static DesiredCapabilities capabilitiesIE = DesiredCapabilities.internetExplorer();
	static DesiredCapabilities capabilitiesFF = DesiredCapabilities.firefox();
	private static RunStrategy runStrategy;
	private static ChromeOptions chromeOptions = new ChromeOptions();
	private static Logger1 log=new Logger1("DriverFactory");//FIXME : Slightly risky code, a static declaration once caused initialization issue, 

	private static  Constants.LifeCycleState state=Constants.LifeCycleState.NULL;

	private static WebDriver staticInstance;
	
	public static void reportCrashOfDriver(){
		log.debug("Browser crashed ");
		staticInstance=null;
	}
	
	public static Constants.LifeCycleState getState() {
		return state;
	}
	private static void configProxyCapabilities() throws Exception{

		if(runStrategy.isProxyServerRequired){
			ProxyServerController.init(runStrategy);
			capabilitiesIE.setCapability(CapabilityType.PROXY, ProxyServerController.getProxy());
			capabilitiesChrome.setCapability(CapabilityType.PROXY, ProxyServerController.getProxy());
			capabilitiesPhantom.setCapability(CapabilityType.PROXY, ProxyServerController.getProxy());
			capabilitiesFF.setCapability(CapabilityType.PROXY, ProxyServerController.getProxy());
		}

	}
	public static WebDriver instance() throws SPOException{	
		WebDriver proxy = null;
		if(state==Constants.LifeCycleState.STARTED ||state==Constants.LifeCycleState.READY){			
			state=Constants.LifeCycleState.READY;
		}else{
			throw new ServiceLifeCycleException();
		}		
		if(runStrategy.reuseDriver && staticInstance!=null){
			return staticInstance;
		}
		
		WebDriver instance_1= null;
	//	WebDriver instance_2= null;
		log.trace("Instance with runStrategy browsername as :"+runStrategy.browserName);
		if(driverQ.size()<SessionContext.appConfig.DRIVER_POOL_SIZE){
			//firstCall = false;			
			log.trace("Q size of" +driverQ.size()+"  Within pool size, hence removing none");
		}else{
			driverQ.peek().quit();
			driverQ.poll();	
			log.trace("Exceeded pool size, removing one");
		}

		if(isHtmlUnit){
			instance_1= new HtmlUnitDriver(true) ;
		//	instance_2= new HtmlUnitDriver(true) ;
		}else if(isChrome){
			chromeOptions.addArguments("user-data-dir=C:/Users/user_name/AppData/Local/Google/Chrome/User Data");
			chromeOptions.addArguments("--start-minimized");
			instance_1= new ChromeDriver(chromeOptions);
			
		//	instance_2 = new ChromeDriver(capabilitiesChrome);
		}else if(isIE){
			instance_1 = new InternetExplorerDriver(capabilitiesIE);
		//instance_2 = new InternetExplorerDriver(capabilitiesIE);
		}else if(isFireFox){

			//			File file = new File("C:\\Program Files (x86)\\Mozilla Firefox\\browser\\extensions\\{972ce4c6-7e08-4474-a285-3208198ce6fd}.xpi");
			//			   FirefoxProfile firefoxProfile = new FirefoxProfile();
			//			   try {
			//				firefoxProfile.addExtension(file);
			//			} catch (IOException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
			//			   firefoxProfile.setPreference("extensions.firebug.currentVersion", "1.8.1"); // Avoid startup screen

			FirefoxProfile firefoxProfile = new ProfilesIni().getProfile("default");
			//			File pluginAutoAuth = new File("C:\\Program Files (x86)\\Mozilla Firefox\\autoauth-2.1-fx+fn.xpi");
			//			try {
			//				firefoxProfile.addExtension(pluginAutoAuth);
			//			} catch (IOException e) {				
			//				e.printStackTrace();
			//			}
			//firefoxProfile.setPreference(FireFoxPreferences., value)
			//FirefoxDriver  ffDriver =  new FirefoxDriver(firefoxProfile);
			//ffDriver.
			instance_1= new FirefoxDriver(firefoxProfile);
			//instance_2= new FirefoxDriver(firefoxProfile);
			//new FirefoxDriver(capabilitiesFF);

		}else{
			instance_1=new PhantomJSDriver(capabilitiesPhantom);
			//instance_2=new PhantomJSDriver(capabilitiesPhantom);
		}
		//proxy = new WebDriverProxy(instance_1);//No need for proxy at this point, may be explreo future use
		proxy=instance_1;
		driverQ.add(proxy);
		//log.error("Returning element "+POOL_SIZE/2+"/"+driverQ.size());		
		log.trace("Returning New element And adding to list of size: "+driverQ.size() );
		//return driverQ.get(Math.round(POOL_SIZE/2));
		log.trace("Driver reutrned is of type :"+proxy.toString());
		return proxy;


	}

	public static WebDriver instance_repeat(boolean continousMode, int pos){
		WebDriver toAdd = null;
		if(!continousMode){
			for(int i=0;i<SessionContext.appConfig.DRIVER_POOL_SIZE;i++) {
				driverQ.get(i).quit();
			}
		}
		if(driverQ.size()<SessionContext.appConfig.DRIVER_POOL_SIZE+1){
			for(int i=0;i<SessionContext.appConfig.DRIVER_POOL_SIZE;i++) {

				if(isHtmlUnit){
					toAdd= new HtmlUnitDriver(true) ;
				}else if(isChrome){
					toAdd = new ChromeDriver(capabilitiesChrome);
				}else if(isIE){
					toAdd= new InternetExplorerDriver(capabilitiesIE);
				}else{
					toAdd=new PhantomJSDriver(capabilitiesPhantom);
				}
				driverQ.add(toAdd);
			}
		}

		return driverQ.get(pos);
	}

	public static int count;


	

	public static void driverStepDown(){

	}

	public static void driverStepUp(){

	}



	public static  WebDriver instance_testMode() {
		boolean test1 = true;
		if(test1){
			if(isHtmlUnit){
				return new HtmlUnitDriver(true);
			}else if(isChrome){
				return new ChromeDriver(capabilitiesChrome);	
			}else if(isIE){
				return new InternetExplorerDriver(capabilitiesIE);
			}else{
				return new PhantomJSDriver(capabilitiesPhantom);
			}
		}else{
			WebDriver d = new PhantomJSDriver(capabilitiesPhantom);

			if(driverQ.size()>20){
				for(int i=0;i<10;i++){
					driverQ.peek().quit();
					driverQ.poll();	
				}

			}
			driverQ.add(d);

			return d;
		}

	}


	public synchronized static void stop() throws SPOException{
		if(state==Constants.LifeCycleState.STARTED || state==Constants.LifeCycleState.READY){			
			state=Constants.LifeCycleState.STOPPED;
		}else if(state==Constants.LifeCycleState.STOPPED ){
			log.error("Trying to stop a Stopped DriverFactory");
			return;
		}else{
		
			throw new ServiceLifeCycleException();
		}
		log.info("Stopping  instances: "+driverQ.size());
		for(int i = 0; i<driverQ.size();i++){
			try{
			driverQ.get(i).quit();
			}catch(UnreachableBrowserException e){
				//dONT CRASH STEST IN CASE FO STOP GETTING WEBDRIVER EXCEPTION, JUST LOG, PROCESS MONTIOR WILL COLLECT THE GARBAGE
				log.error("UNREACHABLE BROWSER EXCEPTION ENCOUTNERED WHILE STOPPING WEBDRIVER, WILL BE GARBAGE COLLECTED BY PROCESS MONITOR!!!");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		driverQ.clear();		
		if(runStrategy.cleanupDrivers){
			try{
				if(runStrategy.browserName.equals("ie")){
					RestrictedOSCmdRouter.taskKill("IEDriverServer.exe");
				}else if(runStrategy.browserName.equalsIgnoreCase("Phantom")){
					RestrictedOSCmdRouter.taskKill("Phantomjs.exe");
				}}catch(Exception e){
					log.error(e.getClass().getSimpleName()+" exception thrown while attempting to cleanup dirver process");
				}
		}
		//REsetting all 
				isHtmlUnit=false; 
				isIE=false;
				isFireFox = false;
				isChrome=false;
				capabilitiesPhantom = DesiredCapabilities.phantomjs();
				capabilitiesChrome = DesiredCapabilities.chrome();
				capabilitiesIE = DesiredCapabilities.internetExplorer();
				capabilitiesFF = DesiredCapabilities.firefox();
				runStrategy=null;
		log.debug("Stopped drivers , driverQ is "+driverQ.toString());

	}

	public static synchronized void init(RunStrategy strategy) throws SPOException{
		
		log = new Logger1("org.spo.fw.service.DriverFactory");
		//log = new Logger1(DriverFactory.class.getName());
		if(state==Constants.LifeCycleState.NULL || state==Constants.LifeCycleState.STOPPED ){
			log.debug("Init driver factory with "+strategy.browserName);
			runStrategy = strategy;
			state=Constants.LifeCycleState.STARTED;
		}else if (state==Constants.LifeCycleState.READY || state==Constants.LifeCycleState.STARTED){
			log.error("WARNING: DRIVER FACTORY REINITIALIZED !!! May be a nested test Init driver factory with "+strategy.browserName);
			//stop();
			runStrategy = strategy;			
			state=Constants.LifeCycleState.STARTED;
		}else{
			log.error("service life cycle exception");
			throw new ServiceLifeCycleException();
		}


		try {
			if(runStrategy.browserName.contains("html")){
				isHtmlUnit=true;
			}else if (runStrategy.browserName.contains("chrome")){
				isChrome=true;
				LoggingPreferences prefs = new LoggingPreferences();
				prefs.enable(LogType.BROWSER, Level.ALL);
				capabilitiesChrome.setCapability(CapabilityType.LOGGING_PREFS, prefs);
				configProxyCapabilities();

			}else if (runStrategy.browserName.contains("ie")){
				isIE=true;

				LoggingPreferences prefs = new LoggingPreferences();
				prefs.enable(LogType.BROWSER, Level.ALL);

				capabilitiesIE.setCapability(CapabilityType.LOGGING_PREFS, prefs);

				configProxyCapabilities();
			}else if (runStrategy.browserName.contains("firefox")){
				isFireFox=true;

				LoggingPreferences prefs = new LoggingPreferences();
				prefs.enable(LogType.BROWSER, Level.ALL);
				capabilitiesFF.setCapability(CapabilityType.LOGGING_PREFS, prefs);

				configProxyCapabilities();
			}


		} catch (Exception e) {

			e.printStackTrace();
		} 

		//java.util.logging.Logger.getLogger("org.openqa.selenium.phantomjs.PhantomJSDriverService").setLevel(java.util.logging.Level.SEVERE);
		capabilitiesPhantom.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,new String[]{ "--webdriver-loglevel=OFF" ,"--acceptSslCerts=True" ,"--web-security=no","--ignore-ssl-errors=yes"});
		capabilitiesPhantom.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,new String[]{ "--webdriver-loglevel=OFF","--web-security=no","--ignore-ssl-errors=yes" });
		//capabilitiesPhantom.setCapability(CapabilityType.SUPPORTS_ALERTS,true);
		capabilitiesPhantom.setCapability("phantomjs.page.settings.userAgent","Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
		capabilitiesPhantom.setCapability(CapabilityType.TAKES_SCREENSHOT, "false");
		capabilitiesPhantom.setCapability(CapabilityType.ACCEPT_SSL_CERTS, "True");
		capabilitiesIE.setCapability(InternetExplorerDriverService.IE_DRIVER_LOGFILE_PROPERTY, "C:/ie.log");
		if(strategy.reuseDriver && staticInstance==null){
		WebDriver instance_1= null;
		
			if(isHtmlUnit){
				instance_1= new HtmlUnitDriver(true) ;
			}else if(isChrome){
				chromeOptions.addArguments("user-data-dir=C:/Users/user_name/AppData/Local/Google/Chrome/User Data");
				chromeOptions.addArguments("--start-minimized");
				instance_1 = new ChromeDriver(chromeOptions);
				
			}else if(isIE){
				instance_1 = new InternetExplorerDriver(capabilitiesIE);
			}else if(isFireFox){
				FirefoxProfile firefoxProfile = new ProfilesIni().getProfile("default");
				instance_1= new FirefoxDriver(firefoxProfile);
			}else{
				instance_1=new PhantomJSDriver(capabilitiesPhantom);
			}
			staticInstance=instance_1;
		}
	}



}
