/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
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
		
		SeleniumScript obj = null;
		try{
			ApplicationContext factory = new ClassPathXmlApplicationContext(resourcePath);
			obj = (SeleniumScript) factory.getBean(scriptName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;



	}

}
