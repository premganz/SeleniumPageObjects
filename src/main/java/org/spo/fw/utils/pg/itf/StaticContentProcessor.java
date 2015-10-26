package org.spo.fw.utils.pg.itf;

import org.spo.fw.utils.pg.model.FileContent;
import org.spo.fw.web.ServiceHub;

public interface StaticContentProcessor {
	//Split by path delimiters
		FileContent getFileContent( String expression,ServiceHub kw  );
		void setStaticContentProvider(StaticContentProvider provider);
		
}
