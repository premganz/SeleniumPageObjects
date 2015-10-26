package org.spo.test.demo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spo.fw.config.RunStrategy;
import org.spo.fw.itf.SessionBoundDriverExecutor;
import org.spo.fw.log.Logger1;
import org.spo.fw.meta.fixture.runner.SimpleScriptStub;
import org.spo.fw.navigation.itf.NavException;
import org.spo.fw.navigation.itf.PageLayoutValidator;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.utils.pg.itf.WebContentProvider;
import org.spo.fw.web.ServiceHub;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * 
 * @author Prem
 *Diff Page
	Runs Everyday 
		->Loops on the Nav Xml pages.

	for each Page:
		->Store to MongoBd in Table (Collection) pages_cache_today against key = pageId
		
		->Var text_yesterday = Pulls old pageText from yesterday MongoDb table pages_cache_yesterday against key=pageId
		->var dif=Diffs (current Text nodes from Browser, text_yesterday)

		->print diff.difference

	end loop
		->replace pages_cache_yesterday with pages_cache_today




Reconfiguring Services

	PageNavModel-> pull pages list using xpath
	
	Lib_Pages->Plug new ContentProvider_Mongo
	
	PageLoadValidator->Plug new Validator that does text match.
 *
 *
 */
 
public class Demo_Test_Diff{ 
	Logger1 log = new Logger1("Test_Diff") ;

	RunStrategy strategy = new RunStrategy();
	Random rand = new Random();
	MongoClient mongoClient=null;
	DB db;
	DBCollection coll_today;
	DBCollection coll_yesterday;
	List<String> lstLogWarning = new ArrayList<String>();

	

	class MakeTestDiff extends SimpleScriptStub {
		String pageName="";

		@Override
		public void init() {
			super.init();
			try {
				mongoClient = new MongoClient( "localhost" , 27017 );
			} catch (UnknownHostException e) {					
				e.printStackTrace();
			}
			db = mongoClient.getDB( "db1" );
			coll_today = db.getCollection("pages_cache1");
			coll_today.drop();
			coll_today = db.getCollection("pages_cache1");
			coll_yesterday=db.getCollection("pages_cache0");
			kw.setContentProvider(new Lib_Content_Mongo());
			
		}

		public  void start_test() throws Exception {
			kw.getNavContainer().getDefaulModel().getFactory().addValidator("(.*)", new InterceptorMakeCache());
		
			List<String> pages =new ArrayList<String>() ;
				pages = kw.getNavContainer().queryAppDocModel("//page", "name");
			pages.remove("Home");pages.remove("ANY");

			for(String page:pages){
				pageName=page;
				try{
					kw.navigateByName(pageName);					
				}catch (NavException e){
					lstLogWarning.add("NavException for "+pageName);
					continue;
				}
			}

			for(String warn:lstLogWarning){
				log.info("***WARNINGS***");
				log.error(warn);
			}


		}

		class InterceptorMakeCache implements PageLayoutValidator {
			//Lib_ExternalScriptCalls<String> remoteCache = new Lib_ExternalScriptCalls<String>();
			Logger1 log = new Logger1("InterceptorMakeCache");

			public boolean isValid(SessionBoundDriverExecutor executor) {
				return true;
			}


			public boolean validateOnLoad(SessionBoundDriverExecutor executor) {
				ServiceHub kw = (ServiceHub)executor;
				log.trace("entering isVAlid");
				String pageText= kw.doPrintPageAsText().replaceAll("([0-9]{1,2}/[0-9]{1,2}/[0-9]{4})","");				
				BasicDBObject doc = new BasicDBObject("content", pageText).append("_id",pageName);		        
				coll_today.insert(doc);				
				return kw.checkPageLayoutForm(pageName, pageName).isPassed();
				
			}

		}

	}

	class MongoStaticFileProvider implements StaticContentProvider{
		public List<String> getContent(String expression, ServiceHub kw) {
			String page_name=expression;
			BasicDBObject doc = new BasicDBObject("_id", page_name);	
			List<String> toReturn = new ArrayList<String>();
			toReturn.add(coll_yesterday.findOne(doc).get("content").toString().replaceAll("([0-9]{1,2}/[0-9]{1,2}/[0-9]{4})",""));	
			return toReturn;
		}

	}
	
	class WebContentProviderDiff implements WebContentProvider{
		public String getPageContent(String pageName, ServiceHub kw) {
			String pageText= kw.doPrintPageAsText().replaceAll("([0-9]{1,2}/[0-9]{1,2}/[0-9]{4})","");
			return pageText;
		}
		
	}


	class Lib_Content_Mongo extends Lib_PageLayout_Content{

		@Override
		public void init() {			
			super.init();			
			fileContentProvider.setStaticContentProvider(new MongoStaticFileProvider());
			webContentProvider.setWebContentProvider(new WebContentProviderDiff());
		}

	}
}