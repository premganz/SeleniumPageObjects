package org.spo.fw.meta.fixture;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.spo.fw.config.AppConfig;
import org.spo.fw.config.Constants;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.DriverFactory;
import org.spo.fw.web.ServiceHub;


/**
 * We can use some system of App wide keyword bus, where all libraries could be plugged in.
This bus is responsible to delegate keyword call to appropriate implementation. It is stateful uses Webdriver Selenium
There are limitations to the way webDriver could be passed between objects hence static variables are part of 
this design.
See also a demo keyword in One of the plugged in libs that returns a command , that works. but it would not work if the plugged
in libraries are instantized for every call.
 */

public class StubKeyWords extends ServiceHub implements SessionBoundDriverExecutor{
	protected static Logger1 log = new Logger1("org.spo.fw.web.ServiceHub");

	protected String filePath_root_screens = System.getProperty("textScreens.path");
	private boolean isIe;
	
//	protected   WebDriver driver ;//TODO to rename this its too confusing with the impl.driver which is really used in queries
	
	

	/**
	 * Create is required and must be run as the first step in a ServiceHub test.
	 * This keyword sets up the ServiceHub. Sets the browser to emulate and the
	 * current test name (for internal ServiceHub logging).
	 *
	 * @param browser valid values include ie6, ie7, ie8, and ff36
	 * @param testName The name of the current testcase. Used for logging
	 * purposes.
	 */
	
	
	
	public void create(String browser, String testName) throws SPOException {
		RunStrategy strategy = new RunStrategy();		
		//1.INITIATING DEFAULT STRATEGY

		URL resourceUrl_page = getClass().getResource("/test_Script_Complex.html");
		String resourcePath_driver="";
		String resourcePath_page="";
		try {
			resourcePath_page = resourceUrl_page.toURI().getPath();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		
		strategy.textFilesPath="C:/works/spoworks/";
		strategy.driverPath=resourcePath_driver;
		strategy.isRecordMode=false;
		strategy.browserName="chrome";
		strategy.isProxyServerRequired=false;
		strategy.requireBasicAuthUrlPrefix=true;
		strategy.logLevel = Constants.LogLevel.DEBUG;
		strategy.appConfig = new AppConfig();
	//	if( (System.getProperties().containsKey("message-robot") && System.getProperty("message-robot").equals("dev"))){
			if(isIe){
				strategy.browserName="chrome";
				strategy.driverPath="";
				strategy.requireBasicAuthUrlPrefix=true;
				System.getProperties().put("webdriver.ie.driver", "");
				System.setProperty("webdriver.chrome.driver", "");
				
			}
	//	}
		
		
		SessionContext.publishStrategy(strategy);
		System.setProperty("phantomjs.binary.path", strategy.driverPath);		
		DriverFactory.init(strategy);
		driver = DriverFactory.instance();
		
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);

		init();
		
			driver.get("file://"+resourcePath_page);
			log.debug(getTitle());
			//printHtml();
			
		
		impl.setLog(log);

	}
	public boolean getIsIe() {
		return isIe;
	}

	public void setIsIe(boolean isIe) {
		this.isIe = isIe;
	}
	
	public void init(){
		super.init();
		factory=new StubPageFactoryImpl();		
		impl_page.init();
		
		
	}
	
	}
