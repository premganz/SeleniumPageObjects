package org.spo.test.service;

import org.spo.fw.itf.SeleniumScript;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class StubSeleniumScriptFactory {
	public static SeleniumScript instance(String scriptName) {
	
			XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("Test_ScriptRegistry.xml"));
			SeleniumScript obj = (SeleniumScript) factory.getBean(scriptName);
			return obj;

		 

	}

}
