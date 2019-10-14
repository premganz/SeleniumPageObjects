/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.setup;

import org.spo.fw.selenium.SeleniumScriptFactory;

public class ScriptFactoryMYP extends SeleniumScriptFactory {
	@Override
	public void init() {
		resourcePath="ScriptRegistry.xml";
	}	
}
