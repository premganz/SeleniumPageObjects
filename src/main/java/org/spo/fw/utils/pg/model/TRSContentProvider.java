package org.spo.fw.utils.pg.model;

import java.util.ArrayList;
import java.util.List;

import org.spo.fw.config.SessionContext;
import org.spo.fw.config.TRSModules;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.service.TestResourceServerConnector;
import org.spo.fw.utils.pg.itf.StaticContentProvider;

public class TRSContentProvider implements StaticContentProvider {
	Logger1 log = new Logger1(this.getClass().getSimpleName());
	@Override
	public List<String> getContent(String expression) {
		return core_getFileFromServer(  expression);
	}

	//Split by path delimiters
	private List<String> core_getFileFromServer( String expression) {
		return SessionContext.appConfig.serviceFactory.getDomainSvc().getPage(expression);
		
	}




}
