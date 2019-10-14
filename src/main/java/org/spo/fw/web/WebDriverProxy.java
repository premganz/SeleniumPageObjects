/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.seleniumhq.jetty7.util.log.Log;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.DriverFactory;


public class WebDriverProxy implements WebDriver  {
	WebDriver driver_1;
	//WebDriver driver_2;
	List<DriverCommand> commandSequence = new ArrayList<DriverCommand>() ;
	public WebDriverProxy(WebDriver main, WebDriver backup) {
		driver_1=main;
		//driver_2=backup;
	}
	public WebDriverProxy(WebDriver main) {
		driver_1=main;
	}
	Logger1 log = new Logger1("WebDriverPRoxy");
	
	class DriverCommand{
		String method;
		Object arg;
		DriverCommand(String method, Object arg){
			this.method=method;this.arg=arg;
		}
		@Override
		public String toString() {
		if(arg!=null){
			return method+":"+arg.getClass().getSimpleName()+":"+arg.toString();
		}else{
			return method+":"+"NULL";
		}
		}
	}
	public WebDriver handleException(Exception e){
		
		e.printStackTrace();
		log.info("HANDLING WITH BACKUP ");
		try {
			driver_1.quit();
			driver_1 = DriverFactory.instance();
		} catch (SPOException e1) {
			log.info("BUT NOT ABLE to due to this:");
			e1.printStackTrace();
		}
		commandSequence.remove(commandSequence.size()-1);//removing last log since failure happened
		for(DriverCommand cmd : commandSequence){
			try{
			Thread.sleep(1000);
			}catch(Exception e1){
				e1.printStackTrace();
			}
			switch (cmd.method){
			case "get":
				driver_1.get((String)cmd.arg);break;
			case "getCurrentUrl":
				driver_1.getCurrentUrl();break;
			case "getTitle":
				driver_1.getTitle();break;
			case "findElements":
				driver_1.findElements((By)cmd.arg);break;
			case "findElement":
				driver_1.findElement((By)cmd.arg);break;
			case "getPageSource":
				driver_1.getPageSource();break;
			}

		}

		return driver_1;
	}


	@Override
	public void get(String url) {	
		try{
			commandSequence.add(new DriverCommand("get",url));
			driver_1.get(url);
		}catch(UnreachableBrowserException | NoSuchWindowException e){
			handleException(e).get(url);

		}

	}
	@Override
	public String getCurrentUrl() {
		try{
			commandSequence.add(new DriverCommand("getCurrentUrl",null));
			return driver_1.getCurrentUrl();
		}catch(UnreachableBrowserException | NoSuchWindowException e){
			return handleException(e).getCurrentUrl();
		}
	}

	@Override
	public String getTitle() {
		try{
			commandSequence.add(new DriverCommand("getTitle",null));
			return driver_1.getTitle();
		}catch(UnreachableBrowserException | NoSuchWindowException e){
			return handleException(e).getTitle();
		}
	}

	@Override
	public List<WebElement> findElements(By by) {
		try{
			commandSequence.add(new DriverCommand("findElements",by));
			return driver_1.findElements(by);
		}catch(UnreachableBrowserException | NoSuchWindowException e){
			return handleException(e).findElements(by);
		}
	}
	@Override
	public WebElement findElement(By by) {
		try{
			commandSequence.add(new DriverCommand("findElement",by));
			return driver_1.findElement(by);
		}catch(UnreachableBrowserException | NoSuchWindowException e){
			return handleException(e).findElement(by);
		}
	}



	@Override
	public String getPageSource() {
		try{
			commandSequence.add(new DriverCommand("getPageSource",null));
			return driver_1.getPageSource();
		}catch(UnreachableBrowserException | NoSuchWindowException e){
			return handleException(e).getPageSource();
		}
	}	
	@Override
	public void close() {
		driver_1.close();
	//	if(driver_2!=null)driver_2.close();

	}
	@Override
	public void quit() {
		driver_1.quit();
		//if(driver_2!=null)driver_2.quit();

	}
	@Override
	public Set<String> getWindowHandles() {
		return driver_1.getWindowHandles();		
	}
	@Override
	public String getWindowHandle() {
		return driver_1.getWindowHandle();	
	}
	@Override
	public TargetLocator switchTo() {
		return driver_1.switchTo();
	}
	@Override
	public Navigation navigate() {
		return driver_1.navigate();
	}
	@Override
	public Options manage() {
		return driver_1.manage();
	}


	@Override
	public String toString() {
		return driver_1.toString()+" and "+"a backup driver"
				;
	}


}
