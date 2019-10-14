/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.itf;

import org.spo.fw.web.ServiceHub;
//TODO FIXME Unused, work in progress, need to check if a post processing is needed seperately that could be configured indpendent of where the content is extracted 
public interface WebContentPostPcsCmd {

	void execute(String metaData, String content, ServiceHub toolkit);
}
