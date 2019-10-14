/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.utils;

import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
/**
 * 
 * @author premganesh
 * To be used with JSoup for customized text representation of a html page.
 *
 */
public class CustomNodeVisitor implements NodeVisitor{
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
        	String name = node.nodeName();
        	if (name.equals("li"))
        		append('\n');

        	else if (node.toString().startsWith("<select")){
        		append1("[SELECT]");append(tab);}
        	else if(node.outerHtml().startsWith("<option") ){
        		//append1(node.attr("value")+":");append1(" ");
        		TextNodeVisitor textVisitor = new TextNodeVisitor();
        		node.traverse(textVisitor);
        		append1("{"+textVisitor.toString()+"}");

        	}


        	else if (node.outerHtml().startsWith("<input")){
        		if(node.attr("type").equals("input"))
        			append1("[INPUT]" + node.attr("maxLength"));
        	}
        	else if (node.outerHtml().startsWith("<span")){
        		TextNodeVisitor textVisitor = new TextNodeVisitor();
        		node.traverse(textVisitor);
        		append1(":"+textVisitor.toString()+" ");
        	}




        }

        // hit when all of the node's children (if any) have been visited
        public void tail(Node node, int depth) {
        	
        }
        
        private void append1(String text){
        	accum.append(text);
        }

        // appends text to the string builder with a simple word wrap method
        private void append2(String text) {
            //if (text.contains('\n'))
                width = 0; // reset counter if starts with a newline. only from formats above, not in natural text
            if (text.equals(" ") &&
                    (accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ")))
                return; // don't accumulate long runs of empty spaces

            if (text.length() + width > maxWidth) { // won't fit, needs to wrap
                String words[] = text.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    boolean last = i == words.length - 1;
                    if (!last) // insert a space if not the last word
                        word = word + " ";
                    if (word.length() + width > maxWidth) { // wrap and reset counter
                        accum.append('\n').append(word);
                        width = word.length();
                    } else {
                        accum.append(word);
                        width += word.length();
                    }
                }
            } else { // fits as is, without need to wrap text
                accum.append(text);
                width += text.length();
            }
        }

        public String toString() {
        	String a = accum.toString();
        	a = a.replaceAll("®", "R");
        	a = a.replaceAll("©", "C");
            return a;
        }


	
	
	
	
	
	
//	public void head(Node node, int depth) {
//
//		if(node.outerHtml().contains("<span")||
//				node.outerHtml().contains("<tr")||
//				node.outerHtml().contains("<td")||
//				node.outerHtml().contains("<select")||
//				node.outerHtml().contains("<input")||
//				node.outerHtml().contains("<option")||
//				node.outerHtml().contains("<div")){
//			
//			if(node.outerHtml().contains("tr")){
//				append("--------------------------------------------------------------------");
//				append(nextLine);
//			} else if (node.outerHtml().contains("td")){
//				append("|");
//			}else if (node.outerHtml().contains("select")){
//				append("Select Box");append(tab);
//			}else if (node.outerHtml().contains("div") || node.outerHtml().contains("span")) {
//				append(node.toString());
//			}else if(node.outerHtml().contains("option") ){
//				append(node.attr("value")+":"+node.toString());append(tab);
//			}else if (node.outerHtml().contains("input")){
//				//append("Input "+elem.getAttribute("type"));
//			}
//
//
//		}
//	}





}
