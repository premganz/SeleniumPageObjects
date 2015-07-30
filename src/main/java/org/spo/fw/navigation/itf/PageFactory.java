package org.spo.fw.navigation.itf;

import org.spo.fw.exception.SPOException;

public interface PageFactory {
	
	Page getPage(String name) throws SPOException;
	Page getDefaultPage();
	Page getMultiPage();
}
