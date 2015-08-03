package org.spo.test.page;


import org.junit.Test;
import org.spo.fw.meta.fixture.runner.SimpleScriptStub;
import org.spo.fw.meta.fixture.runner.TestRunnerTemplate;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.itf.PageFactory;

public class Test_PageFactory {
	
	
	
	
	//Testing with multiple files 
	
	@Test
	public void setStateForPage() throws Exception{TestRunnerTemplate.runTest(new TestPageFactory());	}
	
	class TestPageFactory extends SimpleScriptStub{
		@Override
		public void execute() throws Exception {
			kw.page_event("Culture", "SomeState:test=local");
			kw.navigateByName("Culture");
			
			PageFactory injectedFactory=kw.impl_page.getPageFactory();
			Page page = injectedFactory.getPage("Culture");
			String formData = page.getFormData(kw);
			log.info(formData);
			failed = !formData.contains("Wikipedia");
		}
	}
	
	}
