package org.spo.fw.navigation.itf;

import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.web.ServiceHub;


public interface NavigationServiceProvider extends ExtensibleService{		
		void navigateToUrlFromHome(String destUrl, ServiceHub kw) throws Exception;
		public void setModel(ApplicationNavigationModel model);
		
	}