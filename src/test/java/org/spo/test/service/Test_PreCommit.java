/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.test.service;


import java.util.Set;

import org.junit.Test;
import org.spo.fw.service.precommit.PreCommitScanner;

public class Test_PreCommit {

	
	
	//Testing with multiple files 
	
	@Test
	public void preCommitTest() throws Exception{
		PreCommitScanner scanner = new PreCommitScanner();
		Set tempCheckouts = scanner.scan("org.spo");
		 if(tempCheckouts.size()>10){
	    	 throw new AssertionError("Found  TempCheckout Annotaiton on "+tempCheckouts.size()+" classes");
	     }
		
	}
	
	}
