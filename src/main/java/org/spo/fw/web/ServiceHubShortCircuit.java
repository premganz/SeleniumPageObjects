package org.spo.fw.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.spo.fw.exception.SPOException;
import org.spo.fw.navigation.itf.ApplicationNavigationModel;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.shared.DiffMessage;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.ibm.icu.util.Calendar;

/**
 * @author prem
 *This serves as a system wide keyword bus or something similar to a 'service catalog' with delegatees and perhaps also like
 *the AplicationContext of Spring, which is an application wide object repository that could be dynamically configured at build time
 *This is one compelling reason to have all the other libraries like Lib_ExternalScript calls to come in here.
 *But the oppossing reason is the possibility of having to pass around the 'kw' object everywhere.
 *You cannot also have static members because that would not allow for DI like configuration. 
 *  
 *We can use some system of App wide keyword bus, where all libraries could be plugged in.
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
public class ServiceHubShortCircuit extends ServiceHub{

	
	
	/**
	 * Create is required and must be run as the first step in a ServiceHub test.
	 * This keyword sets up the ServiceHub. Sets the browser to emulate and the
	 * current test name (for internal ServiceHub logging).
	 * purposes.
	 */
	
	public ServiceHubShortCircuit() {
		initDefaults();
	}
	
	public void create(String browser, String testName) throws SPOException {

	}

	/**
	 * This keyword allows the script to click an element, given the xpath to
	 * the element. For more information on xpath:
	 * http://www.w3schools.com/xpath/xpath_syntax.asp
	 *
	 */
	public String clickByXPath(String xpath)  {
		return (String)handleInvocation(impl,"clickByXPath", new Object[]{xpath});
	}

	//UPDATED
	public String click(String nameOrIdOrText)  {
		return (String)handleInvocation(impl,"click", new Object[]{nameOrIdOrText});/**return impl.click(nameOrIdOrText);**/}



	public void clickByTagAndAttribute(String tag, String attribute, String value){
		handleInvocation(impl,"clickByTagAndAttribute", new Object[]{tag,attribute,value});
	}

	public void acceptAlert(){
		handleInvocation(impl,"acceptAlert", new Object[]{});
	}



	/**
	 * Allows the script to enter text into a text box. Assumes the element
	 * defined by nameOrId is an input tag.

	 */
	public String enterText(String nameOrId, String text) {
		return (String)handleInvocation(impl,"enterText", new Object[]{nameOrId,text});
	}
	
	public String clearEnterText(String nameOrId, String text) {
		clear(nameOrId);
		return (String)handleInvocation(impl,"enterText", new Object[]{nameOrId,text});
		//		return impl.enterText(nameOrId, text);
	}
	
	public String clear(String nameOrId) {
		return (String)handleInvocation(impl,"clear", new Object[]{nameOrId});		
	}

	public String getValue(String nameOrId) {
		return (String)handleInvocation(impl,"getValue", new Object[]{nameOrId});
	}


	public boolean assertPageContains(String expectedText)  {
		return true;
	}
	
	public boolean pollPageContains(String expectedText)  {
		return true;
	}

	public boolean assertPageContainsRegex(String regex){	
		return true;
	}
	public boolean assertPageContainsNot(String expectedText) {
		return true;
	}
	
	public void assertButtonNameEquals(String nameOrId, String expectedValue) {
		}


	public String goToPage(String url)  {
		return (String)handleInvocation(impl,"goToPage", new Object[]{url});
	}

	public String getOption(String nameOrId, int index) {
		return (String)handleInvocation(impl,"getOption", new Object[]{nameOrId,String.valueOf(index)});
	}

	public String utilStripWhitespace(String whiteString) {return KeyWords_Utils.stripWhitespace(whiteString);}

	public void selectOption(String nameOrId, String value) { 
		
	}

	/**
	 * Select the option specified by Incex. Use this keyword for dropdown
	 * boxes.
	 *
	 * @param nameOrId the name or id of the select box.
	 * @param index of the element for the option in the select box you want to
	 * select.
	 */
	//zero based index
	public void selectOptionByIndex(String nameOrId, String index) {
		handleInvocation(impl,"selectOptionByIndex", new Object[]{nameOrId,index});
	}
	/**
	 * Select all the options of multiselect box 
	 * @param nameOrId the name or id of the select box.

	 */
	public void selectAllOptions(String nameOrId) {
		handleInvocation(impl,"selectAllOptions", new Object[]{nameOrId});
	}

	/**
	 * Assertion whether the option with display value is slected 
	 * @param nameOrId the name or id of the select box.
	 * @param value expected to be selected

	 */
	public boolean assertOptionSelected(String nameOrId, String value) {
		return true;
		//impl.optionShouldBeSelected(nameOrId, value);
	}

	/**
	 * Check to see that this radio button IS selected. This works for
	 * radiobuttons.
	 *
	 * @param nameOrId the name or id of the radio button.
	 */
	public void assertRadioButtonSelected(String nameOrId) {
		handleInvocation(impl,"radioButtonShouldBeSelected", new Object[]{nameOrId});
		//impl.radioButtonShouldBeSelected(nameOrId);
	}

	/**
	 * Check to see that this radio button is NOT selected. This works for
	 * radiobuttons.
	 *
	 * @param nameOrId the name or id of the radio button.
	 */
	public void assertRAdioButtonSelectedNot(String nameOrId) {
		handleInvocation(impl,"radioButtonShouldNotBeSelected", new Object[]{nameOrId});
		//impl.radioButtonShouldNotBeSelected(nameOrId);
	}
	public void assertCheckBoxSelected(String nameOrId) {
		handleInvocation(impl,"checkBoxShouldBeSelected", new Object[]{nameOrId});
		//impl.checkBoxShouldBeSelected(nameOrId);

	}

	public void assertCheckBoxSelectedNot(String nameOrId) {
		handleInvocation(impl,"checkBoxShouldNotBeSelected", new Object[]{nameOrId});
	}

	public String getOptionSize(String nameOrId) {
		return (String)handleInvocation(impl,"getOptionSize", new Object[]{nameOrId});
		//return impl.getOptionSize(nameOrId);
	}
	public String getDefaultOption(String nameOrId) {
		return (String)handleInvocation(impl,"getDefaultOption", new Object[]{nameOrId});
		//	return impl.getDefaultOption(nameOrId);
	}

	public String getCellFromTable(String nameOrId, int row, int column) throws Exception{
		return "";}

	public String getTagAttributeFromTableCell(String tableNameOrId, int row,
			int column, String tagName, String attribute) throws Exception {return "";}

	public String getNumberOfRowsFromTable(String nameOrId)  {return "";}

	public String getNumberOfRowsFromTableByXpath(String xpath) {return "";}

	public String getTitle()  {
		return (String)handleInvocation(impl,"getTitle", new Object[]{});
	}


	//TODO To recheck
	public void assertLabelAttributeEquals(String forValue,
			String attributeName, String expectedValue) {return ;}

	//FIXME 
	public boolean assertLinkAttributeEquals(String text, String attributeName,String expectedAttribute) {
		return true;
	}

	public void imageAttributeShouldEqual(String attributeName,
			String expectedValue) {return; }

	//FIXME
	public void attributeShouldEqual(String tag, String attribute,
			String expectedValue) {}


	public void inputAttributeShouldEqual(String attributeName,
			String expectedAttribute) {}

	//TODO move to impl_spec, overreidden method , not required.
	public void inputAttributeShouldEqual(String nameOrId,	String attributeName, String expectedAttribute) throws Exception {}

	public boolean assertExists(String tagType, String nameOrId) {return true;} 


	public void tagWithAttributeExists(String tagName, String attribute,
			String attributeValue) {}

	public void tagWithAttributeDoesNotExist(String tagType, String attribute,
			String attributeValue) {}
	public String doRefresh() {return "";}

	public String doBack() {return "";}

	public String doForward() {return "";}




	public String utilClear(String input)   {
		return (String)handleInvocation(impl,"clear", new Object[]{input});
		//return impl.clear(input);
	}

	public String getTextByXPath(String xpath) {
		return (String)handleInvocation(impl,"getText", new Object[]{xpath});
		//return impl.getText(xpath);
	}
	
	public String getAttributeAsText(String xpath, String attrName) {return "";}
	public String evalXpathAsString(String xpath) {return "";}
	public void printHtml() {}

	public String  doPrintPageAsText() {return "";}
	//TODO NEW
	public String getCurrentUrl(){return "";}

	public String  doPrintPageAsTextFormatted() {
		return (String)handleInvocation(impl,"printPageAsTextFormatted", new Object[]{});
	}
	public String  getTableColumnCount(String nameOrId) {return "";}

	public String  utilTrimCleanString(String input, String replaceable){return "";}


	public String  utilTleanString(String input){return "";}
	public String  checkPageAgainstFile(String filePath){return null;}

	@Deprecated //TODO: NO longer functional
	public String  checkPageAgainstRemoteFile(String moduleName, String pageName){return null;}

	//use expression as   :: moduleName/fileName&meta=includeFormData 
	public DiffMessage  checkPageAgainstRemoteFileExpression(String expression){return impl_page.entry_navigateCheckPageLayout("",expression,this);}

	//TODO : Implement navigation and check as single kw??
	public DiffMessage  checkPageLayoutForm(String pageName, String expression){	return impl_page.entry_navigateCheckPageLayout(pageName, expression,this);}

	//public String  checkPageAgainstRemoteFileWithForm(String expression){return impl_page.checkPageAgainstRemoteFile(expression);}

	public DiffMessage  checkPageCollated(String testName, String expression){return impl_page.entry_checkPageCollated(testName, expression,this);}

	public String  getCollatedLog(){return impl_page.getCollatedLog();}

	//public String  checkPageAgainstFile(String filePath){return "";}
	public void quit() throws SPOException{
	}
	public void closeDriver() throws SPOException{
	}
	public void quitDriver() throws SPOException{
	}
	public void doSwitchToNewWindow(){
	}
	//Switches back to the previous window as fter using switchtonew window, stateful with the switchtonewwindow keword
	public void doSwitchBackToPreviousWindow(){
	}
	public String getRecordFromTable(String tableId, String regexSelector){return "";}

	public List<String> getCheckBoxValuesList(String id, String measure){return new ArrayList<String>();}

	//NEW
	public String getListBoxContent(String id) throws Exception{
		return "";
	}



	public Object doExecuteJavaScript(String x1) {

		return "";
	}

	//NEW 
	public void doCloseWindow(){}


	public String getInputValuesAsList(){return "";}

	


	public String getTextFromTagWithId(String nameOrId) {return "";}


	public WebDriver getDriver() {	return new WebDriverDummy();	
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

	public String utilGetTimeStamp () {	       
		return impl_spec.getTimeStamp ();}

	public String  doRunOSCmd (String os, String cmd, String execDir){
		return impl.runOSCmd(os, cmd, execDir); 

	}
	public String getApplicationStartupParam(String key) {
		return impl.getApplicationStartupParam(key);
	}
	public WebElement getElement(String clue) {
		return (WebElement)handleInvocation(impl,"findElement", new Object[]{clue});
		//return impl.findElement(clue);
	}
	
	/**Abstract Specific Keywords**/

	

	

	//TODO public KeyWordCommand findClosestMatch_kw(String category_arg,String partialId_arg, String mdfVal_arg);

	

	//Very specific script for  for a specific business function.


	public void navigateByName(String pageName) {}
	public void event_page(String page, String stateExpression) {}
	public void event_page2(String page, String stateExpression) {}
	//Inheringing domain events
//	public void event_domain(String actor, String eventExpression)  {}
//	public void event_domain2(String actor, String eventExpression)  {}
	
	public void nav_link_config(String linkName, String navExpression) {}
	public void event_current_page(String page, String stateExpression) {}
	
	

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

	public Object handleInvocation(Object proxy, String method, Object[] args){
		
		return "";
	}


	public boolean isFailFast() {
		return failFast;
	}


	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}


	public boolean isFailSlow() {
		return failSlow;
	}
	public void setFailSlow(boolean failSlow) {
		this.failSlow = failSlow;
	}
	

	
	
}
