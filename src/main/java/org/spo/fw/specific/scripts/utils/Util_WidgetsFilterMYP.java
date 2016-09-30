package org.spo.fw.specific.scripts.utils;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.spo.fw.log.Logger1;


public class Util_WidgetsFilterMYP {
	Logger1 log = new Logger1("WidgetsFilter");
	Random rand = new Random();
	enum enumRandom {RAND_SUBJECT, RAND_CB,  RAND_CUSTOM }

	//THE PUBLIC INTERFACE
	//While chaining filters never eagerly circumvent subsequent filters, 
	//Always chain filters from broades to narrowest 
	
	public class Workflow_script2{
		FilterContext context = new FilterControllerImpl();	
		public void step1_setContext(boolean makeRandomExclusions, String type){
			//Zero is allow all , not  configured is block all
			context = new FilterControllerImpl();	
			context.configureRandomness(enumRandom.RAND_SUBJECT, 0);

			if(makeRandomExclusions){
				context.configureRandomness(enumRandom.RAND_CB, 6);
			}else{
				context.configureRandomness(enumRandom.RAND_CB, 0);			
			}
			context.configureType(type);
			
		}		
		public boolean step2_configRunFilter(WebElement elem){
			BlockingFilter headFilter = new Filter_basicExclusionRules();
			headFilter.setFilterContext(context);
			BlockingFilter filterChain = headFilter
			.chain(new Filter_extractValues_2()
			.chain(new Filter_groupExclusionRules_2()			
			.chain(new Filter_typeSpecificRules_2()
			.chain(new Filter_appSpecificRules_script2()
					))));
			return context.runFilterChain(elem, filterChain);
		}
		public String step3_optional_extractReqValues(){

			return context.extractValue("");
		}
	}
	
	public class Workflow_scriptAT{
		FilterContext context = new FilterControllerImpl();	
		public void step1_setContext(boolean makeRandomExclusions, String type){
			//Zero is allow all , not  configured is block all
			context = new FilterControllerImpl();	
			if(makeRandomExclusions){
				context.configureRandomness(enumRandom.RAND_CB, 6);
			}else{
				context.configureRandomness(enumRandom.RAND_CB, 0);			
			}
			context.configureType(type);
			
		}		
		public boolean step2_configRunFilter(WebElement elem){
			BlockingFilter headFilter = new Filter_basicExclusionRules();
			headFilter.setFilterContext(context);
			BlockingFilter filterChain = headFilter
			.chain(new Filter_extractValues_2()					
			.chain(new Filter_typeSpecificRules_2()
			.chain(new Filter_appSpecificRules_script2()
					)));
			return context.runFilterChain(elem, filterChain);
		}
		public String step3_optional_extractReqValues(){

			return context.extractValue("");
		}
	}
	
	public class Workflow_script6{
		FilterContext context = new FilterControllerImpl();	
		public void step1_setContext(boolean makeRandomExclusions, String type){
			//Zero is allow all , not  configured is block all
			context = new FilterControllerImpl();	
			context.configureRandomness(enumRandom.RAND_SUBJECT, 0);		
			if(makeRandomExclusions){
				context.configureRandomness(enumRandom.RAND_CB, 3);
			}else{
				context.configureRandomness(enumRandom.RAND_CB, 0);			
			}
			context.configureType(type);
			
		}
		public boolean step2_configRunFilter(WebElement elem){
			BlockingFilter headFilter = new Filter_basicExclusionRules();
			headFilter.setFilterContext(context);
			BlockingFilter filterChain = headFilter			
			.chain(new Filter_groupExclusionRules_6()			
			.chain(new Filter_typeSpecificRules_6()
			.chain(new Filter_appSpecificRules_script6()
					)));
			return context.runFilterChain(elem, filterChain);
		}
	
	}
	public class Workflow_scriptGeneric{
		FilterContext context = new FilterControllerImpl();	
		public void step1_setContext(boolean makeRandomExclusions, String type){
			//Zero is allow all , not  configured is block all
			context = new FilterControllerImpl();				
			
			context.configureType(type);
			
		}
		public boolean step2_configRunFilter(WebElement elem){
			BlockingFilter headFilter = new Filter_basicExclusionRules();
			headFilter.setFilterContext(context);
			BlockingFilter filterChain = headFilter			
			.chain(new Filter_groupExclusionRules_Generic()			
			);
			return context.runFilterChain(elem, filterChain);
		}
	
	}
	class Workflow_script7{
		FilterContext context = new FilterControllerImpl();	
		public void step1_setContext(boolean makeRandomExclusions, String type){
			//Zero is allow all , not  configured is block all
			context = new FilterControllerImpl();
			context.configureType(type);
			
		}
		public boolean step2_configRunFilter(WebElement elem){
			BlockingFilter headFilter = new Filter_basicExclusionRules();
			headFilter.setFilterContext(context);
			BlockingFilter filterChain = headFilter			
			;
			return context.runFilterChain(elem, filterChain);
		}
	
	}

