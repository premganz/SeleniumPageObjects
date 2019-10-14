/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.spo.fw.log.Logger1;


public class SeleniumCommand {
	/*
	 * Encapsculates a selenium command
	 * 1. It exposes methods that internally calls on the selenium webdriver.
	 * 2. The client of the command adds some commands to this , which are stacked
	 * 3. Invoker then, Waits for a lock on "App under test state" The commands and then invoked and results logged. 
	 *
	 *  
	 */
	protected static Logger1 log = new Logger1("org.spo.fw.selenium.SeleniumCommand");
	
	private SeleniumCommand.types  type;
	//control to update
	private String category;
	private String partialId;
	private String value_toUpdate;//value to update
		
	//Verify measure value
	private String measureName;
	private String message;
	
	private String javaScript;//script to execute
	
	public static enum types {UPDATE_CONTROL,INVOKE_JAVASCIPT, VERIFY_MEASURE, RELOAD_PAGE}
	
	private boolean cacheMode=true;
	
	
	
	public SeleniumCommand executeJavaScript(String script){
		this.javaScript=script;
		this.type=types.INVOKE_JAVASCIPT;
		return this;
		
	}
	
	public SeleniumCommand findMeasureMessageValue(String measureName, String message){
		this.measureName=measureName;
		this.message=message;
		this.type=types.VERIFY_MEASURE;
		return this;
		
	}
	
	public SeleniumCommand reloadPage(){
		this.type=types.RELOAD_PAGE;
		return this;
		
	}
	
	public void execute(WebDriver driver){
		
		if(type.equals(types.UPDATE_CONTROL)){}else if (type.equals(types.VERIFY_MEASURE)){}else if(type.equals(types.INVOKE_JAVASCIPT)){
			//log.debug("Exec javascript"+getCurrentUrl(driver));
			executeJavaScript(driver,javaScript);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}else if(type.equals(types.RELOAD_PAGE)){}
		
		
		
	}
	
	
	public String getCurrentUrl(WebDriver driver){
		String url = driver.getCurrentUrl();
		return url;
	}
	
	
	public Object executeJavaScript(WebDriver driver, String x) {
		try{
		return ((JavascriptExecutor)driver).executeScript(x);
		}catch(Exception e){
			log.debug("Error in executing java script "+x+" in page "+driver.getCurrentUrl());
			
			log.debug("Retrying  ");
			return null;
		}
		
	}
	
	public void refresh(WebDriver driver) {
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver)
			.executeScript("javascript:location.reload(true);");
		}

		
	}
	
	public String toString(){
		return "CommandType "+this.type;
	}
	
	
	

}
