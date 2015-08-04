package org.spo.fw.web;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.exception.UnexpectedWebDriverException;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.ApplicationNavigationModel;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.service.DriverFactory;
import org.spo.fw.shared.DiffMessage;
import org.spo.fw.utils.pg.Lib_PageLayout_Processor;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.ibm.icu.util.Calendar;

/**
 * We can use some system of App wide keyword bus, where all libraries could be plugged in.
This bus is responsible to delegate keyword call to appropriate implementation. It is stateful uses Webdriver Selenium
There are limitations to the way webDriver could be passed between objects hence static variables are part of 
this design.
See also a demo keyword in One of the plugged in libs that returns a command , that works. but it would not work if the plugged
in libraries are instantized for every call.
 */

/*
 * PROPOSED REFACTORING when finding surplus time, (1-Aug-14, Prem)
 * //QueryValueMessage
			//widget Type
			//widget nameOrId
			//widget xPath
			//booleancheck(compareText) > contains/selected/checked/shouldNotContain/equals/
			//count > optionSize/rowCount/columnCount
			//attribute(Attribute)  > id/name/<other>

		new QueryMessage().WidgetType.name("").boolenCheck(compareText)

	//QueryWebElementMessage
			//Use this mainly to get htmlcells from tables.

	//QueryPageMessage
			//title, textContent
			//booleanCheck(contains, notContains)

	Similarly ClickMessage, InputMessage

	//Executor
			Uplinks the messageHandler to the DriverProxy
			//MessageHandler
			//reattempt?
			//visibleBrow?
			//alertStrategy?
			//mutatingOperation?

	//MessageHandler
		//Handles differentMessages Differently, has find and other oft used methods internally.

	//DriverAccessProxy
		//ExecutorEntryPoint
		//Logger, RecoveryCopy(?)
 * 
 */
public class KeyWords implements SessionBoundDriverExecutor, InvocationHandler, ExtensibleService{

	protected static Logger1 log = new Logger1("org.spo.fw.web.KeyWords");

	protected String filePath_root_screens = System.getProperty("textScreens.path");
	protected PageFactory factory;
	protected ApplicationNavContainerImpl navContainer;
	protected ApplicationNavigationModel navModel;

	protected   WebDriver driver ;//TODO to rename this its too confusing with the impl.driver which is really used in queries
	public Lib_KeyWordsCore impl;
	public Lib_KeyWordsExtended impl_ext;
	public Lib_KeyWordsSpecific impl_spec;
	public Lib_PageLayout_Processor impl_page;
	public Lib_NavUtils impl_nav;
	public Lib_Messaging impl_msg;

	protected boolean failFast;
	/**
	 * Create is required and must be run as the first step in a KeyWords test.
	 * This keyword sets up the KeyWords. Sets the browser to emulate and the
	 * current test name (for internal KeyWords logging).
	 *
	 * @param browser valid values include ie6, ie7, ie8, and ff36
	 * @param testName The name of the current testcase. Used for logging
	 * purposes.
	 */
	public void create(String browser, String testName) throws SPOException {
		StringBuilder buffer = new StringBuilder();
		final Logger logger = Logger.getLogger(this.getClass());
		logger.trace("==============================================================================");
		buffer.append(testName).append(" on ").append(browser);
		logger.trace(buffer);
		logger.trace("==============================================================================");
		driverInstance();
		init();
		impl.setLog(log);

	}


	public void driverInstance() throws SPOException {
		StringBuilder buffer = new StringBuilder();
		final Logger logger = Logger.getLogger(this.getClass());
		logger.trace("==============================================================================");

		logger.trace(buffer);
		logger.trace("==============================================================================");
		try{
			if(SessionContext.isRecordMode){
				driver=DriverFactory.instance_testMode();
			}else{
				driver = DriverFactory.instance();
			}
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			//	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		}catch(WebDriverException e){

			if(SessionContext.isRecordMode){
				driver=DriverFactory.instance_testMode();
			}else{
				driver = DriverFactory.instance();
			}

		}


	}
	public void init(){
		//init the plugins, this permits for either complete reconfiguration during extension or 
		impl=new Lib_KeyWordsCore(driver);
		impl_ext=new Lib_KeyWordsExtended(driver);

		impl_page=new Lib_PageLayout_Processor(driver);
		impl_page.setPageFactory(factory);
		navContainer=new ApplicationNavContainerImpl();

		impl_nav= new Lib_NavUtils(driver);
		navModel.setFactory(factory);
		navContainer.setModel(navModel);
		impl_nav.setNavContainer(navContainer);
		
		
		try {
			impl_nav.init();
		} catch (Exception e) {
			log.error("Plugin not loaded "+"navigation plugin");			
		}
		impl_msg= new Lib_Messaging(driver);
		impl_spec=new Lib_KeyWordsSpecific(driver);
	}

