/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils;

import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

public class LabelNodeVisitor implements NodeVisitor{
	private StringBuffer pageText = new StringBuffer();
	public static final char nextLine = '\n';
	public static final char tab = '\t';

	
	void append(char x){
		pageText.append(x);		
	}

	

   
    /**
     * Format an Element to plain-text
     * @param element the root element to format
     * @return formatted text
     */
 

    // the formatting rules, implemented in a breadth-first DOM traverse

        private static final int maxWidth = 80;
        private int width = 0;
        private StringBuilder accum = new StringBuilder(); // holds the accumulated text

        // hit when the node is first seen
        public void head(Node node, int depth) {
        	if(node.attr("id").endsWith("_req")){
        		TextNode textNode = new TextNode(node.outerHtml(), "");
        		append1(textNode.text());
        		 
        	}

        }

        // hit when all of the node's children (if any) have been visited
        public void tail(Node node, int depth) {
        	
        }
        
        private void append1(String text){
        	accum.append(text);
        	accum.append('\n');
        }

        // appends text to the string builder with a simple word wrap method
      

        public String toString() {
        	String a = accum.toString();
        	a = a.replaceAll("®", "R");
        	a = a.replaceAll("©", "C");
            return a;
        }


	
	
	





}
