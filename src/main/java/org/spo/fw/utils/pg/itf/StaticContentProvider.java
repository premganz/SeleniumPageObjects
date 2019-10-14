/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.itf;

import java.util.List;

import org.spo.fw.web.ServiceHub;

public interface StaticContentProvider {
	public List<String> getContent( String expression, ServiceHub kw );
	
	
}
