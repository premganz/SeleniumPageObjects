package org.spo.fw.navigation.itf;

import org.spo.fw.web.KeyWords;


public interface ApplicationNavContainer{		
		void navigateToUrlFromHome(String destUrl, KeyWords kw) throws Exception;
		
	}