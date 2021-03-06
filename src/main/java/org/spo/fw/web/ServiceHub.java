/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.spo.fw.config.ServiceFactory;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.exception.UnexpectedWebDriverException;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.service.DriverFactory;
import org.spo.fw.shared.DiffMessage;
import org.spo.fw.utils.pg.Lib_PageLayout_Processor;
import org.spo.fw.utils.pg.Lib_PageLayout_Validator;

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
public class ServiceHub implements SessionBoundDriverExecutor, InvocationHandler, ExtensibleService{

	protected static Logger1 log = new Logger1("org.spo.fw.web.ServiceHub");
	

	protected String filePath_root_screens = System.getProperty("textScreens.path");
	
	//Injectibles - Specificially for Spring xml injection
//	protected PageFactory factory;
//	protected ApplicationNavContainerImpl navContainer;
//	protected ApplicationNavigationModel navModel;
//	protected Lib_PageLayout_Content contentProvider;
	
	//core selenium library
	protected   WebDriver driver ;

	//delegatee handlers
	public Lib_KeyWordsCore impl;
	public Lib_KeyWordsExtended impl_ext;
	public Lib_KeyWordsSpecific impl_spec;
	public Lib_PageLayout_Processor impl_page;//FIXME TODO change this to mod_page
	public Lib_NavUtils impl_nav;
	//public Lib_Messaging impl_msg;
	
	//ServiceFActory
	public ServiceFactory serviceFactory;

	protected boolean failFast;
	protected boolean failSlow;
	protected boolean profileMode=false;
	StringBuffer logBuffer= new StringBuffer();
	
	/**
	 * Create is required and must be run as the first step in a ServiceHub test.
	 * This keyword sets up the ServiceHub. Sets the browser to emulate and the
	 * current test name (for internal ServiceHub logging).
	 *
	 * purposes.
	 */
	
	public ServiceHub() {
		initDefaults();
	}
	public void create(String browser, String testName) throws SPOException {
		StringBuilder buffer = new StringBuilder();
		final Logger logger = Logger.getLogger(this.getClass());
		logger.trace("==============================================================================");
		buffer.append(testName).append(" on ").append(browser);
		logger.trace(buffer);		
		driverInstance();
		//init();
		injectDriverIntoPlugins(driver);
		impl.setLog(log);

	}


