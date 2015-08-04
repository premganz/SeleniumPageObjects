package org.spo.fw.launch.robot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.itf.SeleniumScriptParametrized;
import org.spo.fw.launch.SeleniumScriptLauncher;
import org.spo.fw.navigation.svc.ApplicationNavModelGeneric;
import org.spo.fw.navigation.util.PageFactoryImpl;
import org.spo.fw.selenium.SeleniumScriptFactory;
import org.spo.fw.session.SessionContainer;
import org.spo.fw.web.KeyWords;

/**
 * A stateful Containter for Selenium Script, the script can be started by name , 
 * in various ways and parametrized using this facade
 * @author prem
 *
 */
public class CustomScriptEnabledLibrary extends KeyWords {
	//
	volatile SeleniumScript script ;

	public Map<String,String> outParams= new LinkedHashMap<String,String>();
	public Map<String,String> inParams= new LinkedHashMap<String,String>();
	protected SeleniumScriptFactory customScriptFactory;
	//public static  Web web;


	private void preProcessScript(String scriptName){
		script = customScriptFactory.instance(scriptName);
	}

	public void parametrizeScript( String key, String value){
		inParams.put(key, value);
	}

	public  SeleniumScript runTemplateProcedure(String scriptName,  List scope_ids){
		preProcessScript(scriptName);
		if(script instanceof SeleniumScriptParametrized){
			((SeleniumScriptParametrized) script).setInParamMap(inParams);
		}

		SeleniumScriptLauncher.executeSeleniumScriptInline(driver, script, scope_ids );
		if(script instanceof SeleniumScriptParametrized){
			outParams=((SeleniumScriptParametrized) script).getOutMap();
		}
		finalize();
		return script;
	}


	public  void launchSeleniumScript(String scriptName, List scope_ids){
		runTemplateProcedure(scriptName, scope_ids );
	}

	public  void launchSeleniumScriptUnscoped(String scriptName){
		runTemplateProcedure(scriptName, null );
	}

	public  void launchSeleniumScriptInContext(String scriptName){
		runTemplateProcedure(scriptName, null );
	}


	public  String postProcessScript( String outputName){
		return outParams.get(outputName);
	}

	public void finalize(){
		inParams.clear();outParams.clear();script=null;
	}

	public void printSessionLog(){
		log.debug(SessionContainer.printSessionLog());

	}

	@Override
	public  void init() {		
		setFactory(new PageFactoryImpl());
		setNavModel(new ApplicationNavModelGeneric());
		customScriptFactory=new SeleniumScriptFactory();
		super.init();
	}
}
