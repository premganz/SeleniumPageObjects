package org.spo.fw.config;

import java.util.ArrayList;
import java.util.List;
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
private static List<StrategyRule> rulesToApply = new ArrayList<StrategyRule>();
private static List<String> loadedIds = new ArrayList<String>();



//public static List<StrategyRule> getRulesToApply() {
//	return rulesToApply;
//}
//
//public static void setRulesToApply(List<StrategyRule> rulesToApply) {
//	StrategyRules.rulesToApply = rulesToApply;
//}

public static void addRulesToStrategy(StrategyRule rule){
	if(loadedIds.contains(rule.getClass().getName())){
		return;
	}else{
		loadedIds.add(rule.getClass().getName());
		rulesToApply.add(rule);
	}
}

public static RunStrategy apply(RunStrategy strategy){
	for(StrategyRule strat:rulesToApply){
		strategy= strat.modifyStrategy(strategy);
	}
	return strategy;
}


	
}
