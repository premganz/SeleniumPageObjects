package org.spo.fw.navigation.itf;

import java.util.List;

/**
 * Thie is actually not a single page in the browser but a set of pages.
 * Can be used to  represent a model level page that has links to several sub pages and a looping over of links from 
 * the module pages can be an effective way to test all the sub pages, as a single unit.
 * @author Prem
 *
 */
public interface MultiPage extends Page {

	void setLoopBackLink(NavLink link);
	NavLink getLoopBackLink();
	
	void setSubLinksList(List<NavLink> subLinks);
	List<NavLink> getSubLinks();
	
}
