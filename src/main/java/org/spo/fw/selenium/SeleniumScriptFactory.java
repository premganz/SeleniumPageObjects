package org.spo.fw.selenium;

import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.itf.SeleniumScript;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SeleniumScriptFactory implements ExtensibleService{

	protected String resourcePath="";

	@Override
	public void init() {
		resourcePath="ScriptRegistry.xml";

	}

	public SeleniumScript instance(String scriptName) {
		init();
		ApplicationContext factory = new ClassPathXmlApplicationContext(resourcePath);
		SeleniumScript obj = null;
		try{
			obj = (SeleniumScript) factory.getBean(scriptName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;



	}

}
