package com.kingcoder.pathfinding.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.kingcoder.pathfinding.Main;
import com.kingcoder.pathfinding.util.Scene;

public class Slider {

	private int x;
	private int y;
	private float sliderValue;
	private int maxVal;
	private int minVal;
	private float step;
	
	private boolean dragging = false;
	private boolean updated = false;
	
	private Color lineColor = new Color(200, 200, 200, 255);
	private Color blockColor = new Color(48, 144, 255, 255);
	
	public Slider(int x, int y, int minVal, int maxVal, int startValue){
		this.x = x;
		this.y = y;
		this.minVal = minVal;
		this.maxVal = maxVal;
		sliderValue = startValue;
		
		step = 500.0f / (maxVal - minVal);
	}
	
	public void update(){
		if(Main.input.buttonPressed(MouseEvent.BUTTON1)){
			if (Scene.getMouseX() >= x + sliderValue*step && Scene.getMouseX() <= x + sliderValue*step + 10 && Scene.getMouseY() >= y - 5 && Scene.getMouseY() <= y + 15){
				dragging = true;
			}
		}else{
			if(dragging)
				updated = true;
			
			dragging = false;
		}
		
		if(dragging){
			sliderValue = (Scene.getMouseX() - x - 5)  / step;
			if(sliderValue > maxVal) sliderValue = maxVal;
			else if(sliderValue < minVal) sliderValue = minVal;
		}
	}
	
	public void render(Graphics g){
		// drawing the line
		g.setColor(lineColor);
		g.fillRect(x, y, 510, 10);
		
		// drawing the movable block
		g.setColor(blockColor);
		g.fillRect(x + (int)(sliderValue*step), y - 5, 10, 20);
		
		// drawing the min and max values
		g.setFont(Scene.regularFont2);
		g.drawString(String.valueOf(minVal), x - 30, y + 10);
		g.drawString(String.valueOf(maxVal), x + 510 + 30, y + 10);
		g.drawString(String.valueOf((int)sliderValue), x + 235, y - 20);
	}
	
	public int getSliderValue(){
		return (int)sliderValue;
	}
	
	public boolean isUpdated(){
		return updated;
	}
	
	public void setUpdated(boolean updated){
		this.updated = updated;
	}
	
}