	public void create(WebDriver  driver) {
		this.driver=driver;
		init();
		impl.setLog(log);


	}


	/**
	 * This keyword allows the script to click an element, given the xpath to
	 * the element. For more information on xpath:
	 * http://www.w3schools.com/xpath/xpath_syntax.asp
	 *
	 * @param xpath The xpath to the element
	 * @return the title of the page after the click
	 * @throws ElementNotFoundException
	 * @throws IOException
	 */
	public String clickByXPath(String xpath)  {

		return (String)handleInvoke(impl,"clickByXPath", new Object[]{xpath});

		//	return impl.clickByXPath( xpath) ;
	}


	/**
	 * Attempts to click on the element with the given name or text. If the user
	 * uses the correct id or name for an element, this keyword will attempt to
	 * use that element. The User could alternatively choose to pass the text
	 * that is on a link or the text on an input tag in place of the name or id.
	 * Using the text value will work on anchor tags and/or buttons.
	 *
	 * @param nameOrIdOrText The id or name of the element, or the text on the
	 * button or link.
	 *
	 * @return the title of the page after clicking.
	 * @throws ElementNotFoundException
	 * @throws IOException
	 */
	//UPDATED
	public String click(String nameOrIdOrText)  {
		return (String)handleInvoke(impl,"click", new Object[]{nameOrIdOrText});/**return impl.click(nameOrIdOrText);**/}



	public void clickByTagAndAttribute(String tag, String attribute, String value){
		handleInvoke(impl,"clickByTagAndAttribute", new Object[]{tag,attribute,value});
		//impl.clickByTagAndAttribute(tag, attribute, value);
	}

	public void acceptAlert(){
		handleInvoke(impl,"acceptAlert", new Object[]{});
		//impl.acceptAlert();
	}



	/**
	 * Allows the script to enter text into a text box. Assumes the element
	 * defined by nameOrId is an input tag.

	 */
	public String enterText(String nameOrId, String text) {
		return (String)handleInvoke(impl,"enterText", new Object[]{nameOrId,text});
		//		return impl.enterText(nameOrId, text);
	}


	/**
	 * Gets the value of an input tag. Only works for input tags.
	 *
	 * @param nameOrId the name or the id of the input tag to get the value of.
	 * @return the text in the text box.
	 */
	public String getValue(String nameOrId) {
		return (String)handleInvoke(impl,"getValue", new Object[]{nameOrId});
		//return impl.getValue(nameOrId);
	}




	/**
	 * Validates whether the page contains the expectedText.
	 *
	 * Implementation detail: This keyword attempts to handle non-ascii
	 * characters by converting them into their most similar ascii character.
	 * For instance, the trademark symbol is converted to 'T'. The registered
	 * trademark symbol (circled R) is converted into 'R'. The long hyphen is
	 * converted to '-'. A full list as of 2/17/12: '\u00A0' -> ' ' // &nbsp;
	 * '\u00A9' -> 'c' // copyright char // anything that could look like a dash
	 * '\u00AD' -> '-' '\u2010' -> '-' '\u2500' -> '-' '\u2212' -> '-' '\u3161'
	 * -> '-' '\u30FC' -> '-' '\u4E00' -> '-' '\u2122' -> 'T' // trademark (TM)
	 *
	 * @param expectedText the text we're expecting to be on the page.
	 */

	public boolean pageShouldContain(String expectedText)  {
		return (Boolean)handleInvoke(impl,"pageShouldContain", new Object[]{expectedText});
		//return impl.pageShouldContain(expectedText);
	}

	public boolean pageShouldContainRegex(String regex){	
		return (Boolean)handleInvoke(impl,"pageShouldContainRegex", new Object[]{regex});
		//return impl.pageShouldContainRegex(regex);
	}
	public boolean pageShouldNotContain(String expectedText)throws Exception {
		return (Boolean)handleInvoke(impl,"pageShouldNotContain", new Object[]{expectedText});
		//impl.pageShouldNotContain(expectedText);
	}


