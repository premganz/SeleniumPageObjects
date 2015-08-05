package org.spo.fw.runners;

import org.apache.commons.lang.StringUtils;
import org.spo.fw.config.Constants;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.itf.SeleniumScriptParametrized;
import org.spo.fw.launch.CustomScriptProvider;
import org.spo.fw.launch.SeleniumScriptLauncher;
import org.spo.fw.log.Logger1;

public class Node3_LaunchSeleniumScript implements ExtensibleService {
	protected boolean ignoreCustomStrategy;
	public static String lock=StringUtils.EMPTY;
	protected RunStrategy strategy;
	static Logger1 log = new Logger1("Node3_LaunchSeleniumScript");
	protected CustomScriptProvider customScriptProvider;
	@Override
	public void init() {
		customScriptProvider = new CustomScriptProvider();
		customScriptProvider.init();
		//1.INITIATING DEFAULT STRATEGY
		ignoreCustomStrategy=false;
		//strategy.isRecordMode=true;strategy.browserName="chrome";
		strategy.isRecordMode=false;strategy.browserName="ie";
		//strategy.isRecordMode=false;strategy.browserName="phantom";
		strategy.textFilesPath="C:\\spo_WorkingDir\\";//TODO Migrate to StubsCriptRunner
		strategy.isProxyServerRequired=false;
		strategy.requireBasicAuthUrlPrefix=true;
		//strategy.logLevel=Constants.LogLevel.TRACE;
		strategy.requireBasicAuthUrlPrefix=true;
	}

	public  void setSystemProps(){
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
		//1.INITIATING DEFAULT STRATEGY
		String browserName = System.getProperty("browserName");
		String logLevel = System.getProperty("logLevel");
		if(browserName==null  || "".equals(browserName) ){
			browserName="phantom";			
		}else{
			strategy.browserName=browserName;
		}
		
		if("TRACE".equals(logLevel)){
			strategy.logLevel=Constants.LogLevel.TRACE;
		}
	}


	
	
	public CustomScriptProvider getCustomScriptProvider() {
		init();
		return customScriptProvider;
	}

	public void setCustomScriptProvider(CustomScriptProvider customScriptProvider) {
		this.customScriptProvider = customScriptProvider;
	}

	public  SeleniumScript launchScript(SeleniumScript script) throws SPOException{
		init();
		setSystemProps();
		log.info("################## Running Test: "+script.getClass().getSimpleName()+"########################");

		//2.Templating in the injected strategy into a script.
		if(!ignoreCustomStrategy && script instanceof SeleniumScriptParametrized){

			SeleniumScriptParametrized scriptWithParams= (SeleniumScriptParametrized)script;
			scriptWithParams.init();
			if(scriptWithParams.getStrategyParams().containsKey("browserName")){
				strategy.browserName=scriptWithParams.getStrategyParams().get("browserName");
			}
			if(scriptWithParams.getStrategyParams().containsKey("requireBasicAuthUrlPrefix")){
				strategy.requireBasicAuthUrlPrefix=scriptWithParams.getStrategyParams().get("requireBasicAuthUrlPrefix").equals("1");
			}

		}

		//3.Override of both injected strategy and default strategy.


		SeleniumScriptLauncher.launchScript(script,strategy);
		return script;
	}

//	public static void main(String[] args) throws Exception {
//		while(true){
//			try{
//				int time = Calendar.getInstance().getTime().getHours();
//				System.out.println("Time is "+time);
//				//if(time==3){
//				System.setProperty("params.script03.pageName", "CreateHBIPSEventEditModePage");
//				System.setProperty("params.script03.toModifyPageName", "EventList");
//				System.setProperty("params.script03.stateExpression", "Search:by=EOC ID&criteria=DRAFT One of each HBIPS RX 1Q2015");
//				String scriptName= "Script_CheckSaveAbstract_5";
//				//scriptName= "Script_CheckSaveAbstract_4a";
//				//scriptName= "Script_CheckSaveAbstract_2_AT";
//				//scriptName= "Script_ControlsCatpure_03";
//				scriptName= "Script_BrokenLinksCheck_6";
//				//scriptName=args[0];
//				Node3_LaunchSeleniumScript launcher= new Node3_LaunchSeleniumScript();
//				launcher.launchScript((SeleniumScriptParametrized)SeleniumScriptFactory.instance(scriptName));
//				//break;
//				//}
//				Thread.sleep(600000);//10 min
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//
//		}
//	}
}