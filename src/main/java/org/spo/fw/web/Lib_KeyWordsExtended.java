/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
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

public class Lib_KeyWordsExtended  extends  Lib_KeyWordsCore{


	protected String filePath_root_screens = System.getProperty("textScreens.path");
	private   WebDriver driver ;//TODO make private
	private String user;
	private String password;
	protected static Logger1 log = new Logger1("org.spo.fw.web.KeyWordsImpl") ;

	public static void setLog(Logger1 log) {
		Lib_KeyWordsExtended.log = log;
	}
	
	public Lib_KeyWordsExtended(){
		
	}
	

	
	public Lib_KeyWordsExtended(WebDriver driver){
		super();
		this.initLibrary(driver);
	}
	public void initLibrary(WebDriver driver){
		this.driver = driver;
	}
		
		

	private boolean isElementSelected(String nameOrId) throws  Exception{
		WebElement elem =  new Util_WebElementQueryHelper(driver).query(nameOrId);
		if (elem.isSelected()){
		return true;
		}
		return false;
	}
	
	//TODO
	public void radioButtonShouldBeSelected(String nameOrId) throws  Exception {
		if (!isElementSelected(nameOrId)){
			throw new AssertionError("radio with  "+nameOrId+"is not selected");
		}
	}
	//TODO
	public void radioButtonShouldNotBeSelected(String nameOrId) throws  Exception {
		if (isElementSelected(nameOrId)){
			throw new AssertionError("radio with  "+nameOrId+"is  selected");
		}
	}

	//TODO
	public void checkBoxShouldBeSelected(String nameOrId) throws  Exception {
		if (!isElementSelected(nameOrId)){
			throw new AssertionError("Checkbox with  "+nameOrId+"is not selected");
		}
	}

	//TODO
	public void checkBoxShouldNotBeSelected(String nameOrId) throws  Exception{
		if (isElementSelected(nameOrId)){
			throw new AssertionError("Checkbox with  "+nameOrId+"is  selected");
		}
	}





	public String getCellFromTable(String nameOrId, int row, int column) throws  Exception {
		WebElement cell = getHtmlTableCellFromTable(nameOrId, row,
				column);
		if (cell == null) {
			throw new AssertionError("Cell in table with name or id ["
					+ nameOrId + "] at [" + row + "," + column + "] not found.");
		}
		for (int i = 0; i < 10; i++) {
			if (cell.getText() != null) {
				break;
			} else {
				//				try {
				//					Thread.sleep(500);
				//				} catch (InterruptedException ex) {
				//					java.util.logging.Logger.getLogger(ServiceHub.class.getName()).log(Level.SEVERE, null, ex);
				//				}
			}
		}
		final String textContent = cell.getText();
		if (textContent == null) {
			return null;
		}
		return  KeyWords_Utils.findAndReplaceNonAsciiChars(textContent.toCharArray());
	}



	protected WebElement getHtmlTableCellFromTable1(String nameOrId, int row,	int column) throws  Exception{
		WebElement table =  new Util_WebElementQueryHelper(driver).query(nameOrId);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement row1=rows.get(row);
		List<WebElement> cells = row1.findElements(By.tagName("td"));
		return cells.get(row);
	}
	protected WebElement getHtmlTableCellFromTable(String nameOrId, int row,			int column) throws  Exception {
		WebElement table =  new Util_WebElementQueryHelper(driver).query(nameOrId);
		WebElement cell = getCellAt(nameOrId, row-1, column-1);
		return cell;
	}


	

	public String getTagAttributeFromTableCell(String tableNameOrId, int row,
			int column, String tagName, String attribute)throws  Exception {
		WebElement cell = getHtmlTableCellFromTable1(tableNameOrId, row,
				column);
		Iterable<WebElement> children = cell.findElements((By.xpath("*")));
		String attributeValue = null;
		for (WebElement child : children) {
			if (child.getTagName().equalsIgnoreCase(tagName)) {
				attributeValue = child.getAttribute(attribute);
			}
		}
		return attributeValue;
	}


	protected WebElement queryElement(String id,String tag){
		return driver.findElement(By.id(id));	
	}