	/**
	 * Validates whether the page does not contain the expectedText.
	 *
	 * @param expectedText the text we're expecting not to be on the page.
	 */

	/**
	 * This keyword can be used to ensure that a button name is equal to the
	 * expected value.
	 *
	 * @param nameOrId
	 * @param expectedValue
	 *///FIXME :Failing concurrent.layout.txt
	public void buttonNameShouldEqual(String nameOrId, String expectedValue) {
		impl_spec.buttonNameShouldEqual(nameOrId, expectedValue);}


	/**
	 * Use this to navigate to a specific page. Usually used at the beginning of
	 * a test. Both http and https will work with this keyword.
	 *
	 * @param url the url of the page
	 * @return title of the page
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String goToPage(String url)  {
		return (String)handleInvoke(impl,"goToPage", new Object[]{url});
		//return impl.goToPage(url);
	}


	/**
	 * Gets the option specified by index from the select (drop-down).
	 *
	 * @param nameOrId of the select box
	 * @param index of the option in the select
	 * @return value of the option
	 */
	public String getOption(String nameOrId, int index) {
		return (String)handleInvoke(impl,"getOption", new Object[]{nameOrId,String.valueOf(index)});
		//return impl.getOption(nameOrId, index);
	}

	/**
	 * strips whitespace on string.
	 *
	 */
	public String stripWhitespace(String whiteString) {return KeyWords_Utils.stripWhitespace(whiteString);}


	/**
	 * Select the option specified by value. Use this keyword for dropdown
	 * boxes.
	 *
	 * @param nameOrId the name or id of the select box.
	 * @param value the text for the option in the select box you want to
	 * select.
	 */
	public void selectOption(String nameOrId, String value) { 
		handleInvoke(impl,"selectOption", new Object[]{nameOrId,value});
		//impl.selectOption(nameOrId, value);
	}

	/**
	 * Select the option specified by Incex. Use this keyword for dropdown
	 * boxes.
	 *
	 * @param nameOrId the name or id of the select box.
	 * @param index of the element for the option in the select box you want to
	 * select.
	 */
	public void selectOptionByIndex(String nameOrId, String index) {
		handleInvoke(impl,"selectOptionByIndex", new Object[]{nameOrId,index});
		//impl.selectOptionByIndex(nameOrId, index);
	}
	/**
	 * Select all the options of multiselect box 
	 * @param nameOrId the name or id of the select box.

	 */
	public void selectAllOptions(String nameOrId) {
		handleInvoke(impl,"selectAllOptions", new Object[]{nameOrId});
		//impl.selectAllOptions(nameOrId);
	}

	/**
	 * Assertion whether the option with display value is slected 
	 * @param nameOrId the name or id of the select box.
	 * @param value expected to be selected

	 */
	public boolean optionShouldBeSelected(String nameOrId, String value) {
		return (Boolean)handleInvoke(impl,"optionShouldBeSelected", new Object[]{nameOrId,value});
		//impl.optionShouldBeSelected(nameOrId, value);
	}

	/**
	 * Check to see that this radio button IS selected. This works for
	 * radiobuttons.
	 *
	 * @param nameOrId the name or id of the radio button.
	 */
	public void radioButtonShouldBeSelected(String nameOrId) {
		handleInvoke(impl,"radioButtonShouldBeSelected", new Object[]{nameOrId});
		//impl.radioButtonShouldBeSelected(nameOrId);
	}

	/**
	 * Check to see that this radio button is NOT selected. This works for
	 * radiobuttons.
	 *
	 * @param nameOrId the name or id of the radio button.
	 */
	public void radioButtonShouldNotBeSelected(String nameOrId) {
		handleInvoke(impl,"radioButtonShouldNotBeSelected", new Object[]{nameOrId});
		//impl.radioButtonShouldNotBeSelected(nameOrId);
	}
	/**
	 * Check to see that this checkbox is selected. This works for checkboxes
	 * only.
	 *
	 * @param nameOrId the name or id of the check box.
	 */
	public void checkBoxShouldBeSelected(String nameOrId) {
		handleInvoke(impl,"checkBoxShouldBeSelected", new Object[]{nameOrId});
		//impl.checkBoxShouldBeSelected(nameOrId);

	}

