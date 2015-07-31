package org.spo.test.utiltests;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.spo.fw.exception.SPOException;
import org.spo.fw.meta.fixture.StubKeyWords;
import org.spo.fw.service.DriverFactory;
import org.spo.fw.web.Util_WebElementQueryHelper;



public class TestUtilWebElementQuery {

	@After
	public void tearDown(){
		try {
			DriverFactory.stop();
		} catch (SPOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFilter_2() throws Exception {
		StubKeyWords kw = new StubKeyWords();
		kw.create("","");
		Util_WebElementQueryHelper helper = new Util_WebElementQueryHelper(kw.getDriver());
		try{
			helper.queryFailFast("select2");
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
		try{
			helper.queryFailFast(".//select[@id='select2']");
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
	}


}
