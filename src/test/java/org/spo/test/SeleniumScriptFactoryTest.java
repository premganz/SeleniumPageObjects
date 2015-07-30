package org.spo.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.spo.fw.itf.SeleniumScriptParametrized;
import org.spo.fw.selenium.SeleniumScriptFactory;


public class SeleniumScriptFactoryTest {
	@Before
	public void setUp() {

	}

	@Test
	public void testInstance() {
		String scriptName = "Script_CheckSaveAbstract_4a";
		SeleniumScriptParametrized script = (SeleniumScriptParametrized) SeleniumScriptFactory
				.instance(scriptName);
		assertNotNull(script);
	}

	@Test
	public void testReadTestSuite() {

	}

}
