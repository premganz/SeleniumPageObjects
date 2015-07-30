package org.spo.fw.navigation.model;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.itf.NavLink;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.navigation.util.StateExpressionWrapper;
import org.spo.fw.web.KeyWords;


public abstract class BasePage implements Page{
	protected String name;
	protected String url;
	String state;
	protected KeyWords kw ;
	protected StateExpressionWrapper exp ;
	protected Logger1 log = new Logger1(this.getClass().getSimpleName());
	protected List<NavLink> linksOnPage;
	private boolean isStateSet;
	private boolean isLastPage;
	protected boolean ready;
	//implicit wait.	
	protected String identifier="";
	protected int sleepTime=1000;
	protected int timesToTry=0;
	protected PageLoadType pageLoadType;
	
	protected enum PageLoadType {SLOW_VERYSLOW, SLOW_LOADSLOW, SLOW_MOSTLY, SLOW_SOMETIMES,FAST_ALWAYS,KINDOF_AVERAGE, EXTREME_SLOW};
//1.long poll, long timout, 2. short poll, long timeout 3. shortpoll, short timeout 4.medium poll, medium timout
	
	
	protected void configureReadiness(String identifer, int sleepTime, int timesToTry){
		this.identifier=identifer;
		this.sleepTime=sleepTime;
		this.timesToTry=timesToTry;
	}

	public boolean isReady(){
		if(pageLoadType==PageLoadType.SLOW_VERYSLOW){
			timesToTry=10;
			sleepTime=1500;
		}else if(pageLoadType==PageLoadType.SLOW_MOSTLY){
			timesToTry=8;
			sleepTime=2000;
		}else if(pageLoadType==PageLoadType.EXTREME_SLOW){
			timesToTry=8;
			sleepTime=5000;
		}else if(pageLoadType==PageLoadType.SLOW_SOMETIMES){
			timesToTry=6;
			sleepTime=500;
		}else if(pageLoadType==PageLoadType.SLOW_MOSTLY ||pageLoadType==PageLoadType.SLOW_LOADSLOW){
			timesToTry=5;
			sleepTime=2000;
		}else {
			timesToTry=1;
			sleepTime=500;
		}
		try{
			if(kw.pageShouldContain(identifier)){
				if(pageLoadType==PageLoadType.SLOW_LOADSLOW){
					Thread.sleep(sleepTime);
					Thread.sleep(sleepTime);
					Thread.sleep(sleepTime);
				}
						
				return true;
			}else{
				while(timesToTry>0){
					timesToTry--;
					Thread.sleep(sleepTime);
					if(kw.pageShouldContain(identifier)){
						return true;
					}
				}				
			}
		}catch(InterruptedException e){

		}catch(WebDriverException e1){

		}catch (AssertionError r){

		}
		return false;
	}

	//TODO: Going for best match
	protected void select(String nameOrId, String value){
		if(!value.isEmpty()){			
		try{	
			kw.selectOption(nameOrId, value);
		}catch(NoSuchElementException e){
			//Not giving up , going by approximate match
			int optionsCount = Integer.parseInt(kw.getOptionSize(nameOrId));
			for(int idx=0;idx<optionsCount;idx++){
				String option = kw.getOption(nameOrId, idx);
				if(option.toUpperCase().replaceAll("[/s/(/)]", "").equals(value.toUpperCase())){
					log.info("WARNING WARNING WARNING : Selecting option :"+value+" in "+nameOrId+ " by approx value at index  "+idx);
					kw.selectOptionByIndex(nameOrId, idx+"");
					break;
				}
			}
			
			
		}
		}
		
	}

	

	public void setState(String stateExpression, SessionBoundDriverExecutor executor) {		

		log.debug("Setting state  for "+name+" state: "+stateExpression);
		kw = (KeyWords)executor;
		exp = new StateExpressionWrapper(stateExpression);
		init();
		if(isReady()){
			setState(stateExpression);
		}


	}

	public abstract void init();
	
	public String getStateExpression(){
		return state;
	}

	public void setStateExpression(String state){
		this.state = state;
	}

	public abstract void setState(String stateExpression);

	public void setName(String name) {
		this.name=name;

	}

	public String getName(){
		return this.name;
	}

	public void setUrl(String url) {
		this.url= url;		
	}

	public void followLink(NavLink link,  SessionBoundDriverExecutor executor) throws NavException{
		kw = (KeyWords)executor;
		link.click(kw);
		log.debug("clicking link on page "+url+link.toString());
	}


	@Override
	public String toString() {
		return "Page:"+this.name ;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public void switchWebDriverContextToPage(KeyWords kw) {
		this.kw=kw;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return identifier;
	}
	
	@Override
	public String getFormData(KeyWords kw) {	
		return "";
	}

}
