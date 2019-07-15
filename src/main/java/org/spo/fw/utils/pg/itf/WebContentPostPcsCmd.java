package org.spo.fw.utils.pg.itf;

import org.spo.fw.web.ServiceHub;

public interface WebContentPostPcsCmd {

	void execute(String metaData, String content, ServiceHub toolkit);
}
