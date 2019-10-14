/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.itf;

import org.spo.fw.utils.pg.model.FileContent;
import org.spo.fw.web.ServiceHub;

public interface StaticContentProcessor {
	//Split by path delimiters
		FileContent getFileContent( String expression,ServiceHub kw  );
		void setStaticContentProvider(StaticContentProvider provider);
		
}
