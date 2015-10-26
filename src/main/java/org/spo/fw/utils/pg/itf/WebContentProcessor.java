package org.spo.fw.utils.pg.itf;

import org.spo.fw.utils.pg.model.PageContent;
import org.spo.fw.web.ServiceHub;

public interface WebContentProcessor {
	 PageContent getPageContent( String pageName,  ServiceHub kw) ;
	 void setWebContentProvider(WebContentProvider provider);
}
