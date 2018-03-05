package org.spo.fw.specific.scripts.setup;

import org.spo.fw.config.SessionContext;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.web.ServiceHub;



public class Lib_Content_Downloads_Cacheable extends Lib_PageLayout_Content{
	@Override
	public void init() {			
		super.init();			
		webContentProvider.setWebContentProvider(new DownloadedFileContentProviderCacheing());
	}
}

class DownloadedFileContentProviderCacheing extends DownloadedFileContentProvider{
	
	public String getPageContent(String pageName, ServiceHub kw) {
		String toReturn=super.getPageContent(pageName, kw);
		String existingUrl=SessionContext.appConfig.TEST_SERVER_BASE_URL;
		SessionContext.appConfig.TEST_SERVER_BASE_URL = existingUrl.replaceAll("bx", "cx").concat("");
		if(!pageName.contains("/")){
			pageName="cache/"+pageName;
		}
		kw.serviceFactory.getDomainSvc().setPage(pageName+"_"+SessionContext.currentTestClass, toReturn);		
		SessionContext.appConfig.TEST_SERVER_BASE_URL=existingUrl;
		return toReturn;
	}

	

	
}

class DownloadedFileContentProviderCached extends DownloadedFileContentProvider{
	
	public String getPageContent(String pageName, ServiceHub kw) {
		String toReturn=super.getPageContent(pageName, kw);
		String existingUrl=SessionContext.appConfig.TEST_SERVER_BASE_URL;
		SessionContext.appConfig.TEST_SERVER_BASE_URL = existingUrl.replaceAll("bx", "cx").concat("");
		if(!pageName.contains("/")){
			pageName="cache/"+pageName;
		}
		kw.serviceFactory.getDomainSvc().setPage(pageName+"_"+SessionContext.currentTestClass, toReturn);		
		SessionContext.appConfig.TEST_SERVER_BASE_URL=existingUrl;
		return toReturn;
	}

}
