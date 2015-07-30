package org.spo.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.spo.fw.log.Logger1;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.navigation.util.PageFactoryImpl;
import org.spo.fw.shared.DiffMessage;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.Lib_PageLayout_Processor;
import org.spo.fw.utils.pg.Lib_PageLayout_Content.FileContent;
import org.spo.fw.utils.pg.Lib_PageLayout_Content.PageContent;
import org.spo.fw.web.Lib_PageLayoutCheck;
import org.spo.fw.web.KeyWords;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;



public class TestLib_PageLayoutCheck {
	KeyWords robot = new KeyWords();		
Logger1 log = new Logger1("TestLib_PageLayoutCheck");

	public void setUp() throws Exception{}

	public void tearDown(){}

	@Test
	public void testExceptionReporting() throws Exception{
		try{
			throw new NullPointerException();
		}catch(Exception e){
			log.error(e);
		}

	}
	
	@Test
	public void testDiff() throws Exception{
		try{
			  // read in lines of each file
	   
		}catch(Exception e){
			log.error(e);
		}

	}
}
