package org.spo.fw.meta.fixture.runner;

import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.meta.fixture.StubPageFactoryImpl;
import org.spo.fw.meta.fixture.page.StubNavModel;
import org.spo.fw.navigation.itf.NavigationServiceProvider;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;
import org.spo.fw.navigation.svc.ApplicationNavModelGeneric;
import org.spo.fw.selenium.JunitScript;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.Lib_PageLayout_Processor;
import org.spo.fw.utils.pg.model.TRSContentProvider;

public class SimpleScriptStub extends JunitScript {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		kw.impl_nav.setNavContainer(new StubNavContainer());
//		kw.setContentProvider(new Lib_PageLayout_Content() {
//			@Override
//			public void init() {
//				super.init();
//				fileContentProvider.setStaticContentProvider(new TRSContentProvider());
//			}
//			
//		});
		super.init();
		
	}

}
