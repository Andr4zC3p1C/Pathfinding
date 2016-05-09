package com.kingcoder.pathfinding.graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.kingcoder.pathfinding.Main;
import com.kingcoder.pathfinding.util.Scene;

public class Checkbox {

	private String title;
	private int x, y;
	private boolean checked = false;
	private boolean updated = false;
	
	public Checkbox(String title, int x, int y){
		this.title = title;
		this.x = x;
		this.y = y;
	}
	
	public void update(){
		if(Scene.getMouseX() >= x && Scene.getMouseX() < x+20){
			if(Scene.getMouseY() >= y && Scene.getMouseY() <= y+20){
				// is hovering the box
				if(Main.input.buttonClicked(MouseEvent.BUTTON1)){
					updated = true;
					checked = !checked;
				}else{
					updated = false;
				}
			}else{
				updated = false;
			}
		}else {
			updated = false;
		}
	}

	public void render(Graphics g){
		// the background
		g.setColor(Scene.checkboxB);
		g.fillRect(x, y, 20, 20);
		
		if(checked){
			g.setColor(Scene.checkbox_checked);
			g.fillRect(x, y, 20, 20);
		}
		
		if(Scene.getMouseX() >= x && Scene.getMouseX() <= x+20){
			if(Scene.getMouseY() >= y && Scene.getMouseY() <= y+20){
				g.setColor(Scene.checkbox_hover);
				g.fillRect(x+5, y+5, 10, 10);
			}
		}
		
		g.setFont(Scene.regularFont);
		g.setColor(Scene.regularFontColor);
		g.drawString(title, x + 30, y+13);
	}
	
	public boolean getChecked(){
		return checked;
	}
	
	public boolean updated(){
		return updated;
	}
	
}
