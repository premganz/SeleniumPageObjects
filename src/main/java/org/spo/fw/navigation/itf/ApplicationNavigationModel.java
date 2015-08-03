package org.spo.fw.navigation.itf;

import java.util.List;

import org.w3c.dom.Document;

public interface ApplicationNavigationModel{
	void init() throws Exception;
	String getHomePageUrl();
	List<NavigationTask> getDefaultPath(String fromUrl, String toUrl);
	List<NavigationTask> getDefaultPath_name(String baseName);
	MultiPage enrichMultiPage(Page targetPage);
	Document getAppHeirarchyDoc();
	void setFactory(PageFactory factory);
	}