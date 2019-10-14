/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.web;

import org.spo.fw.itf.PluggableRobotLibrary;
import org.spo.fw.itf.SessionBoundDriverExecutor;

/**
 * We can use some system of App wide keyword bus, where all libraries could be plugged in.
This bus is responsible to delegate keyword call to appropriate implementation. It is stateful uses Webdriver Selenium
There are limitations to the way webDriver could be passed between objects hence static variables are part of 
this design.
See also a demo keyword in One of the plugged in libs that returns a command , that works. but it would not work if the plugged
in libraries are instantized for every call.
 */

/*
 * PROPOSED REFACTORING when finding surplus time, (1-Aug-14, Prem)
 * //QueryValueMessage
			//widget Type
			//widget nameOrId
			//widget xPath
			//booleancheck(compareText) > contains/selected/checked/shouldNotContain/equals/
			//count > optionSize/rowCount/columnCount
			//attribute(Attribute)  > id/name/<other>

		new QueryMessage().WidgetType.name("").boolenCheck(compareText)

	//QueryWebElementMessage
			//Use this mainly to get htmlcells from tables.

	//QueryPageMessage
			//title, textContent
			//booleanCheck(contains, notContains)

	Similarly ClickMessage, InputMessage

	//Executor
			Uplinks the messageHandler to the DriverProxy
			//MessageHandler
			//reattempt?
			//visibleBrow?
			//alertStrategy?
			//mutatingOperation?

	//MessageHandler
		//Handles differentMessages Differently, has find and other oft used methods internally.

	//DriverAccessProxy
		//ExecutorEntryPoint
		//Logger, RecoveryCopy(?)
 * 
 */
//TODO 
public class KeyWordsExtended extends ServiceHub implements SessionBoundDriverExecutor{
	public  PluggableRobotLibrary getLibraryByName(String name){
		if(name.equals("impl")){
			return impl;
		}else if (name.equals("impl_page")){
			return impl_page;
		}else{
			throw new IllegalArgumentException("Requested an invalid library");
		}
	}
	
}
