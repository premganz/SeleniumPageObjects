/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileContent{
	public String pageTextDebug;
	public List<Section> sections=new ArrayList<Section>();
	public Map<String,Integer> debugMapInfo = new LinkedHashMap<String, Integer>();
	public List<String> debugListSectionTitles = new ArrayList<String>();		
	public String toString() {	
		StringBuffer buf =new StringBuffer();
		for(Section section:sections){
			buf.append(section.toString()+'\n');
		}
		return buf.toString();
	}
	
	
}
