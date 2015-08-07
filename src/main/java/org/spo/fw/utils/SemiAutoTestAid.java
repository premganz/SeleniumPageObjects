package org.spo.fw.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.Select;
import org.spo.fw.web.KeyWords;

import com.gargoylesoftware.htmlunit.javascript.host.Selection;
//import org.spo.fw.utils.LabelNodeVisitor;

/**
 *OBSOLETE AS OF SPO 1.1.0 
 * 
 * 
 * 
 */
public class SemiAutoTestAid extends KeyWords {

	protected static final String HTTP = "http://";
	protected static final String SLASH = "/";

	private static Map<Character, Character> replacementMap = null;

	private static boolean debug;
	private static boolean debug1;
	private Map<String, String> pageDataStoreMap = new LinkedHashMap<String, String>();
	StringBuffer buf_master = new StringBuffer();
	private static final String TEXTBOX = "Text-Box";
	private static final String LISTBOX = "List-Box";
	private static final String CHECKBOX = "Check-Box";
	private static final String COMMA = ",";
	
	
	
	
	
	
	
	
	/****** USER DEFINED ***********/
	//COMMON KEYS
	
	//RECORD KEYS
	private static String url_toTest="";
	
	//RERUN KEY
	
	
	/****** USER DEFINED ***********/
	
	



	public String record_Start() throws Exception{
		//driver = new PhantomJSDriver();
		driver = new InternetExplorerDriver();
			buf_master =new StringBuffer();
			
			//rowSelect=3;
				goToPage(url_toTest);
				Thread.sleep(1000);
				Thread.sleep(1000);
				print1("==================================================================================");
				
				String url= getCurrentUrl();
				print2(url);
				Thread.sleep(4000);
				print1(getCurrentUrl());
				your_test_code();
				
				
				driver.quit();


		return "hi";
	}

	

	public String your_test_code() throws Exception{
		//driver = new PhantomJSDriver();

		for( int k=0;k<1;k++){		
			print1(driver.findElements(By.xpath("//input[@type=\"text\"]")).size()+"");
			
			
		}
		return "hi";
	}

	public  void print2(String x ){
		//System.out.println(x);
	}
	
	public static void main(String[] args) throws Exception {
		SemiAutoTestAid abs = new SemiAutoTestAid();
		try{
			

		
				abs.record_Start();	
				//abs.replay_Start(FILE_DATA_CSV , url_toTest, "TEST1");
				//abs.testAbstractPageRepopulate();
			
		}catch(Exception e){
			abs.print1(e.getMessage());
		}finally{
			//DriverFactory.cleanUp();
		}

	}

}