package org.spo.fw.config;

import java.util.concurrent.TimeUnit;

import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.util.SPODateUtils;

/**
 * 
 * @author prem
 * Some of the strategy properties show clear dependencies but for rare occassions have been kept as 
 * seperate preoperties. These could be combined in this Rules Class for all default purposes . 
 * However it si allowed to override from the runner 
 *
 */

public class StrategyRules {
static Logger1 log = new Logger1("org.spo.fw.config.StrategyRules");

public static RunStrategy apply(RunStrategy strategy){
	if("AT".equals(strategy.testEnv)){
		//strategy.requireBasicAuthUrlPrefix=false;
		strategy.browserName="ie";
	}
	
	//Browser Specific rules.
	log.debug("Applying strategy Rules Visible browser ");
	if(strategy.browserName.equalsIgnoreCase("phantom")){
		strategy.isVisibleBrowser=false;
	}else if(strategy.browserName.equalsIgnoreCase("firefox")){
		strategy.requireBasicAuthUrlPrefix=false;
		strategy.isVisibleBrowser=true;
	}else if(strategy.browserName.equalsIgnoreCase("ie")){
		if(SPODateUtils.getDateAsString("America/Chicago", "ddMMyy").matches(SPODateUtils.getApproxDateAsRegex("America/Chicago", "ddMMyy", 10, TimeUnit.DAYS))){
			//strategy.browserName="chrome";
			//strategy.cleanupDrivers=true;	
		}
		
		
		strategy.requireBasicAuthUrlPrefix=true;
		strategy.isVisibleBrowser=true;
	}else{
		strategy.isVisibleBrowser=true;
		
	}
	return strategy;
}
	
}
