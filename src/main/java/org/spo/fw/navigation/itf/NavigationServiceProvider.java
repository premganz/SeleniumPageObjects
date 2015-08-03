package org.spo.fw.navigation.itf;

import org.spo.fw.web.KeyWords;


public interface NavigationServiceProvider{		
		void navigateToUrlFromHome(String destUrl, KeyWords kw) throws Exception;
		public void setModel(ApplicationNavigationModel model);
		
	}