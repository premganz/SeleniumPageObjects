package org.spo.fw.launch;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.SeleniumScript;
import org.spo.fw.selenium.ScriptContraintFactory;


public class LaunchSeleniumScript {
	
	public static SeleniumScript launchScript(SeleniumScript script, RunStrategy strategy) throws SPOException{
		constrainScript(script);
		BasicLauncher.launchAsSeleniumScript(strategy, script);
		return script;
	}

	
	private static  void  constrainScript(SeleniumScript script){		
		script.setScriptConstraint(ScriptContraintFactory.instance(script.getClass().getSimpleName()));
	}
	
	//this will be called from Robot hence rely on StrategyNoticeBoard
	public static SeleniumScript executeSeleniumScriptFromRobot(WebDriver driver, SeleniumScript script, String start_url, List<String> scope_ids ){
		
		if(scope_ids!=null){
			script.setScriptConstraint(ScriptContraintFactory.getDefault().setStartUrl(start_url).setWebDriver(driver)
					.setWidgetScopeIds(scope_ids.toArray(new String[0])));
		}else{
			script.setScriptConstraint(ScriptContraintFactory.getDefault().setStartUrl(start_url).setWebDriver(driver));
		}
			BasicLauncher.launchSeleniumScriptFromRobot(SessionContext.snapshotStrategy(), script);
		return script;
	}
	
	
	
}
