/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.service.future;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class AlertClearingThread extends Thread {
	WebDriver driver = null;
	
	public AlertClearingThread(WebDriver driver){
		this.driver = driver;
	}
	
	
	@Override
	public void run() {
		while(true){
			try {
				
				System.out.println("Alert Thread going to cancel alert!!");
				synchronized (driver){
					sleep(10000);
					System.out.println("AlertThread got lock");
					try{
						System.out.println("AlertThread calling alert accept");
						driver.switchTo().alert().accept();	
						
					}catch(NoAlertPresentException e){
					}
					
					
				}
				//driver.notifyAll();	
			} catch (InterruptedException e) {
				System.out.println("Interrupted!!");
				//e.printStackTrace();
			}

			super.run();
		}
	}
	public void interrupt(){
		this.interrupt();
	}
	
	
}
