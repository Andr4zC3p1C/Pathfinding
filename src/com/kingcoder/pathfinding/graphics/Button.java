package com.kingcoder.pathfinding.graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.kingcoder.pathfinding.Main;
import com.kingcoder.pathfinding.util.Scene;

public class Button{

	private int x, y, width, height;
	private String text;
	private ButtonCInter bci;

	public Button(String text, int x, int y, int width, int height, ButtonCInter bci){
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bci = bci;
	}
	
	public void update(){
		if(isClicked()){
			bci.onClick();
		}
	}
	
	public void render(Graphics g){
		// rendering the square 
		if(isHovering()){
			g.setColor(Scene.buttonHovColor);
			
			if(Main.input.buttonPressed(MouseEvent.BUTTON1)){
				g.setColor(Scene.buttonColor);
			}
		}else{
			g.setColor(Scene.buttonColor);
		}
		
		g.fillRect(x, y, width, height);
		
		// rendering the string
		g.setColor(Scene.titleFontColor);
		g.setFont(Scene.regularFont);
		g.drawString(text, x + 5, y + height / 2 + 3);
	}
	
	
	// the events
	public boolean isHovering(){
		return (Scene.getMouseX() >= x && Scene.getMouseX() < x+width) && (Scene.getMouseY() >= y && Scene.getMouseY() < y+height);
	}
	
	public boolean isClicked(){
		boolean a = isHovering() && Main.input.buttonClicked(MouseEvent.BUTTON1);
		return a;
	}
	
}
