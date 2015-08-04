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

public class Lib_KeyWordsCore  implements PluggableRobotLibrary{


	protected String filePath_root_screens = System.getProperty("textScreens.path");
	private   WebDriver driver ;//TODO make private
	private String user;
	private String password;
	protected static Logger1 log = new Logger1("org.spo.fw.web.KeyWordsImpl") ;
int waitTimes=5;
	public static void setLog(Logger1 log) {
		Lib_KeyWordsCore.log = log;
	}
	
	public Lib_KeyWordsCore(){
		
	}
	

	
	public Lib_KeyWordsCore(WebDriver driver){
		super();
		this.initLibrary(driver);
	}
	public void initLibrary(WebDriver driver){
		this.driver = driver;
	}
		
		
	public String clickByXPath(String xpath)  throws Exception{
		new Util_WebElementQueryHelper(driver).query(xpath).click();
		return driver.getTitle();
	}

//TODO: To upgrade using utilhelperquery
	public String click(String nameOrIdOrText) throws Exception {
			//*[@id="_ctl0_PageMeat_btnAdd"]
			WebElement clickable=null;
			clickable=new Util_WebElementQueryHelper(driver).queryClickableSilent(nameOrIdOrText);
			if(clickable==null){
				String xpath = ".//*[@value='"+nameOrIdOrText+"']";
				clickable=new Util_WebElementQueryHelper(driver).queryClickableSilent(xpath);
			}
			try{
				if(clickable!=null) clickable.click();			
			}catch(Exception e){
			log.error(e.getClass().getName()+" Error while Trying to click "+nameOrIdOrText);
			throw e;
			//log.debug();
			
		}
		

		return getTitle();

	}

	public void clickByTagAndAttribute(String tag, String attribute, String value) throws Exception{
		try{
		List<WebElement> listTagWise = driver.findElements(By.tagName(tag));
		for (int i = 0; i<listTagWise.size();i++) {
			WebElement elem = listTagWise.get(i);
			String val = elem.getAttribute(attribute);
			if(elem.getAttribute(attribute).equals(value)){
				elem.click();
				
				break;
			}

		}}catch(Exception e){
			log.error(e);  
			//log.trace(driver.getPageSource());
			log.error(getTitle());log.debug(getCurrentUrl());
			log.error("failed to click by tag and attribute "+value);
			throw e;
		}

	}


	public String enterText(String nameOrId, String text) throws Exception{
		WebElement elem = new Util_WebElementQueryHelper(driver).query(nameOrId);
		elem.sendKeys(text);
		elem.sendKeys("\u0009");//Tabbing to get off it.
		return driver.getTitle();
	}
	
