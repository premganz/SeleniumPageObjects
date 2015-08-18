package org.spo.fw.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.spo.fw.config.SessionContext;


/**
 * This class offers RobotFramework the ability to access and interact with web
 * pages through request/response. It emulates a browser. This Library is
 * stateful, meaning the current page and its state is cached upon each keyword
 * that changes the page's state. Use "Create" then "Go To Page" to start
 * navigation.
 */
public class KeyWords_Utils {
	private static Map<Character, Character> replacementMap = null;
	
	public String generatetRandomAsc2String(int size ){
		return RandomStringUtils.randomAscii(size);
	}
	
	public String generateRandomNumber(int bound ){
		Random rand = new Random();
		return String.valueOf(rand.nextInt(bound));
	}
	
	// Some pages use non-ascii chars like \&nbsp; and others. This is a
		// method to get rid of them. Also, python 2.6 breaks when it encounters
		// chars outside of UTF-8
		public static String findAndReplaceNonAsciiChars(char[] text) {
			for (int i = 0; i < text.length; i++) {
				// is this an ascii, printable char?
				if (text[i] < 32 || text[i] >= 128) {

					if(getReplacementMap().containsKey((text[i]))){
						text[i] = getReplacementMap().get(text[i]);
					}else{
						//text[i] = '\0';
					}

				}

			}
			return new String(text);
		}

		private static Map<Character, Character> getReplacementMap() {
			if (replacementMap == null) {
				replacementMap = new HashMap<Character, Character>();
				// anything that could look like a space
				replacementMap.put('\u00A0', ' ');
				// copyright char
				replacementMap.put('\u00A9', 'c');

				// anything that could look like a dash
				replacementMap.put('\u00AD', '-');
				replacementMap.put('\u2010', '-');
				replacementMap.put('\u2500', '-');
				replacementMap.put('\u2212', '-');
				replacementMap.put('\u3161', '-');
				replacementMap.put('\u30FC', '-');
				replacementMap.put('\u4E00', '-');

				// trademark (TM)
				replacementMap.put('\u2122', 'T');

				// registered tradmark symbol (R)
				replacementMap.put('\u00AE', 'R');

				replacementMap.put('\u0093', '"');
				replacementMap.put('\u0094', '"');
				replacementMap.put('\u2019', '\'');
				replacementMap.put('\u201c', '"');
				replacementMap.put('\u201d', '"');

				//replacementMap.put('xa0', '"');//TODO fix

			}
			return replacementMap;
		}
		public static String stripWhitespace(String whiteString) {
			whiteString = whiteString.trim();
			return whiteString;
		}
		
		@Deprecated
		public static String obfuscate(String x){
			if(x.contains("appConfig.basicAuth_userId")){
				if(x.contains("http")){
					x= x.replaceAll(SessionContext.appConfig.basicAuth_userId+":"+SessionContext.appConfig.basicAuth_pwd+"@", StringUtils.EMPTY);
				}
			}
			return x;
		}
		
		public static  class LevenshteinDistance {

		    public static double similarity(String s1, String s2) {
		        if (s1.length() < s2.length()) { // s1 should always be bigger
		            String swap = s1; s1 = s2; s2 = swap;
		        }
		        int bigLen = s1.length();
		        if (bigLen == 0) { return 1.0; /* both strings are zero length */ }
		        return (bigLen - computeEditDistance(s1, s2)) / (double) bigLen;
		    }

		    public static int computeEditDistance(String s1, String s2) {
		        s1 = s1.toLowerCase();
		        s2 = s2.toLowerCase();

		        int[] costs = new int[s2.length() + 1];
		        for (int i = 0; i <= s1.length(); i++) {
		            int lastValue = i;
		            for (int j = 0; j <= s2.length(); j++) {
		                if (i == 0)
		                    costs[j] = j;
		                else {
		                    if (j > 0) {
		                        int newValue = costs[j - 1];
		                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
		                            newValue = Math.min(Math.min(newValue, lastValue),
		                                    costs[j]) + 1;
		                        costs[j - 1] = lastValue;
		                        lastValue = newValue;
		                    }
		                }
		            }
		            if (i > 0)
		                costs[s2.length()] = lastValue;
		        }
		        return costs[s2.length()];
		    }

		    public static void printDistance(String s1, String s2) {
		        System.out.println(s1 + "-->" + s2 + ": " +
		                    computeEditDistance(s1, s2) + " ("+similarity(s1, s2)+")");
		    }

//		    public static void main(String[] args) {
//		        printDistance("", "");
//		        printDistance("1234567890", "1");
//		        printDistance("1234567890", "12");
//		        printDistance("1234567890", "123");
//		        printDistance("1234567890", "1234");
//		        printDistance("1234567890", "12345");
//		        printDistance("1234567890", "123456");
//		        printDistance("1234567890", "1234567");
//		        printDistance("1234567890", "12345678");
//		        printDistance("1234567890", "123456789");
//		        printDistance("1234567890", "1234567890");
//		        printDistance("1234567890", "1234567980");
//
//		        printDistance("47/2010", "472010");
//		        printDistance("47/2010", "472011");
//
//		        printDistance("47/2010", "AB.CDEF");
//		        printDistance("47/2010", "4B.CDEFG");
//		        printDistance("47/2010", "AB.CDEFG");
//
//		        printDistance("The quick fox jumped", "The fox jumped");
//		        printDistance("The quick fox jumped", "The fox");
//		        printDistance("The quick fox jumped",
//		                            "The quick fox jumped off the balcany");
//		        printDistance("kitten", "sitting");
//		        printDistance("rosettacode", "raisethysword");
//		        printDistance(new StringBuilder("rosettacode").reverse().toString(),
//		            new StringBuilder("raisethysword").reverse().toString());
//		        for (int i = 1; i < args.length; i += 2)
//		            printDistance(args[i - 1], args[i]);
//		    }
//		}
		}
		
		
		public static String toDisplayCase(String s) {

		    final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
		                                                 // to be capitalized

		    StringBuilder sb = new StringBuilder();
		    boolean capNext = true;

		    for (char c : s.toCharArray()) {
		        c = (capNext)
		                ? Character.toUpperCase(c)
		                : Character.toLowerCase(c);
		        sb.append(c);
		        capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
		    }
		    return sb.toString();
		}
		
		public static String getHtmlAsText(String html){
			String parsedText = Jsoup.parse(html).text();
			//HtmlSimplificationNodeVisitor visitor = new HtmlSimplificationNodeVisitor();
				//	Jsoup.parse(html).traverse(visitor);//TODO use a visitor to generate formated text
			//		
			String clean = parsedText.replaceAll("\\P{Print}", "");
			//		String clean = visitor.toString();
							//.replaceAll("\\P{Print}", "");
				clean = KeyWords_Utils.findAndReplaceNonAsciiChars(clean.toCharArray());
			//String clean1 = visitor.toString().replaceAll("|", '\n');
			//System.err.println(clean);
			//clean = clean+inputValuesAsList();
			return clean;
		}
}
