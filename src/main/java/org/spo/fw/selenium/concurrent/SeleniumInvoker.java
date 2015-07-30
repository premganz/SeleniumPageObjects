package org.spo.fw.selenium.concurrent;

import java.util.List;
import java.util.concurrent.Callable;

import org.openqa.selenium.WebDriver;
import org.spo.fw.exception.SPOException;
import org.spo.fw.selenium.SeleniumCommand;
import org.spo.fw.web.KeyWords;




public abstract class SeleniumInvoker extends Thread implements Callable<Integer>{
	protected InvokerPipeLineScheduler pipeLineController;
	protected WebDriver driver;
	protected int pipeIdx;

	public SeleniumInvoker(InvokerPipeLineScheduler pipeLine,  int pipeIdx){
		this.pipeLineController= pipeLine;		
		this.pipeIdx=pipeIdx;
	}

	public void setUpState(){
		KeyWords KeyWords1 = new KeyWords();
		try {
			KeyWords1.create("","");
		} catch (SPOException e) {
			e.printStackTrace();
		}
		try {
			KeyWords1.goToPage("http://en.wikipedia.org");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		driver=KeyWords1.getDriver();
	}
	
	@Override
	public void run() {
		
		setUpState();
		
		for(int i = 0; i<20; i++){
			List<SeleniumCommand> commandBatch = pipeLineController.getPipeLineByIdx(pipeIdx);
			System.err.println("processing pipeLineData::"+pipeIdx);
			for(SeleniumCommand command : commandBatch){
				command.execute(driver);
				
			}
			pipeLineController.yieldToPipeByIdx(pipeIdx);
			try {
				sleep(100);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}

	}

	@Override
	public Integer call() throws Exception {		
		return 0;
	}

}
