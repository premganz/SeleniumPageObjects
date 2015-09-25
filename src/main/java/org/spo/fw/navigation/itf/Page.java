package org.spo.fw.navigation.itf;

import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.web.KeyWords;

public interface Page{
		void setLastPage(boolean isLast);
		boolean isLastPage();
		public boolean isReady();//equivaluent to $.ready() in jquery.
		
		//By setting a custom validator you subscribe to the page getting validated before setting State, 
		//the validator is intended to check the runtime state of a webpage, so uses selenium runtime state.
		void setPageValidator(PageLayoutValidator validator);
		boolean isValid( SessionBoundDriverExecutor executor);
		
		void setState(String stateExpression, SessionBoundDriverExecutor driver);
		void followLink(NavLink link, SessionBoundDriverExecutor executor) throws NavException;
		
		String getStateExpression();
		void setStateExpression(String stateExpression);
		
		void setName(String name);
		String getName();
		
		void setUrl(String url);		
		
		
		String getIdentifier();
		String getFormData(KeyWords kw);
		
		void init();
	}