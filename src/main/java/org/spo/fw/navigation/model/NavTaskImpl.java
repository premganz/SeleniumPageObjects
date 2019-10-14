/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.navigation.model;

import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.itf.NavLink;
import org.spo.fw.navigation.itf.NavigationTask;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.web.ServiceHub;

public class NavTaskImpl implements NavigationTask{

	private Page page;
	private NavLink link;
	private Page targetPage;
	public void navigate(SessionBoundDriverExecutor executor) throws NavException{
		page.setState(page.getStateExpression(), executor);
		page.followLink(link, executor);
		if(targetPage.isLastPage()){
			targetPage.setState(targetPage.getStateExpression(), executor);//TOTO duplicate calls possible.FIXME,t emp fix in basepage.
		}
	}


	public void setTargetPage(Page targetPage) {
		this.targetPage = targetPage;		
	}


	@Override
	public Page getPage() {
		return page;
	}
	@Override
	public NavLink getLinkToFollow() {
		return link;
	}
	@Override
	public void setPage(Page page) {
		this.page = page;

	}
	@Override
	public void setLink(NavLink link) {
		this.link = link;

	}

	@Override
	public String toString() {

		return this.page.toString()+" => "+this.link.toString()+":"+this.targetPage.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof NavTaskImpl){
			return ((NavTaskImpl)obj).toString().equals(this.toString());
		}
		return false;
	}


	@Override
	public Page getTargetPage() {		
		return targetPage;
	}

}