	/**
	 * Check to see that this checkbox is NOT selected. This works for check
	 * boxes only.
	 *
	 * @param nameOrId the name or id of the check box.
	 */
	public void checkBoxShouldNotBeSelected(String nameOrId) {
		handleInvoke(impl,"checkBoxShouldNotBeSelected", new Object[]{nameOrId});
		//impl.checkBoxShouldNotBeSelected(nameOrId);
	}


	/**
	 *
	 * Used to get the number of options in the named select box (dropdown).
	 *
	 * @param nameOrId name of the select
	 * @return the count of options in the select
	 */
	public String getOptionSize(String nameOrId) {
		return (String)handleInvoke(impl,"getOptionSize", new Object[]{nameOrId});
		//return impl.getOptionSize(nameOrId);
	}

	/**
	 * Gets the value of the default option from the drop down box (select tag).
	 *
	 * @param nameOrId of the select (drop-down)
	 * @return value of the default option
	 */
	public String getDefaultOption(String nameOrId) {
		return (String)handleInvoke(impl,"getDefaultOption", new Object[]{nameOrId});
		//	return impl.getDefaultOption(nameOrId);
	}

	/**
	 * When given the name or id of a table as well as the row and column within
	 * the table, this keyword returns the text value of the cell.
	 *
	 * @param nameOrId of the table
	 * @param row
	 * @param column
	 * @return the value of the text in the cell
	 */
	public String getCellFromTable(String nameOrId, int row, int column) throws Exception{
		return impl_ext.getCellFromTable(nameOrId, row, column);}



	public final WebElement getCellAt(String nameOrId, final int rowIndex, final int columnIndex) throws Exception{return impl.getCellAt(nameOrId, rowIndex, columnIndex);}
	/**
	 * Use this keyword when there is a tag nested in a cell of a table.
	 *
	 * @param tableNameOrId of the table
	 * @param row of the cell
	 * @param column of the cell
	 * @param tagName of the nested tag you want to get
	 * @param attribute of the tag you want the value for
	 * @return the value of the tag in the given cell
	 */
	public String getTagAttributeFromTableCell(String tableNameOrId, int row,
			int column, String tagName, String attribute) throws Exception {return impl_ext.getTagAttributeFromTableCell(tableNameOrId, row, column, tagName, attribute);}

	/**
	 * Gets the number of rows in the table specified by nameOrId
	 *
	 * @param nameOrId of the table
	 * @return count of rows in the table
	 */
	public String getNumberOfRowsFromTable(String nameOrId)  {return impl_spec.getNumberOfRowsFromTable(nameOrId);}

	public String getNumberOfRowsFromTableByXpath(String xpath) {return impl_spec.getNumberOfRowsFromTableByXpath(xpath);}


	/**
	 * Gets the title of the current page.
	 *
	 * @return
	 */
	public String getTitle()  {
		return (String)handleInvoke(impl,"getTitle", new Object[]{});
		//return impl.getTitle();
	}

	/**
	 * This keyword works for labels that specify a value for the "for"
	 * attribute. It matches on the value of the "for" attribute, then checks to
	 * see that the value of the attribute specified by attributeName has the
	 * value of expectedValue.
	 *
	 * @param forValue
	 * @param attributeName
	 * @param expectedValue
	 */

	//TODO To recheck
	public void labelAttributeShouldEqual(String forValue,
			String attributeName, String expectedValue) { impl_ext.labelAttributeShouldEqual(forValue, attributeName, expectedValue);}

	/**
	 * This keyword indicates that there is a link with the attribute's expected
	 * value somewhere on the page.
	 *
	 * @param text text of the link
	 * @param attributeName
	 * @param expectedAttribute
	 */
	//FIXME 
	public boolean linkAttributeShouldEqual(String text, String attributeName,String expectedAttribute) {
		return impl_ext.linkAttributeShouldEqual(text, attributeName, expectedAttribute);}

	/**
	 * This keyword compares the value of the image attribute specified to the
	 * expected value
	 *
	 * @param attributeName attribute
	 * @param expectedValue
	 */
	public void imageAttributeShouldEqual(String attributeName,
			String expectedValue) {impl_ext.imageAttributeShouldEqual(attributeName, expectedValue);}



