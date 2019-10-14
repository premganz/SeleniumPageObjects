/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.model;

import java.util.List;

import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.web.ServiceHub;

public class MongoStaticContentProvider implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	@Override
	public List<String> getContent(String expression, ServiceHub kw) {
		return core_getFileFromDB(  expression);
	}

	//Split by path delimiters
	public List<String> core_getFileFromDB( String expression) {
		return null; //Intentionally left null,to implement by framework users.
	}

}
