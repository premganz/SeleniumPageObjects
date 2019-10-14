
/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.utils.pg.model.DefaultWebContentProvider;
import org.spo.fw.web.ServiceHub;

/**
 * 
 * @author Prem
 * 
 *
 */




public class Lib_Content_LocalRunsDiff extends Lib_PageLayout_Content{
	@Override
	public void init() {	
		super.init();
		webContentProvider.setWebContentProvider(new CachedWebContentProviderActual());
		fileContentProvider.setStaticContentProvider(new CachedWebContentProviderExpected());
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

class CachedWebContentProviderActual extends DefaultWebContentProvider {
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
}

class CachedWebContentProviderExpected implements StaticContentProvider {
	Logger1 log= new Logger1(this.getClass().getCanonicalName());
	@Override
	public List<String> getContent(String pageName, ServiceHub kw) {
		String content="";
		try {			

			String existingUrl = SessionContext.appConfig.TEST_SERVER_BASE_URL;
			SessionContext.appConfig.TEST_SERVER_BASE_URL = existingUrl.replaceAll("bx", "dx").concat("");
			if(!pageName.contains("/")){
				pageName="cache/"+pageName;
			}else {
				pageName = "cache/"+pageName.split("/")[1];
			}
			pageName=pageName+"_"+SessionContext.currentTestClass;
			content=kw.serviceFactory.getDomainSvc().getPage(pageName).toString();		
			SessionContext.appConfig.TEST_SERVER_BASE_URL=existingUrl;


		} catch (SPOException e) {			
			log.debug("Page object was not found hence proceding ");
			e.printStackTrace();
			content = kw.doPrintPageAsText();
		}
		return Arrays.stream(content.split("\n")).collect(Collectors.toList());
	}

}





