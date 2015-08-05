package org.spo.test;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spo.fw.exception.SPOException;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.service.DriverFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

public class TestLib_CoreKeyWords {
			
	StubKeyWords stubLibrary;
	@Before
	public void setUp() throws Exception{
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("Test_ScriptRegistry.xml"));
		stubLibrary=(StubKeyWords)factory.getBean("stubKeyWords");
		stubLibrary.create("","");
	}
	
	@After
	public void tearDown(){
		try {
			DriverFactory.stop();
		} catch (SPOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	//Testing with multiple files 
	
	@Test
	public void testEnterTextById() throws Exception{
		try{
		String valueToEnter = "abcde";
		stubLibrary.enterText("author_id",valueToEnter);
		String actualValue = stubLibrary.getValue("author_id");
		Assert.isTrue(valueToEnter.equals(actualValue));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	@Test
	public void testEnterTextByXPath() throws Exception{
		try{
		String valueToEnter = "abcde";
		stubLibrary.enterText(".//input[@id='gen_author_id']",valueToEnter);
		String actualValue = stubLibrary.getValue("gen_author_id");
		Assert.isTrue(valueToEnter.equals(actualValue));
		}catch(Exception e){
			e.printStackTrace();
			Assert.isNull(e);
		}
		
	}
	
	//@TestTODO
	public void testCSSSelector() throws Exception{
		try{
		
		String y = (String)stubLibrary.executeJavaScript("return document.querySelector('#opt_ghostwriter_req[original_label_text]').getAttribute('value')");
		Assert.isTrue(y==null);
		}catch(Exception e){
			e.printStackTrace();
			Assert.isNull(e);
		}
		
	}

	@Test
	public void testClear() throws Exception {
		String actualValue = "test";
		try {
			String valueToEnter = "abcde";
			stubLibrary.enterText(".//input[@id='gen_author_id']",valueToEnter);
			actualValue = stubLibrary.getValue("gen_author_id");
			Assert.isTrue(valueToEnter.equals(actualValue));
			stubLibrary.clear(".//input[@id='gen_author_id']");
			actualValue = stubLibrary.getValue("gen_author_id");

		}
		catch(Exception e){
			e.printStackTrace();
		}
		Assert.isTrue("".equals(actualValue));
	}
	
	@Test
	public void testClick_caseInsensitive() throws Exception {
		String actualValue = "test";
		boolean b = false;
		try {
		stubLibrary.click("click to Submit1");
		b = stubLibrary.pageShouldContain("Form has been submitted");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Assert.isTrue(b);
	}
	
	
	
}
