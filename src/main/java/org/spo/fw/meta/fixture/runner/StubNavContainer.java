/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.meta.fixture.runner;

import org.spo.fw.meta.fixture.page.StubNavModel;
import org.spo.fw.navigation.svc.ApplicationNavContainerImpl;

public class StubNavContainer extends ApplicationNavContainerImpl {

	@Override
	public void init() {
		model=new StubNavModel();
		model.init();
	
	}
}