	/**
	 * This keyword allows the user to find any instance of this tag on the page
	 * and validate the expectedValue.
	 *
	 * @param tag name of the tag
	 * @param attribute the value of which is expected to match expectedValue
	 * @param expectedValue the value you expect to match
	 * @return true or false
	 */
	//FIXME
	public void attributeShouldEqual(String tag, String attribute,
			String expectedValue) {
		impl.attributeShouldEqual(tag, attribute, expectedValue);
	}


	public void inputAttributeShouldEqual(String attributeName,
			String expectedAttribute) {impl_ext.inputAttributeShouldEqual(attributeName, expectedAttribute);}

	/**
	 * This keyword allows the user to compare an input tag's attribute with
	 * expectedAttribute. If the input tag specified by nameOrId has an
	 * attribute that matches the expectedValue, it returns true.
	 *
	 * @param nameOrId Name of id of the input tag
	 * @param attributeName Name of the attribute you want to check
	 * @param expectedAttribute Expected value of the attibute
	 */
	//TODO move to impl_spec, overreidden method , not required.
	public void inputAttributeShouldEqual(String nameOrId,	String attributeName, String expectedAttribute) throws Exception {
		impl_ext.inputAttributeShouldEqual(nameOrId, attributeName, expectedAttribute);}

	/**
	 * This keyword can be used to ensure that a tag of type tagName with the
	 * nameOrId exists on the page.
	 *
	 * @param tagType The type of the tag
	 * @param nameOrId The id or name of the tag
	 */
	public boolean shouldExist(String tagType, String nameOrId) {
		return (Boolean)handleInvoke(impl,"shouldExist", new Object[]{tagType,nameOrId});
		//return impl.shouldExist(tagType, nameOrId);
	} 



	/**
	 * Use this keyword to ensure that the tag with the specified attribute and
	 * value exists on the page
	 *
	 * @param tagName the tag you're looking for (input, a, body, etc. any html
	 * tag)
	 * @param attribute the attribute of the tag (example is href for the a tag
	 * or id for the input tag)
	 * @param attributeValue the value you're expecting an instance of this tag
	 * to have for the specified attribute
	 */
	public void tagWithAttributeExists(String tagName, String attribute,
			String attributeValue) {impl.tagWithAttributeExists(tagName, attribute, attributeValue);}

	/**
	 * Use this keyword to ensure that the tag with the specified attribute and
	 * attributeValue does not exists on the page
	 *
	 * @param tagType the tag you're looking for (input, a, body, etc. any html
	 * tag)
	 * @param attribute the attribute of the tag (example is href for the a tag
	 * or id for the input tag)
	 * @param attributeValue the value you're expecting an instance of this tag
	 * to have for the specified attribute
	 */
	public void tagWithAttributeDoesNotExist(String tagType, String attribute,
			String attributeValue) {impl_ext.tagWithAttributeDoesNotExist(tagType, attribute, attributeValue);}

	/**
	 * Reloads the current page. Uses Javascript to do so.
	 *
	 * @return The title of the page.
	 */
	public String refresh() {return impl.refresh();}

	/**
	 * Attempts to go back one page in the browser's history. Uses javascript to
	 * do so.
	 *
	 * @return the title of the page after going back.
	 */
	public String back() {return impl.back();}

	/**
	 * Attempts to go forward one page in the browser's history. Uses javascript
	 * to do so.
	 *
	 * @return the title of the page after going forward.
	 */
	public String forward() {return impl.forward();}




	public String clear(String input)   {
		return (String)handleInvoke(impl,"clear", new Object[]{input});
		//return impl.clear(input);
	}

	public String getTextByXPath(String xpath) {
		return (String)handleInvoke(impl,"getText", new Object[]{xpath});
		//return impl.getText(xpath);
	}
	
	public String getAttributeAsText(String xpath, String attrName) throws  Exception{
		return (String)handleInvoke(impl,"getAttributeAsText", new Object[]{xpath,attrName});
	}
	public String evalXpathAsString(String xpath) {
		return impl.evalXpathAsString(xpath);
	}
	/**
	 * Print the HTML of the document to the console.
	 */
	public void printHtml() {impl.printHtml();}

