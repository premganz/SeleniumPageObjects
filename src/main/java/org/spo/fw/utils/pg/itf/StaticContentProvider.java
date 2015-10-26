package org.spo.fw.utils.pg.itf;

import java.util.List;

import org.spo.fw.web.ServiceHub;

public interface StaticContentProvider {
	public List<String> getContent( String expression, ServiceHub kw );
	
	
}
