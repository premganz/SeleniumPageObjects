package org.spo.fw.navigation.model;

import org.spo.fw.config.SessionContext;
import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.itf.NavLink;
import org.spo.fw.navigation.util.NavExpressionWrapper;
import org.spo.fw.web.Util_WebElementQueryHelper;
import org.spo.fw.web.KeyWords;

public class NavLinkImpl implements NavLink{
	private String navType;
	private String navId;//TODO To refactor and use enum.
	private String name;
	public void setNavType(String navTypeExpression) {
		navType=navTypeExpression;

	}

	public void setNavId(String id) {
		navId=id;
	}

	public void setName(String name) {
		this.name=name;		
	}

	public void click(SessionBoundDriverExecutor executor) throws NavException{
		String navConditional=new NavExpressionWrapper(navType).getConditional();
		String navStrategy=new NavExpressionWrapper(navType).getStrategy();;
		KeyWords kw = (KeyWords)executor;
		if(!handlePreCondition(navConditional, kw)){
			return;
		}

		//handle operation	
		if(navStrategy.contains("xpath")){
			try {
				new Util_WebElementQueryHelper(kw.getDriver()).queryClickable(navId).click();
			} catch (Exception e) {
				//e.printStackTrace();
				throw new NavException("Exception during clicking "+e.getClass().getSimpleName());
			}
		}else if (navStrategy.contains("name")) {			
			kw.click(navId);	
			//TODO FIXME : how so Inelegent, fix this by changing the navExpression to json.!!
		}else if(navStrategy.contains("specific")){
			kw.clickByTagAndAttribute("a", "id", navId);
		}

		//handle postcondition
		handlePostCondition(navConditional, kw);

	}

	private boolean handlePreCondition(String navConditional, KeyWords kw){
		if(navConditional.contains("exists")){
			return kw.shouldExist("input",navId );
		}
		if (navConditional.contains("swback")){
			kw.switchBackToPreviousWindow();
			return false;
		}
		else return true;
	}

	private void  handlePostCondition(String navConditional, KeyWords kw){
		if(navConditional.contains("swwindow")){
			try {
				if(SessionContext.isVisibleBrowser)	Thread.sleep(500);
				kw.switchToNewWindow();

				if(SessionContext.isVisibleBrowser)		Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


	@Override
	public String toString() {
		return "Link : "+this.navId;
	}

	@Override
	public String getName() {
		return name;
	}
}