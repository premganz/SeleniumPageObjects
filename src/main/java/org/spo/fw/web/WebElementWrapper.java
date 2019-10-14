/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.web;

import org.openqa.selenium.WebElement;

public class WebElementWrapper {

	private WebElement element;
	private QueryHandler suitableQueryHandler;
	public WebElement getElement() {
		return element;
	}
	public void setElement(WebElement element) {
		this.element = element;
	}
	public QueryHandler getSuitableQueryHandler() {
		return suitableQueryHandler;
	}
	public void setSuitableQueryHandler(QueryHandler suitableQueryHandler) {
		this.suitableQueryHandler = suitableQueryHandler;
	}
	
	
	
}
