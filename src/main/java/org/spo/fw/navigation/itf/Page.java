/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.navigation.itf;

import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.web.ServiceHub;

public interface Page{
		void setLastPage(boolean isLast);
		boolean isLastPage();
		public boolean isReady();//equivaluent to $.ready() in jquery.
		
		//By setting a custom validator you subscribe to the page getting validated before setting State, 
		//the validator is intended to check the runtime state of a webpage, so uses selenium runtime state.
		void setPageValidator(PageLayoutValidator validator);
		PageLayoutValidator getPageValidator();
		//boolean isValid( SessionBoundDriverExecutor executor);
		
		void setState(String stateExpression, SessionBoundDriverExecutor driver);
		void followLink(NavLink link, SessionBoundDriverExecutor executor) throws NavException;
		
		String getStateExpression();
		void setStateExpression(String stateExpression);
		
		void setName(String name);
		String getName();
		
		void setUrl(String url);		
		
		void setLoadedUnreliably(boolean a_Flag_to_check_if_aCheck_is_needed_to_confirm_loading);
		boolean isLoadedUnreliably();
		
		String getIdentifier();
		String getFormData(ServiceHub kw);
		
		void init();
	}