/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.model;

import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.utils.pg.itf.WebContentProvider;
import org.spo.fw.utils.pg.util.IgnorableTextUtils;
import org.spo.fw.web.ServiceHub;

public class DefaultWebContentProvider implements WebContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	@Override
	public String getPageContent(String pageName, ServiceHub kw) {

		String content;
	
		try {			
			Page page = kw.impl_nav.getNavContainer().getModel().getFactory().getPage(pageName);
			String identifier = page.getIdentifier();
			String formKeyVals = page.getFormData(kw);
			String fromPage = kw.doPrintPageAsText();			
			String pageText = util_getPageText(fromPage,  identifier);
			content = pageText+formKeyVals;

		} catch (SPOException e) {			
			log.debug("Page object was not found hence proceding ");
			e.printStackTrace();
			content = kw.doPrintPageAsText();
		}


		return content;


	}

	
	@Override
	public String getPageContentFormatted(String pageName, ServiceHub kw) {
		String content;
		
		try {			
			Page page = kw.impl_nav.getNavContainer().getModel().getFactory().getPage(pageName);
			String identifier = page.getIdentifier();
			String formKeyVals = page.getFormData(kw);
			String fromPage = kw.doPrintPageAsTextFormatted();			
			//String pageText = util_getPageText(fromPage,  identifier);
			content = fromPage+formKeyVals;

		} catch (SPOException e) {			
			log.debug("Page object was not found hence proceding ");
			e.printStackTrace();
			content = kw.doPrintPageAsText();
		}


		return content;
	}
	
	//To use only first instance of the headLine
	public String util_getPageText(String pageText, String identifier){
		String identifier_sansSpace = identifier.trim();
		String toReturn = "";
		pageText=(IgnorableTextUtils.util_processContent(pageText,IgnorableTextUtils.IGNORABLE_STRINGS_L2));
		if(!identifier_sansSpace.isEmpty() && pageText.contains(identifier_sansSpace)){
			String[] splits= pageText.split(identifier_sansSpace);
			splits[0]="";
			StringBuffer buf = new StringBuffer();
			for(String x :splits){
				buf.append(x);
			}
			 toReturn = identifier_sansSpace+buf.toString();
		}else{
			 toReturn = pageText;
			
		}
return toReturn;
	}
	
	
	
}
