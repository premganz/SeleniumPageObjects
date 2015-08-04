package org.spo.fw.selenium;

import org.spo.fw.itf.SeleniumScript;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SeleniumScriptFactory {
	public static SeleniumScript instance(String scriptName) {
	
			ApplicationContext factory = new ClassPathXmlApplicationContext("ScriptRegistry.xml");
			SeleniumScript obj = null;
			try{
			obj = (SeleniumScript) factory.getBean(scriptName);
			}catch(Exception e){
				e.printStackTrace();
			}
			return obj;

		 

	}

}
