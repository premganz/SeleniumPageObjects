package org.spo.fw.utils.pg.itf;

import org.spo.fw.utils.pg.model.FileContent;

public interface StaticContentProcessor {
	//Split by path delimiters
		FileContent getFileContent( String expression );
		void setStaticContentProvider(StaticContentProvider provider);
		
}
