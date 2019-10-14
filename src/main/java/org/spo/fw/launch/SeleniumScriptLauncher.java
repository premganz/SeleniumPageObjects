/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.launch;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.selenium.ScriptContraintFactory;


public class SeleniumScriptLauncher {
	
	public static SeleniumScript launchScript(SeleniumScript script, RunStrategy strategy) throws SPOException{		
		BasicLauncher.launchAsSeleniumScript(strategy, script);
		return script;
	}

	
	//this will be called from INLINE from robot  or other script hence rely on same sEssionContext as the original runner 
	public static SeleniumScript executeSeleniumScriptInline(WebDriver driver, SeleniumScript script, List<String> scope_ids ){
		
		if(scope_ids!=null){
			script.setScriptConstraint(ScriptContraintFactory.getDefault().setWebDriver(driver).setWidgetScopeIds(scope_ids.toArray(new String[0])));
		}else{
			script.setScriptConstraint(ScriptContraintFactory.getDefault().setWebDriver(driver));
		}
			BasicLauncher.launchSeleniumScriptInline(SessionContext.snapshotStrategy(), script);
		return script;
	}
	
	
	
}
