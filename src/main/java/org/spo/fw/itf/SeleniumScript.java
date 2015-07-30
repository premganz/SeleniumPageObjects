package org.spo.fw.itf;

import org.spo.fw.selenium.ScriptConstraint;


public interface SeleniumScript {
	
	public void init();
	public void startUp();
	public void execute() throws Exception; 	
	public void setScriptConstraint(ScriptConstraint constraint);
	public String getFailureMessage();
	public boolean isFailed();
}
