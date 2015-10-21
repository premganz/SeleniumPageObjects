package org.spo.test.service;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.service.DriverFactory;
import org.spo.fw.service.external.ExternalScriptSvc;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

public class TestLib_SpecExt {
	Logger1 log = new Logger1(this.getClass().getSimpleName());	
	StubKeyWords stubLibrary;
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
	
	
	
	
	//Testing with multiple files 
	
	
	//@Test
	public void testTRSPyCGICalls() throws Exception{
		try{
			ExternalScriptSvc <String> remoteCache = new ExternalScriptSvc<String>();
			//String actualValue =remoteCache.queryTRS("getlatestdir.py");
			List<String> listSbValues=new ArrayList<String>();
			listSbValues.add("x");
			listSbValues.add("y");
			StringBuffer buf = new StringBuffer();
			for(String str:listSbValues){
				buf.append(str+",");
			}
			remoteCache.queryTRS("fsdfd?"+buf.toString());
			String actualValue = remoteCache.queryTRS("");
		log.debug("Latest dir is "+actualValue);
		Assert.isTrue(!"".equals(actualValue));
		}catch(Exception e){
			Assert.isTrue(false);
			e.printStackTrace();
			
			
		}
		
	}
	
	
//	@Test
//	public void navAfterChangingState() throws Exception{
//	//TODO
//		try{
//		
//			String actualValue =stubLibrary.page_event(page, stateExpression);
//		log.debug("Latest dir is "+actualValue);
//		Assert.isTrue(!"".equals(actualValue));
//		}catch(Exception e){
//			Assert.isTrue(false);
//			e.printStackTrace();
//			
//			
//		}
//		
//	}
}
