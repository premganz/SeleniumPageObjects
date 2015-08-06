package org.spo.fw.meta.fixture.runner;

import org.spo.fw.meta.fixture.StubPageFactoryImpl;
import org.spo.fw.meta.fixture.page.StubNavModel;
import org.spo.fw.selenium.JunitScript;

public class SimpleScriptStubStatic extends JunitScript {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		kw.setNavContainer(this.new StubNavigationContainerStatic());		
		super.init();
	}


	class StubNavigationContainerStatic extends StubNavContainer{
		@Override
		public void init() {
			model=new StubNavModelStatic();
			model.init();

		}
	}


	class StubNavModelStatic extends StubNavModel{
		@Override
		public void init() {
			resourcePath="/StubNavTreeModelGeneric.xml";
			factory = new StubPageFactoryImpl();
			appHeirarchyDoc=parseDoc(resourcePath);
		}
	}

}
