/**
MIT Licence, For more info raise tickets at https://github.com/premganz/SeleniumPageObjects/issues
**/
package org.spo.fw.selenium.concurrent;

import java.util.ArrayList;
import java.util.List;

import org.python.modules.synchronize;
import org.spo.fw.selenium.SeleniumCommand;


public class InvokerPipeLineScheduler {
	private boolean app_state_available=true;// a binary switch
	private int runningIdxBatchPipe1=0;
	private int runningIdxBatchPipe2=0;

	private List<List<SeleniumCommand>> commandPipeLine1 = new ArrayList<List<SeleniumCommand>>();
	private List<List<SeleniumCommand>> commandPipeLine2 = new ArrayList<List<SeleniumCommand>>();
	


	public synchronized List<SeleniumCommand> getPipeLineByIdx(int i){
		if (i==1){
			return getPipeLine1();
		}else{
			return getPipeLine2();
		}
	}
	
	public synchronized List<SeleniumCommand> getPipeLine1() {

		while (app_state_available == false) {
			try {
				wait();
			}
			catch (InterruptedException e) {
			}
		}
		if(commandPipeLine1.size()<=runningIdxBatchPipe1){
			return null;
		}
		return commandPipeLine1.get(runningIdxBatchPipe1);
	}

	
	public synchronized void yieldToPipeByIdx(int i){
		if(i==1){
			yeildToPipe1();
		}else{
			yeildToPipe2();
		}
	}

	public synchronized void yeildToPipe2(){
		runningIdxBatchPipe2++;
		app_state_available = true;
		notifyAll();
	}
	
	public synchronized void yeildToPipe1(){
		runningIdxBatchPipe1++;
		app_state_available = false;
		notifyAll();
	}
	
	public synchronized List<SeleniumCommand> getPipeLine2() {

		while (app_state_available == true) {
			try {
				wait();
			}
			catch (InterruptedException e) { 
			} 
		}
		if(commandPipeLine2.size()<=runningIdxBatchPipe2){
			return null;
		}
		return commandPipeLine2.get(runningIdxBatchPipe2);

	}


	public void setCommandPipeLine1(List<List<SeleniumCommand>> commandPipeLine1) {
		this.commandPipeLine1 = commandPipeLine1;
	}

	

	public void setCommandPipeLine2(List<List<SeleniumCommand>> commandPipeLine2) {
		this.commandPipeLine2 = commandPipeLine2;
	}
}