	//Pure Business Filters	
	class Filter_basicExclusionRules extends BlockingFilterImpl{

		public WebElement doFilter(WebElement elem){			
			if( !elem.isEnabled()){
				isBlocked=true;
			}
			else if( !elem.isDisplayed()){
				isBlocked=true;
			}
//			else if(elem.getAttribute("type")!=null && elem.getAttribute("type").equals("hidden")){
//				isBlocked=true;
//			}
			
			else if(elem.getAttribute("readOnly")!=null){
				isBlocked=true;
			}
			else if(elem.getAttribute("id").equals(StringUtils.EMPTY) ){
				isBlocked=true;
			}
			else if(elem.getAttribute("id").equals("hcoSelect") ){
				isBlocked=true;
			}
			return returnElem(isBlocked, elem);
		}

	}

	class Filter_extractValues_2  extends BlockingFilterImpl{

		public WebElement doFilter (WebElement elem){
			String toRet=StringUtils.EMPTY;
			if( elem.getAttribute("id").equals("")||elem.getAttribute("id").equals("") ){//TODO Discharge date is more logical since OPs have dat only.
				toRet = elem.getAttribute("value");
				context.setExtractedValue("admit_date",toRet);
			}			
			return returnElem(isBlocked, elem);
		}
	}

	class Filter_groupExclusionRules_2 extends BlockingFilterImpl {

		public WebElement doFilter(WebElement elem){
			if(elem.getAttribute("id").startsWith("custom") ){
				if(context.getRandomValueForId(enumRandom.RAND_CUSTOM)!=0){
					isBlocked=true;
				}

			}
			if(elem.getAttribute("id").startsWith("common") ){
				isBlocked=true;
			}
			if(elem.getAttribute("id").startsWith("sub_") || elem.getAttribute("id").startsWith("tob_")){
				if (context.getRandomValueForId(enumRandom.RAND_SUBJECT)!=0){
					isBlocked=true;
				}

			}
			return returnElem(isBlocked, elem);
		}
	}
	
	class Filter_appSpecificRules_script2 extends BlockingFilterImpl{
		public WebElement doFilter (WebElement elem){
			//Features to avoid OP record outsampling 
			if(elem.getAttribute("id").startsWith("op_disch_code") || elem.getAttribute("id").equals("code") ){
				isBlocked=true;
			}	
			if(elem.getAttribute("id").contains("SIP_MULTI_ROUTE") ){//TODO Temp workaround, to fix in test data
				isBlocked=true;
			}
			return returnElem(isBlocked, elem);
		}
	}

	
	

	class Filter_typeSpecificRules_2 extends BlockingFilterImpl{
		public WebElement doFilter (WebElement elem){
			//String type, WebElement elem, int rand_for_cb, int rand_for_cb_utd){
			if(context.getType().equals("select")){		
				Select select = new Select(elem);
				int maxSelectIdx = select.getOptions().size()-1;
				if(maxSelectIdx == 0){
					isBlocked=true;
				}
			}
			if(context.getType().equals("checkbox")){
				//checkbox
				String cbId = elem.getAttribute("id");
				if(elem.getAttribute("type")!=null && !elem.getAttribute("type").equals("checkbox")){
					isBlocked=true;
				}else{
					if(cbId.contains("utd")){
					
					}else{
						if (context.getRandomValueForId(enumRandom.RAND_CB)!=0){
							isBlocked=true;
						}

					}
				}

			}
			if(context.getType().equals("input")){
				if(!elem.getAttribute("type").equals("text") ){
					isBlocked=true;
				}

			}
			return returnElem(isBlocked, elem);
		}
	}

	class Filter_appSpecificRules_script6 extends Filter_appSpecificRules_script2{}
	
	class Filter_groupExclusionRules_6 extends BlockingFilterImpl {

		public WebElement doFilter(WebElement elem){
			if(elem.getAttribute("id").startsWith("xyz") ){
					isBlocked=true;
			}
			if(elem.getAttribute("id").startsWith("abc") ){
				isBlocked=true;
			}
			if(elem.getAttribute("id").startsWith("a_") || elem.getAttribute("id").startsWith("b_")){
				if (context.getRandomValueForId(enumRandom.RAND_SUBJECT)!=0){
					isBlocked=true;
				}

			}
			return returnElem(isBlocked, elem);
		}
	}
	
