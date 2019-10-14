/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.runners;

import org.apache.commons.lang.StringUtils;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.itf.SeleniumScriptParametrized;
import org.spo.fw.launch.SeleniumScriptLauncher;
import org.spo.fw.launch.robot.CustomScriptEnabledLibrary;
import org.spo.fw.selenium.SeleniumScriptFactory;


/**
 * EXPERIMENTAL : AS OF NOW APPLICATION IS SINGLE THREADED, CONSIDERING IE DREIVERFACTORY IS SINGLETHREADED
 * @author prem
 *
 */

public class Node4_MultiThreadSeScript {
	private static boolean ignoreCustomStrategy=false;
	public static String lock=StringUtils.EMPTY;

	public static SeleniumScript launchScript(SeleniumScript script) throws SPOException{
		//Requires Firefox 14
		System.getProperties().put("webdriver.development", "true");//Requires Firefox 14 or 12
		RunStrategy strategy = new RunStrategy();		
		//1.INITIATING DEFAULT STRATEGY

		strategy.isRecordMode=false;
		strategy.browserName="phantom";
		strategy.isProxyServerRequired=false;
		strategy.requireBasicAuthUrlPrefix=false;

		//2.Templating in the injected strategy into a script.
		if(!ignoreCustomStrategy && script instanceof SeleniumScriptParametrized){

			SeleniumScriptParametrized scriptWithParams= (SeleniumScriptParametrized)script;
			if(scriptWithParams.getStrategyParams().containsKey("browserName")){
				strategy.browserName=scriptWithParams.getStrategyParams().get("browserName");
			}
			if(scriptWithParams.getStrategyParams().containsKey("requireBasicAuthUrlPrefix")){
				strategy.requireBasicAuthUrlPrefix=scriptWithParams.getStrategyParams().get("requireBasicAuthUrlPrefix").equals("1");
			}

		}

		//3.Override of both injected strategy and default strategy.
		//strategy.requireBasicAuthUrlPrefix=true;


		SessionContext.appConfig.eclipseMode= true; 
		SeleniumScriptLauncher.launchScript(script,strategy);
		SessionContext.appConfig.eclipseMode= false; 
		return script;
	}

	public static void main(String[] args) throws Exception {
		//TODO create script from args using Factory
		//SeleniumScript script =  new Script_CheckSaveAbstract_4();
		synchronized(lock){
			new Thread( new Runnable() {
				@Override
				public void run() {
					try {
						synchronized(Node4_MultiThreadSeScript.lock){
						//lock.wait();
						String scriptName= "Script_CheckSaveAbstract_5";
						CustomScriptEnabledLibrary lib = new CustomScriptEnabledLibrary();
						
						SeleniumScriptParametrized script = (SeleniumScriptParametrized) lib.runTemplateProcedure(scriptName,null);
						
						//lock.notify();
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			new Thread( new Runnable() {
				@Override
				public void run() {
					try{
						synchronized(Node4_MultiThreadSeScript.lock){
						//lock.wait();
						String scriptName= "Script_CheckSaveAbstract_4a";
						CustomScriptEnabledLibrary lib = new CustomScriptEnabledLibrary();
						SeleniumScriptParametrized script = (SeleniumScriptParametrized) lib.runTemplateProcedure(scriptName,null);
						launchScript(script);
						//lock.notify();
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();

		}
	}
}
