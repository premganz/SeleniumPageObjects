package org.spo.fw.service.precommit;

import java.util.Set;

import org.reflections.Reflections;
import org.spo.fw.log.Logger1;

public class PreCommitScanner {
Logger1 log = new Logger1("PreCommitScanner");
	public Set scan(String packagePrefix) {// Get a List with all classes annotated with @RuntimeVisibleTestAnnotation (Java 8 syntax)
		
	     //or using the ConfigurationBuilder	
		
		 Reflections reflections = new Reflections(packagePrefix);
	     Set<Class<?>> tempCheckouts =             reflections.getTypesAnnotatedWith(TempCheckout.class);
	     //System.out.println(tempCheckouts);
	     log.error(tempCheckouts.toString());
	    
	   
	     return tempCheckouts;
	}
	
}
