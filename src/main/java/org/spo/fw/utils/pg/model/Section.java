/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.model;
public class Section{
	public String sectionTitle="";
	public String type;
	public String content="";
	public boolean flag=false;

	public String toString() {	
		return "Title:"+sectionTitle+"/////"+"content:"+content;
	}
}