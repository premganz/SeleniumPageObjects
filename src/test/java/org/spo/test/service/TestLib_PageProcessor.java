package org.spo.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.log.Logger1;
import org.spo.fw.meta.fixture.runner.SimpleScriptStub;
import org.spo.fw.meta.fixture.runner.SimpleScriptStubStatic;
import org.spo.fw.meta.fixture.runner.TestRunnerTemplate;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.utils.pg.itf.WebContentProvider;
import org.spo.fw.web.ServiceHub;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class TestLib_PageProcessor { 
	Logger1 log = new Logger1("Test_Diff") ;

	RunStrategy strategy = new RunStrategy();
	Random rand = new Random();
	MongoClient mongoClient=null;
	DB db;
	DBCollection coll_today;
	DBCollection coll_yesterday;
	List<String> lstLogWarning = new ArrayList<String>();
	String var_pageText="";
	List<String> var_lst=new ArrayList<String>();

	@Test
	public void testPageContent() throws Exception{
		var_pageText="quick brown fox";
		var_lst.add("quick brown1");
		TestRunnerTemplate.runTest(new MakeTestDiffNegative());	}


	@Test
	public void testPageContent1() throws Exception{
		var_pageText="quick brown fox";
		var_lst.add("quick brown");
		var_lst.add("***break***");
		var_lst.add("fox");

		TestRunnerTemplate.runTest(new MakeTestDiff());	}

	@Test
	public void testPageContent2() throws Exception{
		var_pageText="This zoo has a lot of specimens - see the quick brown fox,  a hungry lion, some striped zebras, a noisy kakapo, a wild buffaloe are some of the attractions";
		var_lst.add("quick brown");
		var_lst.add("***section:1***");
		var_lst.add("fox");
		var_lst.add("***end***");
		var_lst.add("***section:2***");
		var_lst.add("some striped zebras,");
		var_lst.add("a noisy kakapo");
		var_lst.add("***end***");

		TestRunnerTemplate.runTest(new MakeTestDiff());	}

	@Test
	public void testPageContent3() throws Exception{
		var_pageText="This zoo has a lot of specimens - see the quick brown fox,  "
				+ "a hungry lion, some striped zebras, a noisy kakapo, a wild buffaloe are some of the attractions";
		var_lst.add("quick brown");
		var_lst.add("***section:1***");
		var_lst.add("fox");
		var_lst.add("***end***");
		var_lst.add("***section:regex***");
		var_lst.add("quick(.*)fox[,].*?[zebras]");
		var_lst.add(",a noisy kakapo");
		var_lst.add("***end***");

		TestRunnerTemplate.runTest(new MakeTestDiff());	}
	@Test
	public void testPageContent3_lite() throws Exception{
		var_pageText="This zoo has a lot of specimens - see the quick brown vixen,  "
				+ "a hungry lion, some striped zebras, a noisy kakapo, a wild buffaloe are some of the attractions";
		var_lst.add("quick brown");
		var_lst.add("***section:1_lite***");
		var_lst.add("fox");
		var_lst.add("***end***");
		var_lst.add("***section:regex***");
		var_lst.add("quick(.*)lion[,].*?[zebras]***expr***");
		var_lst.add(",a noisy kakapo");
		var_lst.add("***end***");
}
	@Test
	public void testPageContent3_lite_1() throws Exception{

		var_pageText="This zoo has a lot of specimens - see the quick brown vixen,  "
				+ "a hungry lion, some striped zebras, a noisy kakapo, a wild buffaloe are some of the attractions";
		var_lst.add("quick brown");
		var_lst.add("***section:1_lite***");
		var_lst.add("fox");
		var_lst.add("***end***");
		var_lst.add("***section:regex***");
		var_lst.add("quick(.*)lion[,].*?[zebras]***expr***");
		var_lst.add(",a noisy kakapo");
		var_lst.add("***end***");
	
		TestRunnerTemplate.runTest(new MakeTestDiffLite());	}

	@Test
	public void testPageContent4() throws Exception{
		var_pageText="This zoo has a lot of specimens - see the quick brown fox,  "
				+ "a hungry lion, some striped zebras, a noisy kakapo, a wild buffaloe are some of the attractions";
		var_lst.add("quick brown");
		var_lst.add("***section:1***");
		var_lst.add("fox");
		var_lst.add("***end***");
		var_lst.add("***section:regex***");
		var_lst.add("quick(.*)fox[,]");
		var_lst.add("***expr***");
		var_lst.add( ".*?[zebras]");
		var_lst.add(",a noisy kakapo");
		var_lst.add("***end***");

		TestRunnerTemplate.runTest(new MakeTestDiff());	}


	class MakeTestDiff extends SimpleScriptStub {
		@Override
		public void init() {
			super.init();
			kw.setContentProvider(new Lib_Content_Test());
		}

		@Override
		public void execute() throws Exception {
			failed=kw.checkPageLayoutForm("", "dummy").isFailed();
		}
	}
	
	class MakeTestDiffLite extends MakeTestDiff {
		@Override
		public void init() {
			super.init();
			SessionContext.appConfig.liteMode=true;
		}

	}

	class MakeTestDiffNegative extends MakeTestDiff {
		@Override
		public void execute() throws Exception {
			failed=!kw.checkPageLayoutForm("", "dummy").isFailed();
		}
	}
	class TestStaticFileProvider implements StaticContentProvider{
		public List<String> getContent(String expression, ServiceHub kw) {
			List<String> toReturn = var_lst;
			return toReturn;
		}
	}

	class WebContentProviderDiff implements WebContentProvider{
		public String getPageContent(String pageName, ServiceHub kw) {
			String pageText= var_pageText;
			return pageText;
		}

		@Override
		public String getPageContentFormatted(String pageName, ServiceHub kw) {
			// TODO Auto-generated method stub
			return "";
		}
	}

	class Lib_Content_Test extends Lib_PageLayout_Content{
		@Override
		public void init() {			
			super.init();			
			fileContentProvider.setStaticContentProvider(new TestStaticFileProvider());
			webContentProvider.setWebContentProvider(new WebContentProviderDiff());
		}

	}
}


