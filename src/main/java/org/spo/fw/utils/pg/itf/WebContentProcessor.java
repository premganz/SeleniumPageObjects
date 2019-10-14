/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.itf;

import org.spo.fw.utils.pg.model.PageContent;
import org.spo.fw.web.ServiceHub;

public interface WebContentProcessor {
	 PageContent getPageContent( String pageName,  ServiceHub kw) ;
	 void setWebContentProvider(WebContentProvider provider);
	 void setWebContentPostProcessor(WebContentPostPcsCmd cmd);
}
