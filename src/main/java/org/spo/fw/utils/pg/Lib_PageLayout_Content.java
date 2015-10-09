package org.spo.fw.utils.pg;

import org.spo.fw.itf.ExtensibleService;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.pg.itf.StaticContentProcessor;
import org.spo.fw.utils.pg.itf.WebContentProcessor;
import org.spo.fw.utils.pg.model.DefaultWebContentProcessor;
import org.spo.fw.utils.pg.model.DefaultWebContentProvider;
import org.spo.fw.utils.pg.model.FileContent;
import org.spo.fw.utils.pg.model.PageContent;
import org.spo.fw.utils.pg.model.SectionWiseContentProcessor;
import org.spo.fw.utils.pg.model.TRSContentProvider;
import org.spo.fw.web.KeyWords;



public class Lib_PageLayout_Content implements ExtensibleService{

	//private PageFactory factory;
	Logger1 log = new Logger1(this.getClass().getSimpleName());

	private StaticContentProcessor fileContentProvider;
	private WebContentProcessor webContentProvider;

	@Override
	public void init() {
		fileContentProvider=new SectionWiseContentProcessor();
		fileContentProvider.setStaticContentProvider(new TRSContentProvider());
		webContentProvider=new DefaultWebContentProcessor();
		webContentProvider.setWebContentProvider(new DefaultWebContentProvider() );
	}

	public Lib_PageLayout_Content() {
		super();		
	}

	//Split by path delimiters
	public FileContent entry_getFileContent( String expression ) {		
		return fileContentProvider.getFileContent(expression);

	}


	public PageContent entry_getPageContent(String pageName, KeyWords kw) {		
		return webContentProvider.getPageContent(pageName, kw);
	}





}
