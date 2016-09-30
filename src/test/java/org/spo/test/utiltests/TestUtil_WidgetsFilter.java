package org.spo.test.utiltests;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.spo.fw.meta.fixture.StubWebElementRepository;
import org.spo.fw.specific.scripts.utils.Util_WidgetsFilterMYP;
import org.spo.fw.specific.scripts.utils.Util_WidgetsFilterMYP.Workflow_script2;



public class TestUtil_WidgetsFilter {


	
	@Test
	public void testFilter_2() throws Exception {
		 Util_WidgetsFilterMYP.Workflow_script2 filterWorkflow2 = new Util_WidgetsFilterMYP().new Workflow_script2();
		 filterWorkflow2.step1_setContext(false, "checkbox");
		 WebElement elem = new StubWebElementRepository.Builder().id("xx_xx_date").tagName("input").type("checkbox").build();
		boolean isBlocked = filterWorkflow2.step2_configRunFilter(elem);
		 Assert.assertTrue(!isBlocked);
		
	}


}
