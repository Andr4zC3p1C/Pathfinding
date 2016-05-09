package com.kingcoder.pathfinding.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

public class Input implements MouseInputListener, MouseMotionListener{

	private boolean[] buttons = new boolean[256]; 
	private boolean[] c_buttons = new boolean[256]; 
	private int mouseX, mouseY;
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
		c_buttons[e.getButton()] = true;
	}

	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}

	public void mouseDragged(MouseEvent e) {
		mouseX = (int)e.getX();
		mouseY = (int)e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = (int)e.getX();
		mouseY = (int)e.getY();
	}
	
	public boolean buttonPressed(int button){
		return buttons[button];
	}
	
	public boolean buttonClicked(int button){
		return c_buttons[button];
	}
	
	public int getMouseX(){
		return mouseX;
	}
	
	public int getMouseY(){
		return mouseY;
	}
	
	public void update(){
		for(int i=0; i < buttons.length; i++){
			c_buttons[i] = false;
		}
	}
}
