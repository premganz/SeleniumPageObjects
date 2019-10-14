/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.itf;

import org.spo.fw.config.RunStrategy;
import org.spo.fw.selenium.ScriptConstraint;
import org.spo.fw.selenium.ScriptType;
import org.spo.fw.web.ServiceHub;


public interface SeleniumScript {
	
	public void init();
	public RunStrategy customizeStrategy(RunStrategy strategy);
	public void startUp();
	public void execute() throws Exception; 	
	public void setScriptConstraint(ScriptConstraint constraint);
	public String getFailureMessage();
	public boolean isFailed();
	public ServiceHub getKw();
	public ScriptType getScriptType();
}
