package org.spo.fw.utils.pg;

import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.itf.PageFactory;
import org.spo.fw.web.KeyWords;



public class Lib_PageLayout_Validator {

	private PageFactory factory;
	Logger1 log = new Logger1(this.getClass().getSimpleName());


	public Lib_PageLayout_Validator(PageFactory factory) {
		this.factory=factory;
	}

	public boolean validatePage( String pageName,  KeyWords kw) {
			Page page = factory.getPage(pageName);
			if(page.getPageValidator()!=null && !page.getPageValidator().isValid(kw)){
				return false;
				
			}
			return true;

	}

	}



