/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.navigation.model;

import java.util.List;

import org.spo.fw.navigation.itf.MultiPage;
import org.spo.fw.navigation.itf.NavLink;


public class MultiPageImpl extends BasePage implements MultiPage{

	NavLink loopBackLink;
	List<NavLink> subLinksList;
	public void init(){
	}
	
	@Override
	public void setState(String stateExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoopBackLink(NavLink link) {
		this.loopBackLink=link;
		
	}

	@Override
	public NavLink getLoopBackLink() {
	return loopBackLink;
	}

	@Override
	public void setSubLinksList(List<NavLink> subLinks) {
	 this.subLinksList=subLinks;
		
	}

	@Override
	public List<NavLink> getSubLinks() {
		return subLinksList;
	}

	

}
