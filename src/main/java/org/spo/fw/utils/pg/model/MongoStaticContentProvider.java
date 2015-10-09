package org.spo.fw.utils.pg.model;

import java.util.List;

import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.itf.StaticContentProvider;

public class MongoStaticContentProvider implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	@Override
	public List<String> getContent(String expression) {
		return core_getFileFromDB(  expression);
	}

	//Split by path delimiters
	public List<String> core_getFileFromDB( String expression) {
		return null; //Intentionally left null,to implement by framework users.
	}

}
