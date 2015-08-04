package org.spo.fw.recorder;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.spo.fw.exception.SPOException;
import org.spo.fw.launch.SeleniumScriptLauncher;
import org.spo.fw.runners.Node3_LaunchSeleniumScript;


public class RobotRecorderWindow {
	   private JFrame mainFrame;
	   private JLabel headerLabel;
	   private JLabel statusLabel;
	   private JPanel controlPanel;
	   private JLabel msglabel;
	   private JButton btnStart = new JButton("                Start                 ");
	   private JButton btnStop = new JButton("                Stop                  ");
	   private Script_Robot_Generator gen = new Script_Robot_Generator();
	   private static String robotScript = StringUtils.EMPTY; 
	   JTextArea areaResult = new JTextArea(robotScript);
	   JPanel panel = new JPanel();
	   
	   public RobotRecorderWindow(){
	      prepareGUI();
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	   }

	   public static void main(String[] args){
		   RobotRecorderWindow swingLayoutDemo = new RobotRecorderWindow();  
	      swingLayoutDemo.showGridBagLayoutDemo();       
	   }
	      
	   private void prepareGUI(){
	      mainFrame = new JFrame("Robot Recorder");
	      mainFrame.setSize(600,600);
	      mainFrame.setLayout(new GridLayout(3, 1));

	      headerLabel = new JLabel("",JLabel.CENTER );
	      statusLabel = new JLabel("",JLabel.CENTER);        

	      statusLabel.setSize(350,100);
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	      });    
	      controlPanel = new JPanel();
	      controlPanel.setLayout(new FlowLayout());

	      mainFrame.add(headerLabel);
	      mainFrame.add(controlPanel);
	      mainFrame.add(statusLabel);
	      mainFrame.setVisible(true);  
	   }

	   private void showGridBagLayoutDemo(){
	      headerLabel.setText("Record user actions in  as robot script");
	      		     

	     
	      panel.setBackground(Color.darkGray);
	      panel.setSize(400,400);
	      GridBagLayout layout = new GridBagLayout();

	      panel.setLayout(layout);        
	      GridBagConstraints gbc = new GridBagConstraints();

	      
	      gbc.gridx = 0;
	      gbc.gridy = 0;      
	      gbc.fill = GridBagConstraints.HORIZONTAL;
	      gbc.gridwidth = 2;
	      JTextArea instructions = new JTextArea("1. Uses Chrome (developer mode off)"+'\n'+"2. Copy result after clicking stop ");
	      instructions.setFocusable(false);
	      instructions.setBackground(Color.gray);
	      panel.add(instructions,gbc);  
	      
	      
	      gbc.gridx = 0;
	      gbc.gridy = 1;
	      gbc.fill = GridBagConstraints.HORIZONTAL;
	      gbc.gridwidth = 1;
	      panel.add(btnStart,gbc);
	    
	      gbc.gridx = 1;
	      gbc.gridy = 1;
	      gbc.fill = GridBagConstraints.HORIZONTAL;
	      gbc.gridwidth = 1;
	      panel.add(btnStop,gbc); 

	     
	      gbc.gridx = 0;
	      gbc.gridy = 2;      
	      gbc.fill = GridBagConstraints.HORIZONTAL;
	      gbc.gridwidth = 2;
	     
	      areaResult.setColumns(40);
	      areaResult.setAutoscrolls(true);
	     // areaResult.setRows(400);
	      JScrollPane sp = new JScrollPane(areaResult);
	      sp.setBounds(10,60,780,1500);
	      sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	      panel.add(sp,gbc);  

	      controlPanel.add(panel);
	      btnStart.addActionListener(new ActionListener() {
	    	  
	            public void actionPerformed(ActionEvent e)
	            {
	              try {
	            	  

	            	  Thread t = new Thread(new Runnable() {
	            	     public void run() {
	            	    	 //FiXME:Node is not meant to be called programmatically
	            	    	 try {
								gen =(Script_Robot_Generator)new Node3_LaunchSeleniumScript().launchScript(gen);
							} catch (SPOException e) {
								e.printStackTrace();
							}

	            	        SwingUtilities.invokeLater(new Runnable() {
	            	            public void run() {


	            	            }
	            	        });
	            	     }
	            	  });
	            	  t.start();
	            	  
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            }
	        });    
	      btnStop.addActionListener(new ActionListener() {
	    	  
	            public void actionPerformed(ActionEvent e)
	            {
	                //Execute when button is pressed
	              robotScript = gen.stop();
	             // robotScript ="hihihih";
	              areaResult.append(robotScript);
	              StringSelection selection = new StringSelection(robotScript);
	              Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	              clipboard.setContents(selection, selection);
	              //panel.add(new JTextArea(robotScript));
	              mainFrame.setVisible(false);  
	              mainFrame.setVisible(true);  
	              
	            }
	        });   

	      mainFrame.setVisible(true);  
	   }

}
