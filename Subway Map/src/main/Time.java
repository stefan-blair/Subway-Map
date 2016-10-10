package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Time extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 100;
	//Variables
	JRadioButton daySwitch;
	JSlider changeTime;
	JLabel displayCurrentTime;
	JTextField displayChangeTime;
	JButton submit;
	JButton setCurrentTime;
	String currentTime;
	Panel panel;
	//Variables
	
	public Time(Panel panel){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 35, 10, 35);
		//Variables
		this.panel = panel;
		
		currentTime = panel.getTime();
		
		daySwitch = new JRadioButton();
		changeTime = new JSlider(JSlider.HORIZONTAL, 0, 24*60-1, 12*60);
		displayCurrentTime = new JLabel("Current Time = "+currentTime);
		displayChangeTime = new JTextField(String.valueOf(Integer.valueOf(changeTime.getValue()/60))+":"+String.valueOf(changeTime.getValue()%60)+"      ");
		submit = new JButton("Submit");
		setCurrentTime = new JButton("Set to current time");
		
		//Variables
		//Styling
		changeTime.setMajorTickSpacing(1);
		changeTime.setFont(new Font("Arial", Font.BOLD, 15));
		displayCurrentTime.setFont(new Font("Arial", Font.BOLD, 15));
		displayChangeTime.setFont(new Font("Arial", Font.BOLD, 15));
		submit.setFont(new Font("Arial", Font.BOLD, 15));
		setCurrentTime.setFont(new Font("Arial", Font.BOLD, 15));
		//Styling
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(changeTime, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(displayChangeTime, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(submit, gbc);
		gbc.gridy = 0;
		gbc.gridx = 3;
		add(displayCurrentTime, gbc);
		gbc.gridx = 3;
		gbc.gridy = 2;
		add(setCurrentTime, gbc);
		buttons();
	}
	
	public void buttons(){
		submit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				panel.setTime(Integer.valueOf(changeTime.getValue()/60), Integer.valueOf(changeTime.getValue()%60));
			}
		});
		
		changeTime.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent arg0) {
				displayChangeTime.setText(String.valueOf(Integer.valueOf(changeTime.getValue()/60))+":"+String.valueOf(changeTime.getValue()%60));
			}
		});

		setCurrentTime.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				panel.setCurrentTime();
			}
			
		});
	}
		
}
