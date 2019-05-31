package org.spo.fw.utils.pg.itf;

import org.spo.fw.web.ServiceHub;

public interface WebContentProvider {

	public String getPageContent( String pageName,  ServiceHub kw) ;
	public String getPageContentFormatted( String pageName,  ServiceHub kw) ;
//	public String preProcessContent(String pageName, ServiceHub kw);
//	public String postProcessContent();
	
}
