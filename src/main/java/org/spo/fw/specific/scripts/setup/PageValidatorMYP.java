package org.spo.fw.specific.scripts.setup;

import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.PageLayoutValidator;
import org.spo.fw.web.ServiceHub;

public class PageValidatorMYP implements PageLayoutValidator {
	Logger1 log = new Logger1("PageValidatorMYP");

	public boolean isValid(SessionBoundDriverExecutor executor) {
		ServiceHub kw = (ServiceHub)executor;
		log.trace("entering isVAlid");
		return kw.assertPageContains("wiki");
	}
	

	public boolean validateOnLoad(SessionBoundDriverExecutor executor) {
		return isValid(executor);
	}

}
