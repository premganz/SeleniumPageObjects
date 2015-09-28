package org.spo.fw.navigation.itf;

import org.spo.fw.itf.SessionBoundDriverExecutor;

public interface PageLayoutValidator {

	boolean isValid(SessionBoundDriverExecutor executor); 
	boolean validateOnLoad(SessionBoundDriverExecutor executor);//validation immediately on navigation
	
}
