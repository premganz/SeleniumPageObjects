package org.spo.fw.selenium;

import org.openqa.selenium.WebDriver;

public class ScriptConstraint {
	
	public int timer=0;//For Timed Scripts.(0 is indefinite)
	public String startUrl;//Where to start the Execution
	public String[] widgetScopeXpaths;//Only these webelements are meant to be tested ,( null is all)
	public String[] widgetScopeIds;//Only these webelements are meant to be tested ,( null is all)
	public WebDriver webDriver;
	
		public ScriptConstraint setTimer(int timer){
			this.timer= timer;
			return this;
		}
	
		public ScriptConstraint setStartUrl(String url){
			this.startUrl= url;
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
