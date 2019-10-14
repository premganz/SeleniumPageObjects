/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.xls;

import java.util.ArrayList;
import java.util.List;

import org.robotframework.javalib.library.AnnotationLibrary;

public class XLS_Reader_Library extends AnnotationLibrary {
@SuppressWarnings("serial")
private static final List<String> keywordPatterns = new ArrayList<String>() {	
{
add("com/XLS_Library/*.class");

}

};



public XLS_Reader_Library() {
	
	super(keywordPatterns); // keywords are looked in all the places
// speficied by the different patterns
System.out.println("THis is the expected list:***"+keywordPatterns);
}
    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__"))
            return "This is the general library documentation.";
        return super.getKeywordDocumentation(keywordName);
    }	
    


    
}
