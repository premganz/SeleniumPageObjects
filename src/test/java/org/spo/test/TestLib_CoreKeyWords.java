package org.spo.test;


import org.junit.Test;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.meta.fixture.runner.SimpleScriptStubStatic;
import org.spo.fw.meta.fixture.runner.TestRunnerTemplate;
import org.springframework.util.Assert;

public class TestLib_CoreKeyWords {
			
	StubKeyWords stubLibrary;
	
	
	
	
	
	//Testing with multiple files 
	
	
	
	@Test
	public void testEnterTextById() throws Exception{TestRunnerTemplate.runTest(new TestEnterTextById());	}
	
	class TestEnterTextById extends SimpleScriptStubStatic{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "false");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			String valueToEnter = "abcde";
			kw.navigateByName("Static");
			kw.curr_page_event("Static", "H:");
			kw.enterText("gen_author_id",valueToEnter);
			String actualValue = kw.getValue("gen_author_id");
			failed = !valueToEnter.equals(actualValue);
			
		}
	}
	
	@Test
	public void testEnterTextByXPath() throws Exception{TestRunnerTemplate.runTest(new TestEnterTextByXPath());	}
	
	class TestEnterTextByXPath extends SimpleScriptStubStatic{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "false");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			String valueToEnter = "abcde";
			kw.navigateByName("Static");
			kw.curr_page_event("Static", "H:");
			kw.enterText("//input{@id=gen_author_id]",valueToEnter);
			String actualValue = kw.getValue("gen_author_id");
			failed = !valueToEnter.equals(actualValue);
			
		}
	}
	

	@Test
	public void testClear() throws Exception{TestRunnerTemplate.runTest(new TestClear());	}
	
	class TestClear extends SimpleScriptStubStatic{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "false");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			String valueToEnter = "";
			kw.navigateByName("Static");
			kw.curr_page_event("Static", "H:");
			kw.clear("//input{@id=gen_author_id]");
			String actualValue = kw.getValue("gen_author_id");
			failed = !valueToEnter.equals(actualValue);
			
		}
	}
	
	
	@Test
	public void testClick_caseInsensitive() throws Exception{TestRunnerTemplate.runTest(new TestClick_caseInsensitive());	}
	
	class TestClick_caseInsensitive extends SimpleScriptStubStatic{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "false");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			kw.navigateByName("Static");
			kw.click("click to Submit1");
			failed = !kw.pageShouldContain("Form has been submitted");
			
		}
	}
	//@TestTODO
	public void testCSSSelector() throws Exception{
		try{
		
		String y = (String)stubLibrary.executeJavaScript("return document.querySelector('#opt_ghostwriter_req[original_label_text]').getAttribute('value')");
		Assert.isTrue(y==null);
		}catch(Exception e){
			e.printStackTrace();
			Assert.isNull(e);
		}
		
	}

	
	
	
	
	
}
