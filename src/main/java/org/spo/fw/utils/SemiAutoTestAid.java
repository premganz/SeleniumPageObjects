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
 * 
 * 
 * This class extends the KeyWords library that is based on Selenium and introduced in 2.0.0. 
 * 
 * THIS SCRIPT RECORDS THE DATA ON A SCREEN FOR A GIVEN URL, STORES TO A FILE OF CHOICE, 
 * optional: IT THEN PERFORMS AN OPTIONAL SAVE OPERATION
 * optional: NAVIGATES TO ANOTHER SCREEN and verifies recorded data expected for that screen. 
 * 
 * This script is configurable with
 * 
 * 1. Target url
 * 2. File format to store
 * 3. Save button operation
 * 4. NExt navigation
 * 5. File to compare
 * 
 * BUT FORGET ALL THE FEATURES, THIS WILL MOSTLY BE USED FOR GENERATING ID AND VALUE KEYWORDS FOR ROBOT FOR A GIVEN URL.
 * 
 * 
 *
 * Works only with ie
 * There are at several places runtime exceptions eaten simply, reason being the unpredictability of ids of html tags and as work
 * around to handle alerts, guess excusable in tool code.
 * There are at many pleases thread introduced to get it working.
 * The list of widgets got by calling By.tagName is not directly looped but rather stored to a new list of ids and then looped 
 * to solve a problem  caused by time taken in looping causing staleness.  
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