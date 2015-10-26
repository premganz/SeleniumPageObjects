package org.spo.fw.web;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;



public class Lib_NavUtils extends Lib_KeyWordsCore{
	ApplicationNavContainerImpl navContainer = new ApplicationNavContainerImpl();
	

	public Lib_NavUtils(WebDriver driver) {
		super(driver);
	}

	public void init() throws Exception{
		navContainer.init();
	}
	
	public void setPageEvent(String page, String stateExpression) {
		navContainer.getDefaulModel().changePageState(page, stateExpression);
	}
	public void setDomainEvent(String actor, String eventExpression) {
		navContainer.getDefaulModel().changePageState(actor, eventExpression);
	}
	public void setlinkStrategy(String linkName, String navExpression) {
		navContainer.getDefaulModel().changeLinkVal(linkName,navExpression);
	}

	//
	public void navigateByName(String pageName, ServiceHub kw) {
		if(pageName.contains("/")){
			String[] arr_pageName=pageName.split("/");
			int len = arr_pageName.length;
			pageName=arr_pageName[len-1];
		}
		else if(pageName.contains("\\")){
			String[] arr_pageName=pageName.split("\\");
			int len = arr_pageName.length;
			pageName=arr_pageName[len-1];
		}
		
		try{
			navContainer.navigateToUrlByName(pageName, kw);
		}catch(NavException e){
			log.error("NavException while trying to navigateByName (either in navigation or setting state) for  "+pageName);
			throw e;
		}
	}
	
	//
	public void setCurrentPageEvent(String pageName, String stateExpression, ServiceHub kw) {
		if(pageName.contains("/")){
			String[] arr_pageName=pageName.split("/");
			int len = arr_pageName.length;
			pageName=arr_pageName[len-1];
		}
		else if(pageName.contains("\\")){
			String[] arr_pageName=pageName.split("\\");
			int len = arr_pageName.length;
			pageName=arr_pageName[len-1];
		}
		
		try{
			navContainer.changeLastPageState(pageName, stateExpression, kw);
		}catch(NavException e){
			log.error("link not found ");
		}
	}

	public List<String> queryAppDocModel(String query, String attr) throws NavException {
		return navContainer.queryAppDocModel(query, attr);
	}

	
	public ApplicationNavContainerImpl getNavContainer() {
		return navContainer;
	}

	public void setNavContainer(ApplicationNavContainerImpl navContainer) {
		this.navContainer = navContainer;
	}

	
	
}
