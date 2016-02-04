package org.spo.fw.utils.pg.model;

import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.utils.pg.itf.WebContentProcessor;
import org.spo.fw.utils.pg.itf.WebContentProvider;
import org.spo.fw.utils.pg.util.ContentUtils;
import org.spo.fw.web.ServiceHub;

public class DefaultWebContentProcessor implements WebContentProcessor {
	private WebContentProvider webContentProvider;
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	@Override
	public PageContent getPageContent(String pageName, ServiceHub kw) {
		return core_getPageContent(pageName,kw);
	}

	@Override
	public void setWebContentProvider(WebContentProvider provider) {
		this.webContentProvider=provider;

	}


	public PageContent core_getPageContent( String pageName,  ServiceHub kw) {
		PageContent pageContent = new PageContent();
		String fromPage = webContentProvider.getPageContent(pageName, kw);			
		pageContent.contentDebug=fromPage;
		pageContent.content =ContentUtils.util_processContent(fromPage,ContentUtils.IGNORABLE_STRINGS_L2) ;
		pageContent.contentFormatted=webContentProvider.getPageContentFormatted(pageName, kw);
		return pageContent;


	}

	

}
