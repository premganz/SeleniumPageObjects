/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.navigation.itf;

import org.spo.fw.itf.SessionBoundDriverExecutor;

public interface NavLink {
	void setNavType(String navTypeExpression);//id, xpath or other
	void setNavId(String id);
	
	void setName(String name);	
	String getName();	
	
	void click(SessionBoundDriverExecutor executor) throws NavException;
}
