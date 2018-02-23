package org.spo.test.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.spo.fw.config.RunStrategy;
import org.spo.fw.config.SessionContext;
import org.spo.fw.exception.SPOException;
import org.spo.fw.log.Logger1;
import org.spo.fw.navigation.itf.Page;
import org.spo.fw.utils.Utils_PageDiff;
import org.spo.fw.utils.pg.Lib_PageLayout_Content;
import org.spo.fw.utils.pg.itf.StaticContentProvider;
import org.spo.fw.utils.pg.itf.WebContentProvider;
import org.spo.fw.utils.pg.model.DefaultWebContentProvider;
import org.spo.fw.web.ServiceHub;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * 
 * @author Prem
 * 
 *
 */




public class Lib_Content_Diff3 extends Lib_PageLayout_Content{
	@Override
	public void init() {	
		String existingUrl = SessionContext.appConfig.TEST_SERVER_BASE_URL;
		SessionContext.appConfig.TEST_SERVER_BASE_URL = existingUrl.replaceAll("bx", "cx").concat("");
		super.init();
	}


}





