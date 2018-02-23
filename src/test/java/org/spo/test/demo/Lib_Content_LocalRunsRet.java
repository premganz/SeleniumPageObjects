package org.spo.test.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.utils.Utils_PageDiff;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.utils.pg.itf.WebContentProvider;
import org.spo.fw.utils.pg.model.DefaultWebContentProvider;
import org.spo.fw.web.ServiceHub;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * 
 * @author Prem
 * 
 *
 */




public class Lib_Content_LocalRunsRet extends Lib_PageLayout_Content{
	@Override
	public void init() {	
		super.init();
		webContentProvider.setWebContentProvider(new CachedWebContentProviderLocal());
	}


}

//class CachedContentProvider_local implements StaticContentProvider {
//	Logger1 log = new Logger1(this.getClass().getSimpleName());
//	ServiceHub kw;
//	//@Override
//	public List<String> getContent(String expression, ServiceHub kw) {
//		this.kw=kw;
//		return core_getFileFromServer(  expression+"_"+SessionContext.currentTestClass);
//	}
//
//	//Split by path delimiters
//	private List<String> core_getFileFromServer( String expression) {
//		return kw.serviceFactory.getDomainSvc().getPage(expression+"_"+SessionContext.currentTestClass);
//		
//	}
//
//
//}

class CachedWebContentProviderLocal extends DefaultWebContentProvider {
	Logger1 log= new Logger1(this.getClass().getCanonicalName());
	@Override
	public String getPageContent(String pageName, ServiceHub kw) {

	String content;

	try {			
		
		content = super.getPageContentFormatted(pageName, kw);
		String existingUrl = SessionContext.appConfig.TEST_SERVER_BASE_URL;
		SessionContext.appConfig.TEST_SERVER_BASE_URL = existingUrl.replaceAll("bx", "cx").concat("");
		if(!pageName.contains("/")){
			pageName="cache/"+pageName;
		}
		 pageName=pageName+"_"+SessionContext.currentTestClass;
		 content=kw.serviceFactory.getDomainSvc().getPage(pageName).toString();		
		SessionContext.appConfig.TEST_SERVER_BASE_URL=existingUrl;
		

	} catch (SPOException e) {			
		log.debug("Page object was not found hence proceding ");
		e.printStackTrace();
		content = kw.doPrintPageAsText();
	}


	return content;


}


public String util_getPageText(String pageText){
	
	return UrlUtils.encodeAnchor(pageText);
}}



