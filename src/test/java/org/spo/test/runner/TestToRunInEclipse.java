package org.spo.test.runner;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.service.DriverFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;


public class TestToRunInEclipse {

	StubKeyWords stubLibrary;
	Logger1 log = new Logger1("TestUtil_NavContainer");

	@Before
	public void setUp() throws Exception{
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("Test_ScriptRegistry.xml"));
		stubLibrary=(StubKeyWords)factory.getBean("stubKeyWords");
	}

	@After
	public void tearDown(){
		try {
			DriverFactory.stop();
		} catch (SPOException e) {
			e.printStackTrace();
		}
	}



		@Test
	public void navigateToBlankForms_loop() throws Exception{
		try{stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.navigateToUrlByName_loop("BlankFormsHome",stubLibrary );
		Thread.sleep(3000);
		log.debug(stubLibrary.getCurrentUrl());
		Assert.isTrue(stubLibrary.getCurrentUrl().contains("False"));
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
