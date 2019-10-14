/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.spo.fw.navigation.itf.ApplicationNavigationModel;
import org.spo.fw.web.ProfileLogger;

@Aspect
public class AspectPerfoLoggerNav {
	public class EmployeeCRUDAspect {
	      
	    @Before("execution(* ApplicationNavContainerImpl.navigateToUrlByName(..)")         //point-cut expression
	    public void logBeforeV1(JoinPoint joinPoint)
	    {
	    	System.out.println("ASpect test");
	     
	    }
	    
	    @After("execution(* ApplicationNavContainerImpl.navigateToUrlByName(..)")         //point-cut expression
	    public void logAfterV1(JoinPoint joinPoint)
	    {		
	    	
	    }

	}
	
	
	
}
