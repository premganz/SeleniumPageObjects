package org.spo.fw.utils.pg.model;

import java.util.List;

import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.web.ServiceHub;

public class TRSContentProvider implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	ServiceHub kw;
	@Override
	public List<String> getContent(String expression, ServiceHub kw) {
		this.kw=kw;
		return core_getFileFromServer(  expression);
	}

	//Split by path delimiters
	private List<String> core_getFileFromServer( String expression) {
		return kw.serviceFactory.getDomainSvc().getPage(expression);
		
	}




}
