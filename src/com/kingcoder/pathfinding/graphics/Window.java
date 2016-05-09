package com.kingcoder.pathfinding.graphics;

import java.awt.Component;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame{

	public Window(String title){
		super(title);
		setResizable(false);
	}
	
	public void addComp(Component c){
		add(c);
	}
	
	public void pack(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.pack();
		
		setLocationRelativeTo(null);
	}
	
	public void setVisible(){
		super.setVisible(true);
	}
	
}