	public KeyWordCommand enterText1(final String nameOrId, final String text)throws  Exception {
		
		return new KeyWordCommand(){
			public String execute(WebDriver driver){
				WebElement elem =null;
				try {
					elem = new Util_WebElementQueryHelper(driver).query(nameOrId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				elem.sendKeys(text);
				elem.sendKeys("\u0009");//Tabbing to get off it.
				return driver.getTitle();
			}
		};
		
	}
	
	public String clear(String nameOrIdOrXpath) throws Exception {
			WebElement elem = new Util_WebElementQueryHelper(driver).query(nameOrIdOrXpath);
			elem.clear();
			return driver.getTitle();
	}

			
				
	public String enterTextByXpath(String xpath, String text)  throws Exception{
		return enterText(xpath, text);
	}

	public String evalXpathAsString(String xpath)  {
		String y = "document.evaluate('" + xpath+
				"', document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null )";
		String x = (String)executeJavaScript(y);
		return x;
	}	
	
	public String getValue(String nameOrId) throws Exception {
		WebElement elem = new Util_WebElementQueryHelper(driver).query(nameOrId);		
		if(!elem.getTagName().equals("select")){
			return elem.getAttribute("value");	
		}else{
			final Select select = new Select(elem);
			return select.getFirstSelectedOption().getText();
		}
	}
	


	public boolean pageShouldContain(String expectedText) throws Exception {
		String parsedText = printPageAsText();
		if(driver.getPageSource().contains(expectedText) || parsedText.contains(expectedText)){
			return true;
		}else{
			log.debug(getCurrentUrl());
			log.error("The following text was not found on the page: "+ expectedText);
			return false;
		}
	}
	
	public boolean pageShouldNotContain(String expectedText) throws Exception {
		String parsedText = printPageAsText();
		if(driver.getPageSource().contains(expectedText) || parsedText.contains(expectedText)){
			log.error("The following text was found on the page: "+ expectedText);
			log.debug(getCurrentUrl());
			return false;
		}else{
			return true;
		}
	}

	public boolean pageShouldContainRegex(String regex) throws Exception{
		String parsedText = printPageAsText();
		Pattern  pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(parsedText);
		if (matcher.find()) {
			return true;
		}else{
			log.error("The following text was not found on the page: "+ regex);
			return false;
		}
	}



	public String goToPage(String url)  throws Exception{
		if(SessionContext.isAuthoizationFailed){
			throw new AssertionError("*******AUTHORIZATION ISSUE *****DO NOT PROCEEED ***");
		}
		String url1 =StringUtils.EMPTY;
		String url2= StringUtils.EMPTY;
		String userId = SessionContext.appConfig.basicAuth_userId;
		String passwordUrl = SessionContext.appConfig.basicAuth_pwd;
		
		
       //Allowing user to configure the basic auth from robot
        if(user!=null){
        	userId = user;
        }
        if(password!=null){
        	passwordUrl = password;
        }

        //No basic auth for https
        if(url.contains("https")){               
               url2= url;
                  
        }else{
        	url1 = url.substring(7,url.length());
        	if(SessionContext.requireBasicAuthUrlPrefix){
        		url2 = "http://"+userId+":"+passwordUrl+"@";	
        	}else{
        		url2 = "http://";	
        	}

        }
		
		String url3=url2+url1;
		url3=url3.replaceAll("[\\s]" ,"%20");
		String title = StringUtils.EMPTY;
		try{

			driver.get(url3);
		}catch(WebDriverException e){
			log.error("Encountered WebDriver Exception while goto " + url);
			title = driver.getTitle();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
			driver.get(url3);

		}
		title = driver.getTitle();
		log.trace(url3);
		//log.trace(driver.getTitle());
		//log.trace(driver.getPageSource());
		if(title.contains("not authorized")){
			SessionContext.isAuthoizationFailed=true;
		}
		return title;
	}


	public String getOption(String nameOrId, String index) throws Exception {
		WebElement elem = new Util_WebElementQueryHelper(driver).query(nameOrId);
		final Select select = new Select(elem);
		int index_int = Integer.parseInt(index);
		String replaceText = select.getOptions().get(index_int-1).getText().replaceAll(" +", " ");//TODO Phantomjs does not eliminate duplicate spaces !!!
		return replaceText;
	}


	public void selectOption(String nameOrId, String value) throws Exception{
		WebElement elem = new Util_WebElementQueryHelper(driver).query(nameOrId);
		final Select select = new Select(elem);
		select.selectByVisibleText(value);
	}

	public void selectOptionByIndex(String nameOrId, String index) throws Exception{
		int index_int = Integer.parseInt(index);
		WebElement elem = new Util_WebElementQueryHelper(driver).query(nameOrId);
		final Select select = new Select(elem);
		select.selectByIndex(index_int);
	}

	public void selectAllOptions(String nameOrId) throws Exception{
		WebElement elem =  new Util_WebElementQueryHelper(driver).query(nameOrId);
		List<WebElement> options_lst =  elem.findElements(By.tagName("option"));
		for(WebElement option:options_lst){
			option.click();
		}

	}


	public boolean  optionShouldBeSelected(String nameOrId, String value) throws Exception{

		final Select select = new Select( new Util_WebElementQueryHelper(driver).query(nameOrId));
		List<WebElement> list = select.getAllSelectedOptions();
		Iterator<WebElement> iter = list.iterator();
		while(iter.hasNext()){
			WebElement we = iter.next();
			if((we.getText().equals(value))){
				return true;
			}
		}
		log.error("Option [" + value + "] not selected");
		return false;

	}

	private boolean isElementSelected(String nameOrId) throws  Exception{
		WebElement elem =  new Util_WebElementQueryHelper(driver).query(nameOrId);
		if (elem.isSelected()){
		return true;
		}
		return false;
	}
	

	public String getOptionSize(String nameOrId) throws  Exception{
		final Select select = new Select( new Util_WebElementQueryHelper(driver).query(nameOrId));
		return String.valueOf(select.getOptions().size());
	}


	public String getDefaultOption(String nameOrId) throws  Exception{
		final Select select = new Select( new Util_WebElementQueryHelper(driver).query(nameOrId));
		String replaceText = select.getFirstSelectedOption().getText().replaceAll(" +"," ");
		return replaceText;
	}


	


	public final WebElement getCellAt(String nameOrId, final int rowIndex, final int columnIndex) throws  Exception{
		WebElement table =  new Util_WebElementQueryHelper(driver).query(nameOrId);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		final HashSet<Point> occupied = new HashSet<Point>();
		int row = 0;
		for (final WebElement htmlTableRow : rows) {
			List<WebElement> columns  = htmlTableRow.findElements(By.tagName("td"));
			int col = 0;
			for (final WebElement cell : columns) {
				while (occupied.contains(new Point(row, col))) {
					col++;
				}
				int x=1 ;int y=1;
				if ((cell.getAttribute("rowspan")!=null)){
					x = Integer.valueOf(cell.getAttribute("rowspan"));
				}
				if ((cell.getAttribute("colspan")!=null)){
					y = Integer.valueOf(cell.getAttribute("colspan"));
				}
				final int nextRow = row + x;
				if (row <= rowIndex && nextRow > rowIndex) {
					final int nextCol = col + y;
					if (col <= columnIndex && nextCol > columnIndex) {
						return cell;
					}
				}
				if (x > 1 || y > 1) {
					for (int i = 0; i < x; i++) {
						for (int j = 0; j < y; j++) {
							occupied.add(new Point(row + i, col + j));
						}
					}
				}
				col++;
			}
			row++;
		}
		return null;
	}

	

	protected WebElement queryElement(String id,String tag){
		return driver.findElement(By.id(id));	
	}


	


	public boolean attributeShouldEqual(String tag, String attribute,	String expectedValue) {
		List<WebElement> elements = driver.findElements(By.tagName(tag));
		for (WebElement element : elements) {
			String attributeValue = element.getAttribute(attribute);
			if (expectedValue.equals(attributeValue)) {
				return true;
			}
			//TODO: The src and href attributes always return full path in phantomjs, hence I am comparing the left most characters
			else if ((expectedValue.length()<attributeValue.length()) &&  expectedValue.equals(attributeValue.substring((attributeValue.length())
					-expectedValue.length()))) {
				return true;
			}
		}
		throw new AssertionError("No [" + tag + "] tag with attribute ["+ attribute + "] " + "and expected value [" + expectedValue
				+ "]");
	}


	


	public boolean shouldExist(String tagType, String nameOrId) {
		WebElement elem =  new Util_WebElementQueryHelper(driver).querySilent(nameOrId);
		if(elem!=null) return true;
		return false;
	} 


	public boolean tagWithAttributeExists(String tagName, String attribute,String attributeValue) {
		List<WebElement> elements = driver.findElements(By.tagName(tagName));
		for (WebElement element : elements) {
			if (attributeValue.equals(element.getAttribute(attribute))) {
				return true;
			}
		}
		throw new AssertionError("Tag [" + tagName + "] with attribute ["+ attribute + "] and expected value [" + attributeValue
				+ "] does not exist");
	}

	//TODO : FIXME to implement this as substitue to specialsized lib_ext methods
	public boolean tagWithIdHasAttributeValue(String tagName, String nameOrId, String attribute,String attributeValue) {
		List<WebElement> elements = driver.findElements(By.tagName(tagName));
		for (WebElement element : elements) {
			if (attributeValue.equals(element.getAttribute(attribute))) {
				return true;
			}
		}
		log.error("Tag [" + tagName + "] with attribute ["+ attribute + "] and expected value [" + attributeValue
				+ "] does not exist");
		return false;
	}
	

	public String refresh() {
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver)
			.executeScript("javascript:location.reload(true);");
		}

		return getTitle();
	}


