package org.spo.test;


import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.navigation.itf.MultiPage;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.itf.NavLink;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.service.DriverFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;


public class TestUtil_NavContainer {

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
	public void navigateToPage() throws Exception{
		stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.navigateToUrlFromHome("https://en.wikiquote.org/wiki/Main_Page",stubLibrary );
		Assert.isTrue(stubLibrary.getCurrentUrl().contains("https://en.wikiquote.org/wiki/Main_Page"));

	}



	@Test
	public void navigateToPageName() throws Exception{
		stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.navigateToUrlByName("Culture",stubLibrary );
		Thread.sleep(3000);
		Assert.isTrue(stubLibrary.getCurrentUrl().replaceAll(":","").contains("Culture"));

	}
	//@Test FIXME
	public void navigateToANYPage() throws Exception{
		String stateExpression = "N:url="+"https://en.wikipedia.org/wiki/Portal:Contents/Portals#Culture_and_the_arts";
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.getDefaulModel().changePageState("ANY", stateExpression);
		container.navigateToUrlByName("ANY",stubLibrary );		
		Thread.sleep(3000);
		log.debug("current url was "+stubLibrary.getCurrentUrl());
		Assert.isTrue(stubLibrary.getCurrentUrl().contains("PopulationChanges"));

	}

	//@Test
	public void navigateToReport() throws Exception{
		try{stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.navigateToUrlByName("Overview",stubLibrary );
		Thread.sleep(3000);
		Assert.isTrue(stubLibrary.getCurrentUrl().contains("Overview"));}
		catch(Exception e){
			e.printStackTrace();Assert.isTrue(false);
		}

	}
	//@Test
	public void navigateToForms_negative() throws Exception{
		try{stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.navigateToUrlByName("ovv",stubLibrary );
		Thread.sleep(3000);
		}catch(NavException e ){
			e.printStackTrace();Assert.isTrue(true);
		}catch (Exception e) {
			e.printStackTrace();Assert.isTrue(false);
		}

	}

	//@Test
	public void navigateToForms_positive() throws Exception{
		try{stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.navigateToUrlByName("ovv2",stubLibrary );
		Thread.sleep(3000);
		log.debug(stubLibrary.getCurrentUrl());
		Assert.isTrue(stubLibrary.getCurrentUrl().contains("False"));
		}catch(NavException e ){
			e.printStackTrace();Assert.isTrue(false);
		}catch (Exception e) {
			e.printStackTrace();Assert.isTrue(false);
		}

	}

	@Test
	public void navigateToForms_loop() throws Exception{
		try{stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.navigateToUrlByName_loop("ovv3",stubLibrary );
		Thread.sleep(3000);
		log.debug(stubLibrary.getCurrentUrl());
		//Assert.isTrue(stubLibrary.getCurrentUrl().contains("False"));
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	//	@Test
	public void navigateToBlankForms_loop_1() throws Exception{
		try{stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		container.navigateToUrlByName("BlankFormsHome",stubLibrary );
		MultiPage multiPage = container.getMultiPage("BlankFormsHome");
		List<NavLink> navLinks = multiPage.getSubLinks();
		for(NavLink link: navLinks){
			multiPage.followLink(link, stubLibrary);
			log.debug("reached "+stubLibrary.getCurrentUrl());
			Thread.sleep(1000);
			multiPage.followLink(multiPage.getLoopBackLink(), stubLibrary);
			log.debug("reached "+stubLibrary.getCurrentUrl());
		}

		log.debug("reached "+stubLibrary.getCurrentUrl());
		container.navigateToUrlByName_loop("BlankFormsHome",stubLibrary );
		Thread.sleep(3000);
		log.debug(stubLibrary.getCurrentUrl());
		//Assert.isTrue(stubLibrary.getCurrentUrl().contains("False"));
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test_QueryAppDocModel() throws Exception {
		try{
		stubLibrary.goToPage(SessionContext.appConfig.URL_UNIT_TEST_MODE);
		log.debug(stubLibrary.getCurrentUrl());
		ApplicationNavContainerImpl container = new ApplicationNavContainerImpl();
		container.init();
		Assert.isTrue(container.queryAppDocModel("//page[@name='ReportsOverview']/nav/page", "name").size()>2);
		}catch(Exception e){
			e.printStackTrace();
			Assert.isTrue(false);
		}
		
	}
}
