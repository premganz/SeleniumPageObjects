/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.meta.fixture;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class StubWebElementRepository {

	public WebElement webElement;
	
	
	public static class Builder {
		private String tagName;
		private String id;
		private String type;
		
		public Builder tagName(String tagName){
			this.tagName=tagName;
			return this;
		}
		public Builder id(String id){
			this.id=id;
			return this;
		}
		public Builder type(String type){
			this.type=type;
			return this;
		}
		public  WebElement build(){
			WebElementStub elem = new WebElementStubExt();
			elem.attributes.put("type", type);
			elem.attributes.put("id", id);
			elem.attributes.put("tagName", tagName);
			return elem;
			
		}
		
		
	}
	
	public static class WebElementStub implements WebElement{
		Map<String, String > attributes = new LinkedHashMap<String, String>();
		
		public void click() {

		}

		
		public void submit() {

		}

		
		public void sendKeys(CharSequence... keysToSend) {

		}

		
		public void clear() {

		}

		
		public String getTagName() {
			return null;
		}

		
		public String getAttribute(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public boolean isSelected() {
			// TODO Auto-generated method stub
			return false;
		}

		
		public boolean isEnabled() {
			// Changed
			return true;
		}

		
		public String getText() {

			return null;
		}

		
		public List<WebElement> findElements(By by) {
			return null;
		}

		
		public WebElement findElement(By by) {
			return null;
		}

		
		public boolean isDisplayed() {
			return true;
		}

		
		public Point getLocation() {
			return null;
		}

		
		public Dimension getSize() {
			return null;
		}

		
		public String getCssValue(String propertyName) {
			return null;
		}

	}

	
	public static class WebElementStubExt extends WebElementStub{
		
		
		public String getTagName() {
		return attributes.get("tagName");
		}
		
		
		public String getAttribute(String name) {
//			switch (name){
//				case "readOnly":
//					return null;
//				
//				default:
//					return attributes.get(name);
//				
//			}	
	
			if(name.equals("readOnly")){
				return null;
			}else{
				return attributes.get(name);
			}
			
		}
		
	}
	
	
	
}
