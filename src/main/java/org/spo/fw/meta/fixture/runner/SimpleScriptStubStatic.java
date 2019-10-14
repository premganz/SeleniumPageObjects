/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.meta.fixture.runner;

import org.spo.fw.meta.fixture.StubPageFactoryImpl;
import org.spo.fw.meta.fixture.page.StubNavModel;
import org.spo.fw.selenium.JunitScript;
@Deprecated
//Use SimpleScriptStub instead
public class SimpleScriptStubStatic extends JunitScript {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		kw.impl_nav.setNavContainer(this.new StubNavigationContainerStatic());		
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
