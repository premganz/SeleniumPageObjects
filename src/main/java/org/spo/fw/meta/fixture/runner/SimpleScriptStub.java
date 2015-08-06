package org.spo.fw.meta.fixture.runner;

import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.meta.fixture.StubPageFactoryImpl;
import org.spo.fw.meta.fixture.page.StubNavModel;
import org.spo.fw.navigation.itf.NavigationServiceProvider;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.navigation.svc.ApplicationNavModelGeneric;
import org.spo.fw.selenium.JunitScript;

public class SimpleScriptStub extends JunitScript {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		kw.setNavContainer(new StubNavContainer());
		super.init();
		
	}

}
