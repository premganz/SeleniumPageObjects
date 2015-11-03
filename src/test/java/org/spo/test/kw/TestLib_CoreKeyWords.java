package org.spo.test.kw;


import org.junit.Test;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.meta.fixture.runner.SimpleScriptStub;
import org.spo.fw.meta.fixture.runner.TestRunnerTemplate;
import org.spo.fw.web.Lib_Utils;
import org.springframework.util.Assert;

public class TestLib_CoreKeyWords {
			
	StubKeyWords stubLibrary;
	
	
	
	
	
	//Testing with multiple files 
	
	
	
	@Test
	public void testEnterTextById() throws Exception{TestRunnerTemplate.runTest(new TestEnterTextById());	}
	
	class TestEnterTextById extends SimpleScriptStub{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "false");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			String valueToEnter = "abcde";
			kw.navigateByName("Static");
			kw.event_current_page("Static", "H:");
			kw.enterText("gen_author_id",valueToEnter);
			String actualValue = kw.getValue("gen_author_id");
			failed = !valueToEnter.equals(actualValue);
			
		}
	}
	
	@Test
	public void testEnterTextByXPath() throws Exception{TestRunnerTemplate.runTest(new TestEnterTextByXPath());	}
	
	class TestEnterTextByXPath extends SimpleScriptStub{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "false");
		strategyParams.put("browserName", "chrome");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			String valueToEnter = "abcde";
			kw.navigateByName("Static");
			kw.event_current_page("Static", "H:");
			kw.enterText("//input{@id=gen_author_id]",valueToEnter);
			String actualValue = kw.getValue("gen_author_id");
			failed = !valueToEnter.equals(actualValue);
			
		}
	}
	

	@Test
	public void testClear() throws Exception{TestRunnerTemplate.runTest(new TestClear());	}
	
	class TestClear extends SimpleScriptStub{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "false");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			String valueToEnter = "";
			kw.navigateByName("Static");
			kw.event_current_page("Static", "H:");
			kw.utilClear("//input{@id=gen_author_id]");
			String actualValue = kw.getValue("gen_author_id");
			failed = !valueToEnter.equals(actualValue);
			
		}
	}
	
	
	@Test
	public void testClick_caseInsensitive() throws Exception{TestRunnerTemplate.runTest(new TestClick_caseInsensitive());	}
	
	class TestClick_caseInsensitive extends SimpleScriptStub{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "false");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			kw.navigateByName("Static");
			kw.click("click to Submit1");
			failed = !kw.assertPageContains("Form has been submitted");
			
		}
	}
	//@TestTODO
	public void testCSSSelector() throws Exception{
		try{
		
		String y = (String)stubLibrary.doExecuteJavaScript("return document.querySelector('#opt_ghostwriter_req[original_label_text]').getAttribute('value')");
		Assert.isTrue(y==null);
		}catch(Exception e){
			e.printStackTrace();
			Assert.isNull(e);
		}
		
	}

	
	@Test
	public void testUrlAlteration() throws Exception{TestRunnerTemplate.runTest(new TestUrlAlteration());	}
	
	class TestUrlAlteration extends SimpleScriptStub{
		@Override
		public void init() {
		strategyParams.put("requireBasicAuthUrlPrefix", "true");
			super.init();
		}
		
		@Override
		public void execute() throws Exception {
			try{
				String url = "https://null.com";
				String y = Lib_Utils.alterGotoUrl(url, null, null);
				Assert.isTrue(y.equals(url));
				 url = "http://null.com";
				y = Lib_Utils.alterGotoUrl(url, null, null);
				log.debug(y);
				Assert.isTrue(!y.equals(url));
				}catch(Exception e){
					e.printStackTrace();
					Assert.isNull(e);
				}
			
		}
	}
	
}
