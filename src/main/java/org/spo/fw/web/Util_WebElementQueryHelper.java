package org.spo.fw.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;




public class  Util_WebElementQueryHelper implements QueryAPI{
	public WebDriver driver;
	protected  Logger1 log = new Logger1("org.spo.fw.web.WebElementQuery");
	protected  String cache_elementId = StringUtils.EMPTY;
	public Util_WebElementQueryHelper(WebDriver driver){
		this.driver = driver;
	}

	public WebElement queryFailFast(String tag,String attribute, String value) {
		WebElement element = null;
		if(tag.equals("*")){
			List<WebElement> listTagWise = driver.findElements(By.xpath(".//*"));
		}

		List<WebElement> listTagWise = driver.findElements(By.tagName(tag));
		for (int i = 0; i<listTagWise.size();i++) {
			WebElement elem = listTagWise.get(i);
			String val = elem.getAttribute(attribute);
			if(elem.getAttribute(attribute).equals(value)){
				element= elem;
				break;
			}

		}
		return element;

	}


	public WebElement querySilent(String clue) {
		QueryForm form = new QueryFormImpl();
		if(clue.contains("//")){
			form.setIsXpath(true);
		}
		form.setFailStrategy(false);
		form.setElementDescriptor(clue);


		try {
			return buildWorkFlow(driver, form).dispatchResult(form);
		} catch (Exception e) {
			return null;
		}


	}



	public QueryWorkFlow buildWorkFlow(WebDriver driver, QueryForm form){
		QueryWorkFlow workflow = new QueryWorkFlowImpl();
		workflow.init(driver);
		workflow.sortQuery(form);
		workflow.delegateToHandlers(form);
		return workflow;
	}

	public WebElement query(String clue) throws Exception{
		QueryForm form = new QueryFormImpl();
		if(clue.contains("//")){
			form.setIsXpath(true);
		}
		form.setFailStrategy(true);
		form.setElementDescriptor(clue);	
		return buildWorkFlow(driver, form).dispatchResult(form);

	}


	public WebElement queryFailFast(String clue) {
		QueryForm form = new QueryFormImpl();
		if(clue.contains("//")){
			form.setIsXpath(true);
		}
		form.setFailStrategy(true);
		form.setElementDescriptor(clue);		
		try {
			return buildWorkFlow(driver, form).dispatchResult(form);
		} catch (Exception e) {		
			throw new AssertionError(e.getClass().getSimpleName()+"  was thrown for " +form.getClue());
		}

	}

	public WebElement queryClickableSilent(String clue) {
		QueryForm form = new QueryFormImpl();
		form.setClickable(true);
		if(clue.contains("//")){
			form.setIsXpath(true);
		}
		form.setFailStrategy(false);
		form.setElementDescriptor(clue);
		try {
			return buildWorkFlow(driver, form).dispatchResult(form);
		} catch (Exception e) {
			return null;
		}

	}
	public WebElement queryClickable(String clue) throws Exception {
		QueryForm form = new QueryFormImpl();
		form.setClickable(true);
		if(clue.contains("//")){
			form.setIsXpath(true);
		}
		form.setFailStrategy(true);
		form.setElementDescriptor(clue);	
		return buildWorkFlow(driver, form).dispatchResult(form);
	}

}	
interface QueryAPI{
	public WebElement queryFailFast(String tagName, String attribute , String value) ;
	public WebElement querySilent(String clue) ; // suppresses exception returns null
	public WebElement queryFailFast(String clue) ;//throws assertion error on failure
	public WebElement query(String clue) throws Exception;
	public WebElement queryClickableSilent(String clue) ;
	public WebElement queryClickable(String clue) throws Exception;
	
	//buildForm, startWorkflow, dispatchResult throws Exception are other methods.

}

interface QueryWorkFlow{
	public void init(WebDriver driver);		
	public void registerException(Exception exception);


	public void sortQuery(QueryForm form);//identify handler based on xpath or id/name and fail strategy.		
	public void delegateToHandlers(QueryForm form);

	public WebElement dispatchResult(QueryForm form) throws Exception;//which may throw exception or null for silent mode.

	//		public void throwException();
}

interface QueryHandler{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver);
	public void validateElement(QueryForm form, QueryWorkFlow flow, WebDriver driver, WebElement elem);
}

interface QueryForm{
	public String getClue();
	public void setFailStrategy(boolean isFailFast);
	public void setIsXpath(boolean isXpath);
	public void setElementDescriptor(String clue);
	public void setClickable(boolean isSo);
	public boolean isFailFast();
	public boolean isXpath();
	public boolean isClickable();	

}


abstract class QueryHandlerImpl implements QueryHandler{
	Logger1 log = new Logger1("QueryHandlerImpl");
	public void validateElement(QueryForm form, QueryWorkFlow flow, WebDriver driver, WebElement element){
		try{
			if (!element.isDisplayed()) {
				element = (new WebDriverWait(driver, SessionContext.appConfig.DISPLAY_WAIT_TIME)).until(ExpectedConditions.visibilityOf(element));
				if(!element.isDisplayed()){
					log.debug("Element is not displayed");
					element=null;
					throw new ElementNotVisibleException("Element not visible");
				}
			}}catch(StaleElementReferenceException e){
				element = handleQuery(form, flow, driver);			
				if(!element.isDisplayed() ){
					log.debug("Element is not displayed");
					element=null;
					throw new ElementNotVisibleException("Element not visible");
				}
			}

	}
	public String toString(){
		return this.getClass().getSimpleName();
	}
}

