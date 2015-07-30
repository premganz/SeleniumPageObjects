package org.spo.fw.navigation.itf;

import org.spo.fw.itf.SessionBoundDriverExecutor;

//calls on ApplicaitonTreeModel and arrives at Page set. executes them by picking up NavStep (and fallback navSteps)
	//and calls them seq. finally executes Page.setup
	public interface NavigationTask{
		void navigate(SessionBoundDriverExecutor kw) throws NavException ;
		
		void setPage(Page page);
		void setLink(NavLink link);
		
		Page getPage();
		NavLink getLinkToFollow();

		Page getTargetPage();
	}