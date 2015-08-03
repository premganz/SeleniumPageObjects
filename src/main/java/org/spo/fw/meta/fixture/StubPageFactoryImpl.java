package org.spo.fw.meta.fixture;

import org.spo.fw.meta.fixture.page.StubDefaultPage;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.model.MultiPageImpl;
import org.spo.fw.navigation.util.PageFactoryImpl;


public class StubPageFactoryImpl extends PageFactoryImpl{

	@Override
	public void init() {
		packageName="org.spo.fw.meta.fixture.page";
	}

	public  Page getDefaultPage(){
		return new StubDefaultPage();
	}
	public  Page getMultiPage(){
		return new MultiPageImpl();
	}
}
