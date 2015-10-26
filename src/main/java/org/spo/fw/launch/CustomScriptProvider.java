package org.spo.fw.launch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.spo.fw.config.SessionContext;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.itf.SeleniumScriptParametrized;
import org.spo.fw.launch.SeleniumScriptLauncher;
import org.spo.fw.navigation.svc.ApplicationNavModelGeneric;
import org.spo.fw.navigation.util.PageFactoryImpl;
import org.spo.fw.selenium.JunitScript;
import org.spo.fw.selenium.SeleniumScriptFactory;
import org.spo.fw.session.SessionContainer;
import org.spo.fw.web.ServiceHub;

/**
 *Provides instances of customscripts from a spring based factory (which can be injeted dynamically to this provider with modifying the xml locaiton).
 *The customscripts extend junit script and can  be executed here and the results are actually executd customScripts loaded with outparams and 'failed' variable
 *based on these it is up to the calling script to validate the test.
 *
 *The Provider hence 
 *a) is a wrapper for the factory
 *b) Allows for launching the script in new contxt or incontext.
 *c) Allows pre launch constraining/parametrizing the script.
 *
 *Since this is launched from a junit environment as against the CustomSCriptEnabledLibrary intended for RobotFramework, 
 *it does not extend Keywords, 
 *Instead the caller of this Script, the @Test via the JunitScript can inject kw into this.
 *
 * @author prem
 *
 */
public class CustomScriptProvider  implements ExtensibleService {
	//
	volatile SeleniumScript script ;

	public Map<String,String> outParams= new LinkedHashMap<String,String>();
	public Map<String,String> inParams= new LinkedHashMap<String,String>();
	protected SeleniumScriptFactory customScriptFactory;
	//public static  Web web;


	public SeleniumScript getScript(String scriptName){
		init();
		script = customScriptFactory.instance(scriptName);
		return script;
	}

	public void parametrizeScript( String key, String value){
		inParams.put(key, value);
	}

	public  SeleniumScript runTemplateProcedure(String scriptName,  List scope_ids, ServiceHub kw){
		init();
		getScript(scriptName);
		if(script instanceof SeleniumScriptParametrized){
			((SeleniumScriptParametrized) script).setInParamMap(inParams);
		}
		if(kw!=null){
		SeleniumScriptLauncher.executeSeleniumScriptInline(kw.getDriver(), script, scope_ids );
		}else{
			SeleniumScriptLauncher.launchScript(script, SessionContext.snapshotStrategy());
		}
		if(script instanceof SeleniumScriptParametrized){
			outParams=((SeleniumScriptParametrized) script).getOutMap();
		}
		finalize();
		return script;
	}


	public  SeleniumScript launchSeleniumScriptConstrained(String scriptName, List scope_ids){
		return runTemplateProcedure(scriptName, scope_ids,null );
	}

	public  SeleniumScript launchSeleniumScript(String scriptName){
		return runTemplateProcedure(scriptName, null , null);
	}

	public  SeleniumScript launchSeleniumScriptInContext(String scriptName, ServiceHub kw){
		return runTemplateProcedure(scriptName,null,  kw);
	}


	public  String postProcessScript( String outputName){
		return outParams.get(outputName);
	}

	public void finalize(){
		inParams.clear();outParams.clear();script=null;
	}



	@Override
	public  void init() {
		customScriptFactory=new SeleniumScriptFactory();
	}
}
