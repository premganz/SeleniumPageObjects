package org.spo.fw.web;

import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.spo.fw.config.Constants;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.ServiceLifeCycleException;
import org.spo.fw.exception.UnPrivilagedOperationException;
import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.KeyWordCommand;
import org.spo.fw.itf.PluggableRobotLibrary;
import org.spo.fw.log.Logger1;
import org.spo.fw.selenium.ScriptConstraint;
import org.spo.fw.service.DriverFactory;
import org.spo.fw.service.RestrictedOSCmdRouter;
import org.spo.fw.specific.scripts.common.Script_ControlsToMap_04;
import org.spo.fw.utils.HtmlSimplificationNodeVisitor;
import org.spo.fw.utils.TextNodeVisitor;


/**
 * This class offers RobotFramework the ability to access and interact with web
 * pages through request/response. It emulates a browser. This Library is
 * stateful, meaning the current page and its state is cached upon each keyword
 * that changes the page's state. Use "Create" then "Go To Page" to start
 * navigation.
 */

public class Lib_KeyWordsSpecific  implements PluggableRobotLibrary{


	protected String filePath_root_screens = System.getProperty("textScreens.path");
	private   WebDriver driver ;//TODO make private
	private String user;
	private String password;
	protected static Logger1 log = new Logger1("org.spo.fw.web.KeyWordsImpl") ;

	public static void setLog(Logger1 log) {
		Lib_KeyWordsSpecific.log = log;
	}
	
	public Lib_KeyWordsSpecific(){
		
	}
	

	
	public Lib_KeyWordsSpecific(WebDriver driver){
		super();
		this.initLibrary(driver);
	}
	public void initLibrary(WebDriver driver){
		this.driver = driver;
	}
		



	///FIXME :Failing concurrent.layout.txt
	public void buttonNameShouldEqual(String nameOrId, String expectedValue) {
		WebElement elem = new Util_WebElementQueryHelper(driver).querySilent(nameOrId);
		if (elem == null) {//FIXME recheck this method is kind of redundant carreid fromward from htmlunit
			//attributeShouldEqual("input", "value", expectedValue) ;
			if(driver.findElement(By.xpath(".//input[@value='"+expectedValue+"']")).getAttribute("value").equals(expectedValue));
			return;
		}

		String value = elem.getAttribute("Value");
		if (value.equals(expectedValue)) {
			return;
		}else{
			throw new AssertionError("The following text was not found on the button: "+ expectedValue);	
		}

	}

	


	

	

	public String getNumberOfRowsFromTable(String nameOrId) {
		WebElement table =  new Util_WebElementQueryHelper(driver).queryFailFast(nameOrId);
		return Integer.toString(table.findElements(By.tagName("tr")).size());
	}

	public String getNumberOfRowsFromTableByXpath(String xpath) {
		List<WebElement>  lstElem = driver.findElements(By.xpath(xpath));
		return Integer.toString(lstElem.size());

	}


	

	

	


	public String  getTableColumnCount(String nameOrId) {

		WebElement table = new Util_WebElementQueryHelper(driver).queryFailFast(nameOrId);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		int maxCol =0;
		for (WebElement row: rows){
			if(maxCol<row.findElements(By.tagName("td")).size())
				maxCol = row.findElements(By.tagName("td")).size();
		}		
		return String.valueOf(maxCol);

	}


	public String  trimCleanString(String input, String replaceable){
		String output = input.trim();
		output = output.replaceAll(replaceable, "");
		output = output.replaceAll("[\\n]",StringUtils.EMPTY);
		String clean = output.replaceAll("\\P{Print}", "");
		print (clean);
		return clean;

	}
	public String  cleanString(String input){
		String clean = input.trim().replaceAll("\\P{Print}", "");
		print (clean);
		return clean;

	}

	
	



	//New


	//TODO keep or drop, if ever gets used.
	public String getRecordFromTable(String tableId, String regexSelector){

		WebElement elem = new Util_WebElementQueryHelper(driver).queryFailFast(tableId);
		List<WebElement> recordList = elem.findElements(By.cssSelector("itemRow"));

		for(int i = 0; i< recordList.size();i++ ){
			WebElement elem_record = recordList.get(i);
			String parsedText = Jsoup.parse(elem_record.getText()).text();
			Pattern pat =Pattern.compile(regexSelector);
			Matcher mat = pat.matcher(parsedText);
			if(mat.find()){

			}
		}

		return "";
	}


	public void print(String x ){
		log.debug(x);
	}

	//TODO: To move to specific file, this is very specific quirky for abstract pages.
	public List<String> getCheckBoxValuesList(String id, String measure){
		List<WebElement> inputList = driver.findElements(By.tagName("input"));
		//StringBuffer buf = new StringBuffer();
		List<String> allowedVals= new ArrayList<String>();
		//allowedVals.add("");// fix to bug that caused more list items 
		for (WebElement w :  inputList){
			String type = w.getAttribute("type");
			String submitName = w.getAttribute("submitName");
			if(w.getAttribute("name").contains(id) && !w.getAttribute("name").contains("utd") && type.equals("checkbox") ){
				WebElement parent = w.findElement(By.xpath(".."));
				print("hi  "+w.getAttribute("name"));
				print(parent.getAttribute("innerHTML"));
				String cbText = parent.findElement(By.tagName("label")).getText();
				allowedVals.add(cbText);
			}
		}
		print(allowedVals.toString());
		return allowedVals;


	}

	




	public String getTextFromTagWithId(String nameOrId) {
		WebElement elem = new Util_WebElementQueryHelper(driver).queryFailFast(nameOrId);
		return elem.getText();
	}

	public  void print1(String x ){	
		System.err.println(KeyWords_Utils.obfuscate(x));
	}


	

	
	public String getTimeStamp (){
		return new Date().toString();
		
	}

	
	
}
