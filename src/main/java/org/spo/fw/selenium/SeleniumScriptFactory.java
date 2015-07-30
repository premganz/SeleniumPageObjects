package org.spo.fw.selenium;

import org.spo.fw.itf.SeleniumScript;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class SeleniumScriptFactory {
	public static SeleniumScript instance(String scriptName) {
	
			XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("ScriptRegistry.xml"));
			SeleniumScript obj = null;
			try{
			obj = (SeleniumScript) factory.getBean(scriptName);
			}catch(Exception e){
				e.printStackTrace();
			}
			return obj;

		 

	}

}
