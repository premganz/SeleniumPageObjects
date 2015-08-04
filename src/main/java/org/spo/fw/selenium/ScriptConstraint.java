package org.spo.fw.selenium;

import org.openqa.selenium.WebDriver;
import org.spo.fw.config.SessionContext;
/**
 * 
 * @author premganesh
 * This is an optional constraint model that could be injected into any SeleniumsCript
 * 
 * At this point it helps to
 * a) make hot transfer of the webDriver if the script is called from a different context, such as from a RobotScript.
 * b) make inclusions and exclusions of controls in cases where the coverage is cross screens and across applications
 * c) A timer- UNTESTED as of now
 *  
 *
 */
public class ScriptConstraint {

	public int timer=0;//For Timed Scripts.(0 is indefinite)
	public String[] widgetScopeXpaths;//Only these webelements are meant to be tested ,( null is all)
	public String[] widgetScopeIds;//Only these webelements are meant to be tested ,( null is all)
	public WebDriver webDriver;

	public ScriptConstraint setTimer(int timer){
		this.timer= timer;
		SessionContext.appConfig.WEBDRIVER_TIMEOUT=timer;
		return this;
	}


	public ScriptConstraint setWidgetScopeXpaths(String[] xpaths){

		this.widgetScopeXpaths= xpaths;
		return this;
	}

	public ScriptConstraint setWidgetScopeIds(String[] ids){

		this.widgetScopeIds= ids;
		return this;
	}
	public ScriptConstraint setWebDriver(WebDriver driver){

		this.webDriver= driver;
		return this;
	}
}
