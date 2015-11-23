package org.spo.fw.selenium;

import org.openqa.selenium.WebDriver;


public class ScriptContraintFactory {

	
	public static ScriptConstraint getDefault(){
		return new ScriptConstraint();
	}
	public static ScriptConstraint getDefault(WebDriver driver ){
		ScriptConstraint constraint= new ScriptConstraint();
		constraint.webDriver=driver;
		return constraint;
	}

}