	//TODO To recheck
	public void labelAttributeShouldEqual(String forValue, String attributeName, String expectedValue) {

		List<WebElement> elements= driver.findElements(By.tagName("label"));
		//List<DomElement> elements = currentPage.getElementsByTagName("label");
		for (WebElement element : elements) {
			if (forValue.equals(element.getAttribute("for"))) {
				if (expectedValue.equals(element.getAttribute(attributeName))) {
					return;
				} else if (attributeName.equals("text")
						&& element.getText().equals(expectedValue)) {
					return;
				}
			}
		}
		throw new AssertionError("No [label] for [" + forValue		+ "] with attribute [" + attributeName + "] "
				+ "and expected value [" + expectedValue + "]");
	}


	//FIXME 
	public boolean linkAttributeShouldEqual(String text, String attributeName,String expectedAttribute) {
		try{

			List<WebElement> elements= driver.findElements(By.tagName("a"));
			for (WebElement element : elements) {				
				if(attributeName.equals("href")){
					
					if (element.getAttribute(attributeName)!=null && !"".equals(element.getAttribute(attributeName)) 
							&& element.getAttribute(attributeName).contains(expectedAttribute)
							&& expectedAttribute.equals(element.getAttribute(attributeName).substring((element.getAttribute(attributeName).length())-expectedAttribute.length()))
							) {

						return true;
					}
				}
				else if (attributeName.equals("text")
						&& element.getText().equals(expectedAttribute)) {
					return true;
				}
				else {				
					if (expectedAttribute.equals(element.getAttribute(attributeName))) {
						return true;
					}
				}
			}
			log.error("No link for [" + text + "] with attribute ["	+ attributeName + "] " + "and expected value ["
					+ expectedAttribute + "]");
			return false;
		}

		catch(Exception e){
			//e.printStackTrace(System.err);
			throw e;
		}

	}


	public void imageAttributeShouldEqual(String attributeName,		String expectedValue) {
		attributeShouldEqual("img", attributeName, expectedValue);
	}



	
	public void inputAttributeShouldEqual(String attributeName,
			String expectedAttribute) {
		attributeShouldEqual("input", attributeName, expectedAttribute);
	}


	//TODO Overrridden method to revisit
	public void inputAttributeShouldEqual(String nameOrId,	String attributeName, String expectedAttribute) throws  Exception{
		WebElement input =  new Util_WebElementQueryHelper(driver).query(nameOrId);
		String actualValue = input.getAttribute(attributeName);
		//FIXME workaround done to maintains status quo with htmlUnit
		if(actualValue==null)actualValue="";
		if (!expectedAttribute.equals(actualValue)) {
			throw new AssertionError("No [input] with id or name [" + nameOrId					+ "] and attribute [" + attributeName + "] "
					+ "and expected value [" + expectedAttribute + "]");
		}
	}


	


	public void tagWithAttributeDoesNotExist(String tagType, String attribute,
			String attributeValue) {
		if(tagWithAttributeExists(tagType, attribute, attributeValue)){
			throw new AssertionError("Tag [" + tagType + "] with attribute ["+ attribute + "] and expected value [" + attributeValue
					+ "] does  exists");
		}
	}


	public String refresh() {
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver)
			.executeScript("javascript:location.reload(true);");
		}

		return getTitle();
	}


	

	//NEW
	public String getListBoxContent(String id) throws  Exception{
		final Select select = new Select(new Util_WebElementQueryHelper(driver).query(id));
		StringBuffer buf = new StringBuffer();
		List<WebElement> list = select.getOptions();
		Iterator<WebElement> iter = list.iterator();
		while(iter.hasNext()){
			WebElement we = iter.next();
			buf.append(we.getText());
			buf.append("|");	
		}
		return buf.toString();
	}

	public String inputValuesAsList(){

		TextNodeVisitor visitor = new TextNodeVisitor();
		String parsedText = Jsoup.parse(driver.getPageSource() ).traverse(visitor).toString();
		String clean = parsedText.replaceAll("\\P{Print}", "");
		return clean;
	}


	public WebDriver getDriver() {		
		return driver; //FIXME how will this work in factory setup???
	}

	

	public void setNTCredentials(String user, String password) {
		this.user = user; 
		this.password = password;
		
	}

	
}
