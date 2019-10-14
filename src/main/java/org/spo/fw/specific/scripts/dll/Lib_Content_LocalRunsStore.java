/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.dll;

import java.util.List;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.utils.pg.model.DefaultWebContentProvider;
import org.spo.fw.web.ServiceHub;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * 
 * @author Prem
 * The test domain is Upload 
 * This is a Non destructive test 
 *
 */








class CachedContentProviderLocal implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	ServiceHub kw;
	//@Override
	public List<String> getContent(String expression, ServiceHub kw) {
		this.kw=kw;
		return core_getFileFromServer(  expression);
	}

	//Split by path delimiters
	private List<String> core_getFileFromServer( String expression) {
		return kw.serviceFactory.getDomainSvc().getPage(expression);
		
	}


}


class CacheingWebContentProviderLocal extends DefaultWebContentProvider {
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
		kw.serviceFactory.getDomainSvc().setPage(pageName+"_"+SessionContext.currentTestClass, content);		
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


public class Lib_Content_LocalRunsStore extends Lib_PageLayout_Content{
	@Override
	public void init() {			
		super.init();
		webContentProvider.setWebContentProvider(new CacheingWebContentProviderLocal());

	}


}





