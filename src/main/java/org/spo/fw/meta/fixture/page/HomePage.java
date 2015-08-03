package org.spo.fw.meta.fixture.page;

import org.spo.fw.config.AppConfig;
import org.spo.fw.web.KeyWords;

public class HomePage extends StubDefaultPage {

	@Override
	public void setState(String stateExpression) {
		kw.goToPage("http://en.wikipedia.org");
	}


}
