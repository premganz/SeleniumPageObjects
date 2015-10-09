package org.spo.fw.utils.pg.itf;

import org.spo.fw.utils.pg.model.PageContent;
import org.spo.fw.web.KeyWords;

public interface WebContentProcessor {
	 PageContent getPageContent( String pageName,  KeyWords kw) ;
	 void setWebContentProvider(WebContentProvider provider);
}