	protected void driverInstance() throws SPOException {
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
	
	public void initDefaults(){
//		navContainer=new ApplicationNavContainerImpl();// DEFAULTS
//		contentProvider=new Lib_PageLayout_Content();// DEFAULTS
		impl=new Lib_KeyWordsCore(driver);
		impl_ext=new Lib_KeyWordsExtended(driver);
		impl_page=new Lib_PageLayout_Processor(driver);
		impl_spec=new Lib_KeyWordsSpecific(driver);
		impl_nav= new Lib_NavUtils(driver);	
		serviceFactory = new ServiceFactory();
	}
	public void init(){
		//init the plugins, this permits for either complete reconfiguration during extension or 
		//All injections happen here, note that none of the plugins are initialized above, 
		//so they can be configured here, either setting them to kw for a project in their extension of JunitScript or by Spring.
		// Simply call init() first if you want to set up some low level component of the libraries (such as navContainer.xxx and then call post the config again init()
		// You dont have to call init() in case if you plan to replace an existing high level libraray such as navContainer
		// during configuration it is ok to pass null instead of driver to the constructor of the libraries.
		
		/**
		 * The null check based initiation is done to accomodate overrides which may call super.init() pre or post the custom code. 
		 * In a pre Call, we expect an initiation of defaults and in a post call, those values that had been set into the service hub
		 * 
		 */
		
		
		
		//Navigation initiation on default mode or post the navContainer is set by the SETTER
		if(impl_nav==null){
		impl_nav= new Lib_NavUtils(driver);	
		
		}
		
		//*********************
		
		
		if(impl_page==null){
			impl_page=new Lib_PageLayout_Processor(driver);			
		}
		
		
		//***********************
		if(serviceFactory==null){
			 serviceFactory = new ServiceFactory();
		}
		
		//*********************
		
		impl_page.setValidation_provider(new Lib_PageLayout_Validator());
		impl_spec=new Lib_KeyWordsSpecific(driver);
		if(impl==null)
		impl=new Lib_KeyWordsCore(driver);
		
		impl_ext=new Lib_KeyWordsExtended(driver);
		impl_page.init();
		impl_nav.init();		
		
		
		
	}

	
	public void injectDriverIntoPlugins(WebDriver driver){
		impl.initLibrary(driver);
		impl_ext.initLibrary(driver);
		impl_page.initLibrary(driver);
		impl_spec.initLibrary(driver);
		impl_nav.initLibrary(driver);	
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
	 */
	public String clickByXPath(String xpath)  {

		return (String)handleInvocation(impl,"clickByXPath", new Object[]{xpath});

		//	return impl.clickByXPath( xpath) ;
	}


	/**
	 * Attempts to click on the element with the given name or text. If the user
	 * uses the correct id or name for an element, this keyword will attempt to
	 * use that element. The User could alternatively choose to pass the text
	 * that is on a link or the text on an input tag in place of the name or id.
	 * Using the text value will work on anchor tags and/or buttons.
	 *
	
	 *
	 */
	//UPDATED
	public String click(String nameOrIdOrText)  {
		return (String)handleInvocation(impl,"click", new Object[]{nameOrIdOrText});/**return impl.click(nameOrIdOrText);**/}



	public void clickByTagAndAttribute(String tag, String attribute, String value){
		handleInvocation(impl,"clickByTagAndAttribute", new Object[]{tag,attribute,value});
		//impl.clickByTagAndAttribute(tag, attribute, value);
	}

	public void acceptAlert(){
		handleInvocation(impl,"acceptAlert", new Object[]{});
		//impl.acceptAlert();
	}



	/**
	 * Allows the script to enter text into a text box. Assumes the element
	 * defined by nameOrId is an input tag.

	 */
	public String enterText(String nameOrId, String text) {
		return (String)handleInvocation(impl,"enterText", new Object[]{nameOrId,text});
		//		return impl.enterText(nameOrId, text);
	}
	
	public String clearEnterText(String nameOrId, String text) {
		clear(nameOrId);
		return (String)handleInvocation(impl,"enterText", new Object[]{nameOrId,text});
		//		return impl.enterText(nameOrId, text);
	}
	
	public String clear(String nameOrId) {
		return (String)handleInvocation(impl,"clear", new Object[]{nameOrId});		
		//		return impl.enterText(nameOrId, text);
	}


	/**
	 * Gets the value of an input tag. Only works for input tags.
	 *
	 */
	public String getValue(String nameOrId) {
		return (String)handleInvocation(impl,"getValue", new Object[]{nameOrId});
		//return impl.getValue(nameOrId);
	}





	public boolean assertPageContains(String expectedText)  {
		return (Boolean)handleInvocation(impl,"pageShouldContain", new Object[]{expectedText});
		//return impl.pageShouldContain(expectedText);
	}
	
	public boolean pollPageContains(String expectedText)  {
		return (Boolean)handleInvocation(impl,"pollPageShouldContain", new Object[]{expectedText});
		//return impl.pageShouldContain(expectedText);
	}

	public boolean assertPageContainsRegex(String regex){	
		return (Boolean)handleInvocation(impl,"pageShouldContainRegex", new Object[]{regex});
		//return impl.pageShouldContainRegex(regex);
	}
	public boolean assertPageContainsNot(String expectedText) {
		return (Boolean)handleInvocation(impl,"pageShouldNotContain", new Object[]{expectedText});
		//impl.pageShouldNotContain(expectedText);
	}
	

	/**
	 * Validates whether the page does not contain the expectedText.
	 *
	 * @param expectedText the text we're expecting not to be on the page.
	 */

	
	 ///FIXME :Failing concurrent.layout.txt
	public void assertButtonNameEquals(String nameOrId, String expectedValue) {
		impl_spec.buttonNameShouldEqual(nameOrId, expectedValue);}


	
	public String goToPage(String url)  {
		return (String)handleInvocation(impl,"goToPage", new Object[]{url});
		//return impl.goToPage(url);
	}


	
	public String getOption(String nameOrId, int index) {
		return (String)handleInvocation(impl,"getOption", new Object[]{nameOrId,String.valueOf(index)});
		//return impl.getOption(nameOrId, index);
	}

	
	public String utilStripWhitespace(String whiteString) {return KeyWords_Utils.stripWhitespace(whiteString);}


	
	public void selectOption(String nameOrId, String value) { 
		handleInvocation(impl,"selectOption", new Object[]{nameOrId,value});
		//impl.selectOption(nameOrId, value);
	}

	
	//zero based index
	public void selectOptionByIndex(String nameOrId, String index) {
		handleInvocation(impl,"selectOptionByIndex", new Object[]{nameOrId,index});
		//impl.selectOptionByIndex(nameOrId, index);
	}
	
	public void selectAllOptions(String nameOrId) {
		handleInvocation(impl,"selectAllOptions", new Object[]{nameOrId});
		//impl.selectAllOptions(nameOrId);
	}

	
	public boolean assertOptionSelected(String nameOrId, String value) {
		return (Boolean)handleInvocation(impl,"optionShouldBeSelected", new Object[]{nameOrId,value});
		//impl.optionShouldBeSelected(nameOrId, value);
	}

	
	public void assertRadioButtonSelected(String nameOrId) {
		handleInvocation(impl,"radioButtonShouldBeSelected", new Object[]{nameOrId});
		//impl.radioButtonShouldBeSelected(nameOrId);
	}

	
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
		//impl.checkBoxShouldNotBeSelected(nameOrId);
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
		return impl_ext.getCellFromTable(nameOrId, row, column);}



	public final WebElement getCellAt(String nameOrId, final int rowIndex, final int columnIndex) throws Exception{return impl.getCellAt(nameOrId, rowIndex, columnIndex);}

	public String getTagAttributeFromTableCell(String tableNameOrId, int row,
			int column, String tagName, String attribute) throws Exception {return impl_ext.getTagAttributeFromTableCell(tableNameOrId, row, column, tagName, attribute);}

	
	public String getNumberOfRowsFromTable(String nameOrId)  {return impl_spec.getNumberOfRowsFromTable(nameOrId);}

	public String getNumberOfRowsFromTableByXpath(String xpath) {return impl_spec.getNumberOfRowsFromTableByXpath(xpath);}


	/**
	 * Gets the title of the current page.
	 *
	 */
	public String getTitle()  {
		return (String)handleInvocation(impl,"getTitle", new Object[]{});
		//return impl.getTitle();
	}

	

	//TODO To recheck
	public void assertLabelAttributeEquals(String forValue,
			String attributeName, String expectedValue) { impl_ext.labelAttributeShouldEqual(forValue, attributeName, expectedValue);}


	//FIXME 
	public boolean assertLinkAttributeEquals(String text, String attributeName,String expectedAttribute) {
		return (Boolean)handleInvocation(impl_ext,"linkAttributeShouldEqual", new Object[]{text, attributeName, expectedAttribute});
	}

	
	public void imageAttributeShouldEqual(String attributeName,
			String expectedValue) {impl_ext.imageAttributeShouldEqual(attributeName, expectedValue);}



	//FIXME
	public void attributeShouldEqual(String tag, String attribute,
			String expectedValue) {
		impl.attributeShouldEqual(tag, attribute, expectedValue);
	}


	public void inputAttributeShouldEqual(String attributeName,
			String expectedAttribute) {impl_ext.inputAttributeShouldEqual(attributeName, expectedAttribute);}

	
	//TODO move to impl_spec, overreidden method , not required.
	public void inputAttributeShouldEqual(String nameOrId,	String attributeName, String expectedAttribute) throws Exception {
		impl_ext.inputAttributeShouldEqual(nameOrId, attributeName, expectedAttribute);}

	
	public boolean assertExists(String tagType, String nameOrId) {
		return (Boolean)handleInvocation(impl,"shouldExist", new Object[]{tagType,nameOrId});
		//return impl.shouldExist(tagType, nameOrId);
	} 



	public void tagWithAttributeExists(String tagName, String attribute,
			String attributeValue) {impl.tagWithAttributeExists(tagName, attribute, attributeValue);}

	public void tagWithAttributeDoesNotExist(String tagType, String attribute,
			String attributeValue) {impl_ext.tagWithAttributeDoesNotExist(tagType, attribute, attributeValue);}

	
	public String doRefresh() {return impl.refresh();}

	
	public String doBack() {return impl.back();}

	
	public String doForward() {return impl.forward();}




	public String utilClear(String input)   {
		return (String)handleInvocation(impl,"clear", new Object[]{input});
		//return impl.clear(input);
	}

	public String getTextByXPath(String xpath) {
		return (String)handleInvocation(impl,"getText", new Object[]{xpath});
		//return impl.getText(xpath);
	}
	
	public String getAttributeAsText(String xpath, String attrName) {
		return (String)handleInvocation(impl,"getAttributeAsText", new Object[]{xpath,attrName});
	}
	public String evalXpathAsString(String xpath) {
		return impl.evalXpathAsString(xpath);
	}
	/**
	 * Print the HTML of the document to the console.
	 */
	public void printHtml() {impl.printHtml();}

	/**Dumps the text nodes **/
	public String  doPrintPageAsText() {
		return (String)handleInvocation(impl,"printPageAsText", new Object[]{});
		//return impl.printPageAsText();
	}
	//TODO NEW
	public String getCurrentUrl(){
		return (String)handleInvocation(impl,"getCurrentUrl", new Object[]{});
		//return impl.getCurrentUrl();
	}

	/**Gets text nodes and formats it in lines**/
	public String  doPrintPageAsTextFormatted() {
		return (String)handleInvocation(impl,"printPageAsTextFormatted", new Object[]{});
		//return impl.printPageAsTextFormatted();
	}
	
	public String  getTableColumnCount(String nameOrId) {return impl_spec.getTableColumnCount(nameOrId);}



	

	public String  utilTrimCleanString(String input, String replaceable){return impl_spec.trimCleanString(input, replaceable);}


	public String  utilTleanString(String input){return impl_spec.cleanString(input);}

	//This keyword allows the capture of the screen using Ctrl+c from IE , store it as a file and 
	// compare it against text representation of actual screen. Notice ***Break*** keyword helps to have breaks in the compared text
	@Deprecated
	//TODO: NO longer functional
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
	public void doSwitchToNewWindow(){
		handleInvocation(impl,"switchToNewWindow", new Object[]{});
		//impl.switchToNewWindow();
	}
	//Switches back to the previous window as fter using switchtonew window, stateful with the switchtonewwindow keword
	public void doSwitchBackToPreviousWindow(){
		handleInvocation(impl,"switchBackToPreviousWindow", new Object[]{});
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



	public Object doExecuteJavaScript(String x1) {

		return impl.executeJavaScript(x1);
	}

	//NEW 
	public void doCloseWindow(){impl.closeWindow();}


	public String getInputValuesAsList(){return impl_ext.inputValuesAsList();}

	


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
	
//	public T doGetExternalScriptData(String url, T type){		
//		Lib_ExternalScriptCalls<T> lib = new Lib_ExternalScriptCalls<T>();
//		return lib.queryTRSCustom(url);
//	}
//	
//	public JsonMessage<T> doGetExternalScriptMessage(String url, Class<T> classOfT){
//		Lib_ExternalScriptCalls<JsonMessage<T>> lib = new Lib_ExternalScriptCalls<JsonMessage<T>>();
//		return lib.queryTRS(url);
//	}
	
	/**Abstract Specific Keywords**/

	

	

	//TODO public KeyWordCommand findClosestMatch_kw(String category_arg,String partialId_arg, String mdfVal_arg);

	

	//Very specific script for  for a specific business function.


	public void navigateByName(String pageName) {
		//ServiceHub h = this;
		 //handleInvocation(impl_nav,"navigateByName", new Object[]{pageName,h});
		impl_nav.navigateByName(pageName, this);
		 }
	public void event_page(String page, String stateExpression) {
		impl_nav.setPageEvent(page, stateExpression);		
	}
	public void event_page2(String page, String stateExpression) {
		impl_nav.setPageEvent(page, stateExpression.replaceAll(", ",",").replaceAll(",","&"));		
	}
	
	public void event_domain(String actor, String eventExpression)  {
		serviceFactory.getDomainSvc().event_domain(actor, eventExpression);
		
	}
	public void event_domain2(String actor, String eventExpression)  {		
		serviceFactory.getDomainSvc().event_domain(actor, eventExpression,"");		
	}
	
	
	
	
	public void nav_link_config(String linkName, String navExpression) {
		impl_nav.setlinkStrategy(linkName, navExpression);
	}
	public void event_current_page(String page, String stateExpression) {
		impl_nav.setCurrentPageEvent(page, stateExpression,this);		
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

	public Object handleInvocation(Object proxy, String method, Object[] args){		
		if(failSlow){try {Thread.sleep(3000);} catch (InterruptedException e2) {e2.printStackTrace();}}
		Object toReturn = null;
		Method m = null;
		Class[] classes = new Class[args.length];
		for(int i = 0;i<args.length;i++){
			classes[i]=String.class;
		}
		for(Object x:args){
		logBuffer.append(x.toString()+",");	
		}
		log.trace("ServiceHub. "+method+"("+logBuffer.toString()+")");
		logBuffer = new StringBuffer();
		try{
			 m = proxy.getClass().getMethod(method,classes);
			if(m.getReturnType().getSimpleName().equals("boolean")){				
				boolean bool_toReturn = (Boolean)invoke(proxy,m, args);
				if(!bool_toReturn && failFast){
					log.error("Going to throw assertionError because false was returned for "+method+" and kw configured for failfast");
					throw new AssertionError("FalseReturned  involcation of method "+method);
				}
			}
			
			long startTime=System.currentTimeMillis();			
			toReturn = invoke(proxy,m, args);
			if(profileMode && (m.getName().equals("click")|m.getName().contains("JavaScript"))) {
				long finiTime=System.currentTimeMillis();
				StringBuffer profMsg = new StringBuffer();
				
				//			profMsg.append((finiTime-startTime)+",");
				String methodInvoked = m.getName()+"("+Arrays.stream(args).map(x->(String)x).collect(Collectors.joining(","))+")";
				String url_path = driver.getCurrentUrl().replaceAll(SessionContext.appConfig.BASE_URL, "").replaceAll("http://","").replaceAll(SessionContext.appConfig.basicAuth_userId+"@","");
				profMsg.append((finiTime-startTime)+","+ methodInvoked.trim()+","+url_path.trim() );
//				profMsg.append(String.format("%1$10s %2$1s %3$35s %4$1s %5$200s",(finiTime-startTime),",", methodInvoked,",",url_path.trim() ));
//				profMsg.appe
				
				//			profMsg.append("On Url"+this.getCurrentUrl()+":::");
				//			profMsg.append(""+SessionContext.currentTestClass+"");
				ProfileLogger.closeAndPushToLog(profMsg.toString());
				
			}
		
		}catch(NoSuchMethodException e){	
			e.printStackTrace();
		
		}catch( StaleElementReferenceException e){
			log.debug("Exception was of tye "+e.getClass().getCanonicalName());
			try{
				Thread.sleep(1000);
				for(int i = 0;i<args.length;i++){
					classes[i]=String.class;
				}
				for(Object x:args){
				logBuffer.append(x.toString()+",");	
				}
				log.info("RETRYING   ServiceHub. "+method+"("+logBuffer.toString()+")");
				toReturn = invoke(proxy,m, args);
			}catch(Throwable e1){
				e1.printStackTrace();
			}
			
		}
		catch(UnreachableBrowserException | NoSuchWindowException e){
			log.debug("Exception was of tye "+e.getClass().getCanonicalName());
			throw new UnexpectedWebDriverException();
		}
		
		catch(SPOException e1){
			if(e1.getCause() instanceof UnreachableBrowserException ||  e1.getCause() instanceof NoSuchWindowException ||   e1.getCause() instanceof StaleElementReferenceException ){
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
				if(failSlow){
					log.error("TRYING TO HANDLE FROM WEBDRIVER ERROR");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						toReturn = invoke(proxy,m, args);
					} catch (Throwable e) {
						e.printStackTrace();
					}
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

	public void sleep(Consumer<ServiceHub> k, Integer sleepTime) {
		try {
			Thread.sleep(sleepTime*1000);
			k.accept(this);
			
		} catch (InterruptedException e) {
			
		}
		
	}
	

//	public void sleep1(IntConsumer<? super ServiceHub,Integer> mapper) {
//		
//	}
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
	


	public boolean isProfileMode() {
		return profileMode;
	}
	public void setProfileMode(boolean profileMode) {
		this.profileMode = profileMode;
	}
	
	@Deprecated
	public void setFactory(PageFactory factory) {
		init();
		impl_nav.navContainer.getDefaulModel().setFactory(factory);
	}
	
	@Deprecated
	public void setNavContainer(ApplicationNavContainerImpl navContainer) {
	init();
		impl_nav.navContainer=navContainer;
	}
	


	

	
}
