/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.specific.scripts.utils;

import java.util.List;

import org.openqa.selenium.WebDriverException;
import org.spo.fw.exception.SPOException;


public class Util_navHelper {

	//calls on ApplicaitonTreeModel and arrives at Page set. executes them by picking up NavStep (and fallback navSteps)
	//and calls them seq. finally executes Page.setup
	interface NavigationTask{
		void navigate(String url, DriverInstructionSet destPageModifier) throws NavFailedException;
	}
	
	interface Page{
		void setUpState(DriverInstructionSet instructionSet);
	}
	
	interface DriverInstructionSet{
		void execute() throws WebDriverException;
	}
	
	
	interface NavStepAssembler{
		//key is : Pagestart=>Pageend
		DriverInstructionSet getNavSteps(String key);
		DriverInstructionSet getAltNavSteps(String key);
	}

	interface ApplicationNavTreeModel{		
		List<Page> getPageSequence(String destUrl);
	}
	
	
	abstract class NavFailedException extends SPOException{
		
	}
	
	class NavStepAssmeblerImpl implements NavStepAssembler{

		@Override
		public DriverInstructionSet getNavSteps(String key) {
			
			return null;
		}

		@Override
		public DriverInstructionSet getAltNavSteps(String key) {
			
			return null;
		}
		
	}
	
	
	
}
