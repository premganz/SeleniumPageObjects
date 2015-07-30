package org.spo.fw.meta.fixture;

import java.lang.reflect.Constructor;

import org.spo.fw.exception.SPOException;
import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.specific.scripts.navigation.itf.Page;
import org.spo.fw.specific.scripts.navigation.itf.PageFactory;
import org.spo.fw.specific.scripts.navigation.model.DefaultPage;
import org.spo.fw.specific.scripts.navigation.model.MultiPageImpl;


public class StubPageFactoryImpl implements PageFactory, ExtensibleService{
protected String packageName="";
	@Override
		public void init() {
		
			
		}
	public  Page getPage(String name) throws SPOException{
		init();
		Page page = null;
			
			try {
				@SuppressWarnings("rawtypes")
				Constructor constructor = Class.forName(packageName+name+"Page").getConstructor(null);
				 page = (Page)constructor.newInstance(null);
				 page.init();
			} catch (ClassNotFoundException e) {
				return new StubDefaultPage();
				//e.printStackTrace();
			}catch (Exception e) {
				throw new SPOException("An Exception was thrown trying to getPage object for  "+name+" : "+e.getClass().getName());
				//e.printStackTrace();
			}
			return page;
		
	}
	public  Page getDefaultPage(){
			return new StubDefaultPage();
	}
	public  Page getMultiPage(){
		return new MultiPageImpl();
}
}
