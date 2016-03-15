package org.spo.fw.itf;

import java.util.Map;

/**
 * The characteristics of SeleniumScript parametrized to proide handle across the application as to what it could do
 * and what can be expected out of it.
 * @author prem
 *
 */

public interface SeleniumScriptParametrized extends SeleniumScript{
	public void setInParam(String inKey, String inValue);
	public Map <String,String> getInParams();
		
	public void setStrategyParams(Map<String, String> inParam);
	public  Map<String, String> getStrategyParams();
		
	public Map<String, String> getOutMap();
	public String getOutParam(String outKey);
}

