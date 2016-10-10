package main;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {
	
	public static void main(String[] args) throws IOException{
		JFrame frame = new JFrame();
		JFrame loading = new JFrame("LOADING...");
		loading.setResizable(false);
		loading.setVisible(true);
		loading.setSize(250,50);
		loading.setLocationRelativeTo(null);
		loading.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		loading.setLayout(new BorderLayout());
		loading.add(new JLabel("LOADING..."), BorderLayout.CENTER);
		Panel panel = new Panel();
		Time time = new Time(panel);
		frame.setTitle("Subway Map");
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setSize(800,600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.add(time, BorderLayout.SOUTH);
		panel.requestFocusInWindow();
		frame.pack();
		loading.setVisible(false);

	}

}