class QueryFormImpl implements QueryForm{
	String nameOrId;
	boolean isXpath;
	boolean isFailFast;
	boolean isClickable;

	public String getClue(){
		return nameOrId;		
	}
	public void setFailStrategy(boolean isFailFast) {
		this.isFailFast=isFailFast;
	}

	public void setIsXpath(boolean isXpath) {
		this.isXpath= isXpath;
	}

	public void setElementDescriptor(String clue) {
		this.nameOrId= clue;
	}
	public boolean isFailFast() {
		return isFailFast;
	}
	public boolean isXpath() {
		return isXpath;
	}
	public boolean isClickable() {
		return isClickable;
	}
	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}

}



class QueryWorkFlowImpl implements QueryWorkFlow{
	List<QueryHandler> listHandlers = new ArrayList<QueryHandler>();
	Exception exception = null;	
	WebDriver driver;
	WebElement resultElem;
	Logger1 log = new Logger1("QueryWorkFlowImpl");
	public void init(WebDriver driver){
		this.driver =driver; 
	}

	public void registerException(Exception exception) {
		this.exception=exception;
	}


	public void sortQuery(QueryForm form) {		
		if(form.isClickable()){
			log.trace("Building workflow for clickable");
			if(form.isXpath()){
				listHandlers.add(new handler_xpathQuery());
				listHandlers.add(new handler_xpathQuery_Wait());
			}else{
				listHandlers.add(new handler_NameQuery());
				listHandlers.add(new handler_IdQuery());
				listHandlers.add(new handler_linkTextQuery());
				listHandlers.add(new handler_linkTextCaseAdjustQuery());
				listHandlers.add(new handler_linkTextQuery_Wait());
				listHandlers.add(new handler_linkTextCaseAdjustQuery_Wait());
				listHandlers.add(new handler_NameQuery_Wait());
				listHandlers.add(new handler_IdQuery_Wait());
			}

		}else if(form.isXpath()){
			listHandlers.add(new handler_xpathQuery());
			listHandlers.add(new handler_xpathQuery_Wait());
		}
		else{
			listHandlers.add(new handler_NameQuery());
			listHandlers.add(new handler_IdQuery());
			listHandlers.add(new handler_NameQuery_Wait());
			listHandlers.add(new handler_IdQuery_Wait());
		}


	}

	public void delegateToHandlers(QueryForm form) {
		boolean logError=true;
		for(QueryHandler handler: listHandlers){
			try{
				WebElement elem = handler.handleQuery(form, this, driver);
				if(elem!=null){
					resultElem = elem;
					handler.validateElement(form, this, driver, resultElem);
					logError=false;
					break;
				}
			}catch (Exception e){					
				registerException(e);
				continue;
			}
		}
		if(logError){
			log.debug("Error Encountered while working with "+listHandlers.toString()+
					" for query "+form.getClue()+ " on pag "+driver.getCurrentUrl().replaceAll("http","htm"));

		}

	}

	public WebElement dispatchResult(QueryForm form) throws Exception {
		if(resultElem!=null){
			return resultElem;
		}else{
			if(form.isFailFast()){
				if(exception!=null){
					log.debug(exception.getClass().getSimpleName()+"  was thrown");
					log.trace(exception);
					throw exception;						 
				}else{
					throw new AssertionError();
				}

			} else{
				return null;
			}
		}

	}

}

class handler_IdQuery extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		return driver.findElement(By.id(form.getClue()));
	}
}

class handler_NameQuery extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		return driver.findElement(By.name(form.getClue()));
	}
}

class handler_xpathQuery extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		return driver.findElement(By.xpath(form.getClue()));
	}
}

class handler_xpathQuery_Wait extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		try{
			return (new WebDriverWait(driver, SessionContext.appConfig.SEARCH_WAIT_TIME)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(form.getClue())));
		}catch(TimeoutException e){
			log.trace(e);
			return null;
		}
	}
}

class handler_IdQuery_Wait extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		try{
			return (new WebDriverWait(driver, SessionContext.appConfig.SEARCH_WAIT_TIME)).until(ExpectedConditions.presenceOfElementLocated(By.name(form.getClue())));
		}catch(TimeoutException e){
			log.trace(e);
			return null;
		}
	}
}

class handler_NameQuery_Wait extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		try{
			return (new WebDriverWait(driver, SessionContext.appConfig.SEARCH_WAIT_TIME)).until(ExpectedConditions.presenceOfElementLocated(By.id(form.getClue())));
		}catch(TimeoutException e){
			log.trace(e);
			return null;
		}
	}
}

class handler_linkTextQuery extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		return driver.findElement(By.partialLinkText(form.getClue()));
	}
}
class handler_linkTextQuery_Wait extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		try{
			return (new WebDriverWait(driver, SessionContext.appConfig.SEARCH_WAIT_TIME)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(form.getClue())));
		}catch(TimeoutException e){
			log.trace(e);
			return null;
		}
	}
}
//TODO FIXME : Temp work around for case change of links
class handler_linkTextCaseAdjustQuery extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		return driver.findElement(By.partialLinkText(KeyWords_Utils.toDisplayCase(form.getClue())));
	}
}
class handler_linkTextCaseAdjustQuery_Wait extends QueryHandlerImpl{
	public WebElement handleQuery(QueryForm form, QueryWorkFlow flow, WebDriver driver) {
		try{
			return (new WebDriverWait(driver, SessionContext.appConfig.SEARCH_WAIT_TIME)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(KeyWords_Utils.toDisplayCase(form.getClue()))));
		}catch(TimeoutException e){
			log.trace(e);
			return null;
		}
	}
}


