package org.spo.fw.navigation.itf;

import org.spo.fw.exception.SPOException;

public interface PageFactory {
	
	Page getPage(String name) throws SPOException;
	Page getDefaultPage(String name) throws SPOException;
	Page getMultiPage();
	void addValidator(String pageNameRegex, PageLayoutValidator validator);//By setting a validator for a factory, you subscribe to each page created therein to be validated against this.
	void removeValidator(String pageNameRegex);
}