	/**Dumps the text nodes **/
	public String  printPageAsText() {
		return (String)handleInvoke(impl,"printPageAsText", new Object[]{});
		//return impl.printPageAsText();
	}
	/**Gets text nodes and formats it in lines**/
	public String  printPageAsTextFormatted() {
		return (String)handleInvoke(impl,"printPageAsTextFormatted", new Object[]{});
		//return impl.printPageAsTextFormatted();
	}
	/**
	 * Gets the count of Columns from the table specified by nameOrId.
	 *
	 * @param nameOrId the name or the id of the table.
	 * @return number of columns
	 */
	public String  getTableColumnCount(String nameOrId) {return impl_spec.getTableColumnCount(nameOrId);}



	/**
	 * Checks for whether the element specified by nameOrId is displayed.
	 *
	 * @param nameOrId the name or id of the element.
	 */


	public String  trimCleanString(String input, String replaceable){return impl_spec.trimCleanString(input, replaceable);}


	public String  cleanString(String input){return impl_spec.cleanString(input);}

	//This keyword allows the capture of the screen using Ctrl+c from IE , store it as a file and 
	// compare it against text representation of actual screen. Notice ***Break*** keyword helps to have breaks in the compared text
	@Deprecated
	//TODO: NO longer functional
	public String  checkPageAgainstFile(String filePath){return null;}

	@Deprecated //TODO: NO longer functional
	public String  checkPageAgainstRemoteFile(String moduleName, String pageName){return null;}

	//use expression as   :: moduleName/fileName&meta=includeFormData 
	public DiffMessage  checkPageAgainstRemoteFileExpression(String expression){return impl_page.entry_navigateCheckPageLayout(expression,this);}

	//TODO : Implement navigation and check as single kw??
	public DiffMessage  checkPageLayoutForm(String pageName, String expression){	return impl_page.entry_navigateCheckPageLayout(pageName, expression,this);}

	//public String  checkPageAgainstRemoteFileWithForm(String expression){return impl_page.checkPageAgainstRemoteFile(expression);}

	public DiffMessage  checkPageCollated(String testName, String expression){return impl_page.entry_checkPageCollated(testName, expression,this);}

	public String  getCollatedLog(){return impl_page.getCollatedLog();}

	//public String  checkPageAgainstFile(String filePath){return "";}
	public void quit() throws SPOException{
		//driver.quit();
		impl.quit();
	}
	public void closeDriver() throws SPOException{
		//driver.quit();
		driver.close();
	}
	public void quitDriver() throws SPOException{
		//driver.quit();
		driver.quit();
	}
	public void switchToNewWindow(){
		handleInvoke(impl,"switchToNewWindow", new Object[]{});
		//impl.switchToNewWindow();
	}
	//Switches back to the previous window as fter using switchtonew window, stateful with the switchtonewwindow keword
	public void switchBackToPreviousWindow(){
		handleInvoke(impl,"switchBackToPreviousWindow", new Object[]{});
		//impl.switchBackToPreviousWindow();
	}
	//New


	//TODO keep or drop, if ever gets used.
	public String getRecordFromTable(String tableId, String regexSelector){return impl_spec.getRecordFromTable(tableId, regexSelector);}




	//TODO: To move to specific file, this is very specific quirky for abstract pages.
	public List<String> getCheckBoxValuesList(String id, String measure){return impl_spec.getCheckBoxValuesList(id, measure);}

	//NEW
	public String getListBoxContent(String id) throws Exception{
		return impl_ext.getListBoxContent(id);
	}



	public Object executeJavaScript(String x1) {

		return impl.executeJavaScript(x1);
	}

	//NEW 
	public void closeWindow(){impl.closeWindow();}


	public String inputValuesAsList(){return impl_ext.inputValuesAsList();}

	//TODO NEW
	public String getCurrentUrl(){return impl.getCurrentUrl();}



	public String getTextFromTagWithId(String nameOrId) {return impl_spec.getTextFromTagWithId(nameOrId);}


	public WebDriver getDriver() {		
		return impl.getDriver(); //FIXME how will this work in factory setup???
	}


	public void print(String x ){

		//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//System.err.println( sdf.format(Calendar.getInstance().getTime())+":"+x);
		System.err.println(KeyWords_Utils.obfuscate(x));
	}
	public void print1(String x ){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.err.println( sdf.format(Calendar.getInstance().getTime())+":"+KeyWords_Utils.obfuscate(x)) ;
		//System.err.println(x);
	}
	public void setNTCredentials(String user, String password, String host,
			String workstation, String domain) {
		impl_ext.setNTCredentials(user, password);
	}
	public String getTimeStamp () {	       
		return impl_spec.getTimeStamp ();}

