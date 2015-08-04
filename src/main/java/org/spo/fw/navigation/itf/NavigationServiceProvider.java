package org.spo.fw.navigation.itf;

import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.web.KeyWords;


public interface NavigationServiceProvider extends ExtensibleService{		
		void navigateToUrlFromHome(String destUrl, KeyWords kw) throws Exception;
		public void setModel(ApplicationNavigationModel model);
		
	}