/**
 * 
 */
package org.spo.fw.web;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.logging.Logs;

/**
 * @author c400133
 *
 */
public class WebDriverDummy implements WebDriver {

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#get(java.lang.String)
	 */
	@Override
	public void get(String url) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#getCurrentUrl()
	 */
	@Override
	public String getCurrentUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#findElements(org.openqa.selenium.By)
	 */
	@Override
	public List<WebElement> findElements(By by) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#findElement(org.openqa.selenium.By)
	 */
	@Override
	public WebElement findElement(By by) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#getPageSource()
	 */
	@Override
	public String getPageSource() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#quit()
	 */
	@Override
	public void quit() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#getWindowHandles()
	 */
	@Override
	public Set<String> getWindowHandles() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#getWindowHandle()
	 */
	@Override
	public String getWindowHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#switchTo()
	 */
	@Override
	public TargetLocator switchTo() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#navigate()
	 */
	@Override
	public Navigation navigate() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebDriver#manage()
	 */
	@Override
	public Options manage() {
		// TODO Auto-generated method stub
		return new WebDriverOptionsDummy();
	}

}

class WebDriverOptionsDummy implements Options {

	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCookieNamed(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCookie(Cookie cookie) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllCookies() {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Cookie> getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie getCookieNamed(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timeouts timeouts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImeHandler ime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Window window() {
		// TODO Auto-generated method stub
		return new WindowDummy();
	}

	@Override
	public Logs logs() {
		// TODO Auto-generated method stub
		return null;
	}

}
class WindowDummy implements Window {

	@Override
	public void setSize(Dimension targetSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPosition(Point targetPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void maximize() {
		// TODO Auto-generated method stub

	}

}