	public String  runOSCmd (String os, String cmd, String execDir){
		return impl.runOSCmd(os, cmd, execDir); 

	}
	public String getApplicationStartupParam(String key) {
		return impl.getApplicationStartupParam(key);
	}
	public WebElement findElement(String clue) {
		return (WebElement)handleInvoke(impl,"findElement", new Object[]{clue});
		//return impl.findElement(clue);
	}
	/**Abstract Specific Keywords**/

	

	

	//TODO public KeyWordCommand findClosestMatch_kw(String category_arg,String partialId_arg, String mdfVal_arg);

	

	//Very specific script for  for a specific business function.


	public void navigateByName(String pageName) {impl_nav.navigateByName(pageName, this);}
	public void page_event(String page, String stateExpression) {
		impl_nav.setPageEvent(page, stateExpression);		
	}
	public void domain_event(String actor, String eventExpression) throws Exception {
		impl_msg.setDomainEvent(actor, eventExpression);		
	}
	public void link_config(String linkName, String navExpression) {
		impl_nav.setlinkStrategy(linkName, navExpression);
	}
	public void curr_page_event(String page, String stateExpression) {
		impl_nav.setCurrentPageEvent(page, stateExpression,this);		
	}
	public PageFactory getFactory() {
		return factory;
	}

	public void setFactory(PageFactory factory) {
		this.factory = factory;
	}

	public ApplicationNavContainerImpl getNavContainer() {
		return navContainer;
	}

	public void setNavContainer(ApplicationNavContainerImpl navContainer) {
		this.navContainer = navContainer;
	}

	/**All fail fast strategies could be centralized here., no need of any assertion errors in libraries,
	 * However it makes sense to wrap webdriver exceptions as either recoverable or nonrecoverable
	 * recoverable ones are like unexpected hangs on the browser, which could be handled at the test runner level, wherein the 
	 * whole test can be retried.
	 * irrecoverable ones are also mostly handled at the test runner level, but capable of being caught at the test script level *  
	 **/
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		Object o = null;
		Throwable target = null;
		try{
			o = method.invoke(proxy, args);
		}		
		catch(InvocationTargetException ee){			
			target = ee.getTargetException();
			throw target;
		}
		return o;
	}

	public Object handleInvoke(Object proxy, String method, Object[] args){
		Object toReturn = null;
		Class[] classes = new Class[args.length];
		for(int i = 0;i<args.length;i++){
			classes[i]=String.class;
		}

		try{
			Method m = impl.getClass().getMethod(method,classes);
			if(m.getReturnType().getSimpleName().equals("Boolean")){
				boolean bool_toReturn = (Boolean)invoke(impl,m, args);
				if(bool_toReturn && failFast){
					throw new AssertionError("FalseReturned  involcation of method "+method);
				}
			}
			toReturn = invoke(impl,m, args);
		
		}catch(NoSuchMethodException e){	
			e.printStackTrace();
		
		}catch(UnreachableBrowserException | NoSuchWindowException e){
			throw new UnexpectedWebDriverException();
		}
		
		catch(SPOException e1){
			if(e1.getCause() instanceof UnreachableBrowserException ||  e1.getCause() instanceof NoSuchWindowException ){
				throw new UnexpectedWebDriverException();
			}else{
				if(failFast){
					throw new AssertionError(e1.getMessage());
				}
				throw e1;
			}
		}catch(WebDriverException e1){
			if(e1 instanceof UnreachableBrowserException ||  e1 instanceof NoSuchWindowException ){
				throw new UnexpectedWebDriverException();
			}else{
				if(failFast){
					throw new AssertionError(e1.getMessage());
				}
				throw e1;
			}
		}catch(AssertionError t){
			throw t;
		}
		catch(Throwable t){		
			t.printStackTrace();
			throw new SPOException("some strange error");

		}
		return toReturn;
	}


	public boolean isFailFast() {
		return failFast;
	}


	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}


	public ApplicationNavigationModel getNavModel() {
		return navModel;
	}


	public void setNavModel(ApplicationNavigationModel navModel) {
		this.navModel = navModel;
	}


	
	
	
}
