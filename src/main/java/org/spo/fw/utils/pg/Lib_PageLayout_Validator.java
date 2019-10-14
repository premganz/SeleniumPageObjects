/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg;

import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.web.ServiceHub;



public class Lib_PageLayout_Validator {

	
	Logger1 log = new Logger1(this.getClass().getSimpleName());


	

	public boolean validatePage( String pageName,  ServiceHub kw) {
		if(!SessionContext.isBrowserLess) {
			Page page = kw.impl_nav.getNavContainer().getModel().getFactory().getPage(pageName);
			if(page.getPageValidator()!=null && !page.getPageValidator().isValid(kw)){
				return false;
				
			}
			return true;
		}
		return true;
	}

	}



