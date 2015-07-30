package org.spo.fw.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.spo.fw.session.SessionContainer;


public class LoggingThread extends Thread{
	private Logger1 log ;
	private JFrame frame;
	private JTextArea textArea;
	private Thread reader;
	private Thread reader2;
	private boolean quit;
	private boolean scrollLock;
	public LoggingThread(){
		frame=new JFrame("Java Console");
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize=new Dimension((int)(screenSize.width/2),(int)(screenSize.height/2));
		int x=(int)(frameSize.width/2);
		int y=(int)(frameSize.height/2);
		frame.setBounds(x,y,frameSize.width,frameSize.height);

		textArea=new JTextArea();
		textArea.setEditable(true);
		JButton button=new JButton("scroll lock/unlock");

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);
		frame.getContentPane().add(button,BorderLayout.SOUTH);
		frame.setVisible(true);
		button.addActionListener(new ActionListener() {
			  
	          public void actionPerformed(ActionEvent e)
	          {
	             scrollLock=!scrollLock;
	          }
	      }); 
		
	}
	   
	public Logger1 getLog() {
		return log;
	}
	public void setLog(Logger1 log) {
		this.log = log;
	}
	@Override
	public void run() {
		while(true){
			try {
				String logVal =SessionContainer.logQueue.poll();
				if(logVal!=null && !logVal.isEmpty()){
					textArea.append(logVal+'\n');
					if(!scrollLock)
					textArea.setCaretPosition(textArea.getDocument().getLength());
					
				}
				sleep(100);
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}

	}
}