	class Filter_groupExclusionRules_Generic extends BlockingFilterImpl {

		public WebElement doFilter(WebElement elem){
			if(elem.getAttribute("id").startsWith("custom") ){
					isBlocked=true;
			}	
			else if(elem.getTagName().equals("input") && elem.getAttribute("maxLength")==null){
				isBlocked=true;
			}
			else if(elem.getTagName().equals("input") && elem.getAttribute("maxLength")!=null ){
				if((Integer.valueOf(elem.getAttribute("maxLength"))>500000)){
					isBlocked=true;
				}
				else if((Integer.valueOf(elem.getAttribute("maxLength"))<1)){
					isBlocked=true;
				}
			}
			return returnElem(isBlocked, elem);
		}
	}
	class Filter_typeSpecificRules_6 extends BlockingFilterImpl{
		public WebElement doFilter (WebElement elem){

			if(context.getType().equals("checkbox")){
				//checkbox
				String cbId = elem.getAttribute("id");
				if(elem.getAttribute("type")!=null && !elem.getAttribute("type").equals("checkbox")){
					isBlocked=true;
				}else{
					if(cbId.contains("xyz")){
						isBlocked=true;
					}
				}
			}
			if(context.getType().equals("input")){
				if(!elem.getAttribute("type").equals("text") ){
					isBlocked=true;
				}

			}
			return returnElem(isBlocked, elem);
		}
	}
	
	
	/***DEFINITIONS*** thats 90 lines of boiler plate code to get some clarity into the bug ridden module**/

	//Chainable filters.
	interface BlockingFilter{
		WebElement doFilter(WebElement elem);	
		BlockingFilter chain(BlockingFilter filter);
		void setFilterContext(FilterContext context);
	}

	//Where each filter reads config from and does writebacks.
	interface FilterContext{	
		void blockElement();
		String getType();
		int getRandomValueForId(enumRandom id);
		void configureRandomness(enumRandom id, int value);
		void configureType(String type);
		String extractValue(String reqId);
		void setExtractedValue(String id, String value);
		boolean runFilterChain(WebElement elem, BlockingFilter filter);
	}
	class FilterControllerImpl implements FilterContext{
		EnumMap<enumRandom, Integer> mapRandConfig = new EnumMap<enumRandom, Integer>(enumRandom.class);
		Map<String, String> mapExtractedIdVal = new LinkedHashMap<String, String>();
		String type;
		boolean isBlocked;
		Random rand = new Random();
		public void configureRandomness(enumRandom id, int value){
			mapRandConfig.put(id,  value);
		}
		public void configureType(String type){
			this.type = type;
		}
		public String extractValue(String reqId){
			return mapExtractedIdVal.get(reqId);
		}
		public boolean runFilterChain(WebElement elem, BlockingFilter filterChain){
			filterChain.doFilter(elem);	
			return isBlocked;
		}
		public String getType(){
			return type;
		}
		public int getRandomValueForId(enumRandom id){
			if(!mapRandConfig.containsKey(id)){
				return -1;
			}else if(mapRandConfig.get(id)==0){
				return 0;
			}
			return rand.nextInt(mapRandConfig.get(id));
		}
		public void setExtractedValue(String id, String value){
			mapExtractedIdVal.put(id, value);
		}
		public void blockElement(){
			isBlocked=true;
		}

	}

	//That which has access to context for writebacks and carries the chaining algorithm.
	abstract class BlockingFilterImpl implements BlockingFilter{
		FilterContext context = null;
		boolean isBlocked=false;
		BlockingFilter chainedFilter = null;

		public BlockingFilter chain(BlockingFilter filter){
			chainedFilter=filter;
			return this;

		}
		public void setFilterContext(FilterContext context){
			this.context=context;
		}
		public WebElement returnElem(boolean isBlocked, WebElement elem){		
			log.trace(elem.getAttribute("id")+" :Examined in  "+this.getClass().getSimpleName());
			if(isBlocked){				
				context.blockElement();
				log.trace("BLOCKED");
				return null;
			}else{
				if(chainedFilter!=null){
					chainedFilter.setFilterContext(context);
					return chainedFilter.doFilter(elem);	
				}	
			}
			//log.trace(elem.getAttribute("id")+" :Allowed in "+this.getClass().getSimpleName());
			log.trace("ALLOWED");
			return elem;
		}

	}

}
