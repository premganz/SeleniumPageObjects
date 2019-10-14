/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.meta.fixture.page;

import org.spo.fw.meta.fixture.StubPageFactoryImpl;
import org.spo.fw.navigation.svc.ApplicationNavModelGeneric;

public class StubNavModel extends ApplicationNavModelGeneric{
@Override
public void init() {
	resourcePath="/ApplicationNavTreeModelGeneric.xml";
	factory = new StubPageFactoryImpl();
	appHeirarchyDoc=parseDoc(resourcePath);
}
}
