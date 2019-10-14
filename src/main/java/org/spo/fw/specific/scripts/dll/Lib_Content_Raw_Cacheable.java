/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.dll;

import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.model.DefaultWebContentProvider;
import org.spo.fw.web.ServiceHub;

class RawHtmlContentProvider_Cacheable extends DefaultWebContentProvider {
	Logger1 log= new Logger1(this.getClass().getCanonicalName());
	@Override
	public String getPageContent(String pageName, ServiceHub kw) {
		String content;
		try {			
			
			content = kw.getDriver().getPageSource();
			String existingUrl = SessionContext.appConfig.TEST_SERVER_BASE_URL;
			SessionContext.appConfig.TEST_SERVER_BASE_URL = existingUrl.replaceAll("bx", "cx").concat("");
			if(!pageName.contains("/")){
				pageName="cache/"+pageName;
			}
			kw.serviceFactory.getDomainSvc().setPage(pageName, content);		
			SessionContext.appConfig.TEST_SERVER_BASE_URL=existingUrl;
			

		} catch (SPOException e) {			
			log.debug("Page object was not found hence proceding ");
			e.printStackTrace();
			content = kw.doPrintPageAsText();
		}


		return content;
	}	
}

public class Lib_Content_Raw_Cacheable extends Lib_PageLayout_Content{
	@Override
	public void init() {			
		super.init();
		webContentProvider.setWebContentProvider(new RawHtmlContentProvider_Cacheable ());

	}


}
