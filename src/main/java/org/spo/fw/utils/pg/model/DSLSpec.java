/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils.pg.model;

public class DSLSpec {

	public static final String COMMENT_LINE="###";
	public static final String SPL_LINE_START="***";
	public static final String CONTENT_BREAK="^(([\\\\*]{3})([Bb]reak)([\\\\*]{3}))$";
	public static final String START_SECTION="^(([\\\\*]{3})([Ss]ection\\\\:[\\\\w\\\\W0-9]{0,100})([\\\\*]{3}))$";
	public static final String END_SECTION="^*\\*\\*\\*[Ee][Nn][Dd]*\\*\\*\\**$";
	public static final String SECTION_KEYWORD_REGEX="regex"; //starts with regex
	public static final String SECTION_KEYWORD_LITE="lite";
	
	
	
	public static final String REGEX_FLAG_LINE ="regexFlag:";
}
