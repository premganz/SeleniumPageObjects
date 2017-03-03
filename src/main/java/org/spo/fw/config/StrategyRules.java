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
protected static List<StrategyRule> rulesToApply = new ArrayList<StrategyRule>();



public static List<StrategyRule> getRulesToApply() {
	return rulesToApply;
}

public static void setRulesToApply(List<StrategyRule> rulesToApply) {
	StrategyRules.rulesToApply = rulesToApply;
}

public static RunStrategy apply(RunStrategy strategy){
	for(StrategyRule strat:rulesToApply){
		strategy= strat.modifyStrategy(strategy);
	}
	return strategy;
}


	
}