	public String back() {

		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver)
			.executeScript("javascript:window.history.back();");
		}

		return getTitle();

	}
	public void acceptAlert(){
		try{
			driver.switchTo().alert().accept();	
		}catch(NoAlertPresentException e){

		}
	}


	public String forward() {
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver)
			.executeScript("javascript:window.history.forward();");
		}
		return getTitle();
	}

	public String getTitle() {
		String processedText = driver.getTitle().replaceAll("[\\n]",StringUtils.EMPTY);		
		String parsedText = Jsoup.parse(  KeyWords_Utils.findAndReplaceNonAsciiChars(processedText.toCharArray())).text();
		return parsedText;
	}
	//TODO NEW
	public String getCurrentUrl(){
		String url = driver.getCurrentUrl();
		return url;
	}



	public String getText(String xpath) throws  Exception{

		WebElement elem = new Util_WebElementQueryHelper(driver).query(xpath);
		String text = elem.getText();
		if (text == null) {
			throw new AssertionError("Could not find text at [" + xpath + "]");
		}
		return text;
	}


	public String getAttributeAsText(String xpath, String attrName) throws  Exception{
		WebElement elem = new Util_WebElementQueryHelper(driver).query(xpath);
		String text = elem.getAttribute(attrName);
		if (text == null) {
			throw new AssertionError("Could not find text at [" + xpath + "]");
		}
		return text;	
	}


	public void printHtml() {
		String processedText = driver.getPageSource().replaceAll("[\\n]",StringUtils.EMPTY);
		String parsedText = Jsoup.parse(  KeyWords_Utils.findAndReplaceNonAsciiChars(processedText.toCharArray())).html();
		System.err.println(parsedText);
		//		System.err.println("\n"
		//				+ findAndReplaceNonAsciiChars(processedText
		//				.toCharArray()));
	}


	public String  printPageAsText() {
		//	String processedText = driver.getPageSource().replaceAll("[\\n]",StringUtils.EMPTY);
		//String processedText = driver.getPageSource();
		String parsedText = Jsoup.parse(driver.getPageSource() ).text();
		//		CustomNodeVisitor visitor = new CustomNodeVisitor();
		//		Jsoup.parse(driver.getPageSource() ).traverse(visitor);//TODO use a visitor to generate formated text
		//		
		String clean = parsedText.replaceAll("\\P{Print}", "");
		//		String clean = visitor.toString().replaceAll("\\P{Print}", "");
		//String clean1 = visitor.toString().replaceAll("|", '\n');
		//System.err.println(clean);
		//clean = clean+inputValuesAsList();
		return clean;
	}

	public String  getPageFormData() {
		StringBuffer outputBuffer = new StringBuffer();
		Script_ControlsToMap_04 script = new Script_ControlsToMap_04();
		script.setScriptConstraint(new ScriptConstraint().setWebDriver(driver));
		script.startUp();
		//script.execute();
		Map<String,String> formMap = script.getPageDataStoreMap();
		ArrayList<String> sortedIds = new ArrayList<String>();
		sortedIds.addAll(formMap.keySet());
		Collections.sort(sortedIds);//sorting to make things sensible because different control types are obtained one after another
		outputBuffer.append(""+'\n'+'\n'+"***section:formdata***"+'\n'+'\n');
		for(String key : sortedIds){
			outputBuffer.append(formMap.get(key)+'\n');
		}

		outputBuffer.append(""+'\n'+'\n'+"***end***"+'\n'+'\n');
		log.info(outputBuffer.toString());
		return outputBuffer.toString();
	}
	
	public String  printPageAsTextFormatted() {
		//	String processedText = driver.getPageSource().replaceAll("[\\n]",StringUtils.EMPTY);
		//String processedText = driver.getPageSource();
		String parsedText = Jsoup.parse(driver.getPageSource() ).text();
		HtmlSimplificationNodeVisitor visitor = new HtmlSimplificationNodeVisitor();
				Jsoup.parse(driver.getPageSource() ).traverse(visitor);//TODO use a visitor to generate formated text
		//		
		//String clean = parsedText.replaceAll("\\P{Print}", "");
				String clean = visitor.toString();
						//.replaceAll("\\P{Print}", "");
			clean = KeyWords_Utils.findAndReplaceNonAsciiChars(clean.toCharArray());
		//String clean1 = visitor.toString().replaceAll("|", '\n');
		//System.err.println(clean);
		//clean = clean+inputValuesAsList();
		return clean;
	}

	
	
	private void xxxAttributeShouldEqual(String xxx, String forValue,
			String attributeName, String expectedValue) {
		List<WebElement> elements= driver.findElements(By.tagName(xxx));
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
		throw new AssertionError("No [label] for [" + forValue
				+ "] with attribute [" + attributeName + "] "
				+ "and expected value [" + expectedValue + "]");
	}


	public void quit() throws SPOException{
		//driver.quit();
		try{
			DriverFactory.stop();
		}catch(ServiceLifeCycleException e){
			if(DriverFactory.getState().equals(Constants.LifeCycleState.STOPPED)){
				log.info("Superflous quit call, trying to stop DriverFActory which is already in stopped state.");
			}
		}
	}

	private String previousWindowHandle;
	public void switchToNewWindow(){
		String currentWindowId = driver.getWindowHandle().toString();
		previousWindowHandle=currentWindowId;
		for(String handle : driver.getWindowHandles()){
			if (!handle.toString().equals(currentWindowId )) {
				driver.switchTo().window(handle);
			}
			
		}
		log.trace("Switched to "+getCurrentUrl());
	}

	public void switchBackToPreviousWindow(){
		for(String handle : driver.getWindowHandles()){
			if (handle.toString().equals(previousWindowHandle )) {
				driver.switchTo().window(handle);
			}
			
		}
		log.trace("Switched to "+getCurrentUrl());
	}


	


	public Object executeJavaScript(String x) {
		try{
		return ((JavascriptExecutor)driver).executeScript(x);
		}catch(Exception e){
			log.error("Error in executing java script "+x+" in page "+getCurrentUrl());
			log.debug("Retrying  ");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
				return ((JavascriptExecutor)driver).executeScript(x);
			
		}
	}


	//NEW 
	public void closeWindow(){
		try{
			((JavascriptExecutor)driver).executeScript("javascript:close_this_window();");
			Thread.sleep(100);
			driver.switchTo().alert().accept();	
		}catch(NoAlertPresentException e){
		}catch(InterruptedException e){
		}

	}



	public WebDriver getDriver() {		
		return driver; //FIXME how will this work in factory setup???
	}

	


	public String runOSCmd (String os, String cmd, String execDir) throws IllegalArgumentException	{
		os = StringUtils.capitalize(os);		
		if(!EnumUtils.getEnumMap(Constants.OS_Supported.class).containsKey(os)){
			throw new IllegalArgumentException("os should either be Windows or Linux", null);
		}
		try{
			return RestrictedOSCmdRouter.execArbitraryCommand(os, cmd, execDir);	
		}catch(UnPrivilagedOperationException e){
			log.error("An UnPrivilagedOperationException exception was thrown, concerning your security privilages to execute this cmd");
		}catch(SPOException e){
			log.error("A SPOException exception was thrown, probably your cmd does not meet application rules");
		}catch(Exception e){
			log.error("A system exception was thrown");
			//log.debug(e);
		}
		return Constants.ROBOT_FAIL;
		
	}
	
	public String getApplicationStartupParam(String key) {
		return System.getProperty(key);
	}
	
	public WebElement findElement(String clue) throws Exception{
		WebElement elem = new Util_WebElementQueryHelper(driver).query(clue);
		return elem;
	}
	
}
