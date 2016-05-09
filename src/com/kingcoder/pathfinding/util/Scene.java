package com.kingcoder.pathfinding.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.kingcoder.pathfinding.Main;
import com.kingcoder.pathfinding.algorithms.AStar;
import com.kingcoder.pathfinding.algorithms.Dijkstra;
import com.kingcoder.pathfinding.algorithms.GBFS;
import com.kingcoder.pathfinding.graphics.Button;
import com.kingcoder.pathfinding.graphics.ButtonCInter;
import com.kingcoder.pathfinding.graphics.Slider;

public class Scene {

	public static GridMap map;

	private ArrayList<Panel> planes;
	private Slider slider;
	private Button clearButton;
	
	private static int mouseX, mouseY;
	
	// the colors
	public static Color backgroundFadeColor = new Color(0,0,0, 80);
	public static Color messageColor = new Color(183, 208, 255, 230);
	public static Color buttonColor = new Color(153, 153, 153, 200);
	public static Color buttonHovColor = new Color(183, 208, 255, 200);
	public static Color backgroundColor = new Color(198, 198, 198, 255);
	public static Color floorColor = new Color(211, 211, 211, 255);
	public static Color wallColor = new Color(81, 81, 81, 255);
	public static Color fadeWallColor = new Color(160, 160, 160, 255);
	public static Color gridColor = new Color(232, 232, 232, 255);
	public static Color goalColor = new Color(103, 143, 224, 255);
	public static Color goalClickedColor = new Color(86, 121, 191, 200);
	public static Color startColor = new Color(53, 186, 53, 255);
	public static Color startClickedColor = new Color(141, 193, 148, 200);
	public static Color openColor = new Color(68, 186, 255, 50);
	public static Color closedColor = new Color(68, 186, 255, 150);
	public static Color pathColor = new Color(10, 160, 0, 150);
	public static Color regularFontColor = new Color(70, 70, 70, 255);
	public static Color titleFontColor = new Color(54, 125, 196, 255);
	public static Color numberColor = new Color(0, 119, 204, 255);
	public static Color checkboxB = new Color(160, 160, 160, 255);
	public static Color checkbox_checked = new Color(103, 143, 224, 255);
	public static Color checkbox_hover = new Color(180, 180, 180, 255);
	
	
	// the fonts
	public static Font buttonTextFont = new Font(Font.MONOSPACED, Font.BOLD, 40);
	public static Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 26);
	public static Font regularFont = new Font(Font.MONOSPACED, 0, 12);
	public static Font regularFont2 = new Font(Font.MONOSPACED, 0, 18);
	
	private static boolean started = false;
	
	// the button
	private String buttonText = "GOT IT!";
	private int bX = 435, bY = 700;
	
	public Scene(){
		map = new GridMap(30, 30);
		
		planes = new ArrayList<Panel>();
		planes.add(new Dijkstra(50, 200));
		planes.add(new AStar(555, 200));
		planes.add(new GBFS(1060, 200));
		
		slider = new Slider(Main.WIDTH / 2 - 255, Main.HEIGHT - 100, 1, 1000, 50);
		clearButton = new Button("CLEAR THE MAP", 200, 780, 100, 35, new ButtonCInter() {			
			@Override
			public void onClick() {
				map.clear();
				for(int i=0; i < planes.size(); i++){
					planes.get(i).setChanged(true);
				}
			}
		});
	}
	
	public void render(Graphics g){
		// drawing the planes
		for(int i=0; i < planes.size(); i++){
			planes.get(i).render(g);
		}
		
		slider.render(g);
		clearButton.render(g);
		
		if(!started){
			g.setColor(backgroundFadeColor);
			g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
			// render the display message
			g.setColor(messageColor);
			g.fillRect(400, 200, 800, 600);
			
			// drawing the title text
			g.setColor(goalColor);
			g.setFont(titleFont);
			g.drawString("Welcome!", 435, 250);
			
			g.setFont(regularFont2);
			g.drawString(" . . . to a program for testing pathfinding algorithms . . .", 435, 300);
			
			// drawing the paragraphs
			g.setColor(regularFontColor);
			g.setFont(regularFont);
			
			// line break is 15 pixels thick
			g.drawString("This program is designed to demonstrate and test 3 best-known pathfinding algorithms", 435, 340);
			
			g.drawString("Those are:", 435, 370);
			g.drawString("	- Dijkstra algorithm", 435, 385);
			g.drawString("	- A* algorithm with JPS(Jump Point Search)", 435, 400);
			g.drawString("	- Greedy Best-First Search algorithm", 435, 415);
			
			g.drawString("The algorithms are designed for graph based applications", 435, 445);
			g.drawString("In this perticular program the graph is presented as a 2D grid", 435, 460);
			g.drawString("It consists of 'walkable' nodes, walls, a starting node and a goal node", 435, 475);
			
			g.drawString("To add or remove a wall just click LMB on any of the walkable nodes / walls", 435, 505);
			g.drawString("To change the starting or goal node position, just drag it around", 435, 520);
			g.drawString("To change how many times the algorithms will run just use the slider", 435, 535);
			
			g.drawString("As soon as you change the map, the algorithms will be run again to find the new shortest path", 435, 565);
			g.drawString("Also, the map can be cleared by pressing the button: 'CLEAR THE MAP'", 435, 580);
			g.drawString("To enable JPS with A*, just click the checkbox below the A* algorithm", 435, 595);
			g.drawString("Finally, time is measured for how much it took to find the path for all three algorithms", 435, 610);
			
			// the ending
			g.setColor(titleFontColor);
			g.setFont(titleFont);
			g.drawString("HAVE FUN!", 435, 650);
			
			// the signature
			g.setFont(regularFont2);
			g.drawString("made by Andraž Èepiè", 900, 750);
			
			// rendering the button
			if(mouseX > bX && mouseX < bX+250 && mouseY > bY && mouseY < bY+75){
				g.setColor(buttonHovColor);
				g.fillRect(bX, bY, 250, 75);
				
				// rendering the button text
				g.setFont(buttonTextFont);
				g.setColor(buttonColor);
				g.drawString(buttonText, bX + 35, bY + 45);
			}else{
				g.setColor(buttonColor);
				g.fillRect(bX, bY, 250, 75);
				
				// rendering the button text
				g.setFont(buttonTextFont);
				g.setColor(buttonHovColor);
				g.drawString(buttonText, bX + 35, bY + 45);
			}
		}
	}
	
	public void update(){
		mouseX = Main.input.getMouseX();
		mouseY = Main.input.getMouseY();
		
		if(!started){
			if(Main.input.buttonPressed(MouseEvent.BUTTON1)){
				if(mouseX > bX && mouseX < bX+250 && mouseY > bY && mouseY < bY+75){
					started = true;
				}
			}
		}else{
			// setting the runCounter for all three planes
			int runCounter = 0;
			
			// updating the slider
			slider.update();
			int sliderValue = slider.getSliderValue();
			
			// updating the clearMap Button 
			clearButton.update();
			
			// updating the planes
			for(int i=0; i < planes.size(); i++){
				planes.get(i).setRunCounter(sliderValue);
				
				if(slider.isUpdated()) planes.get(i).setChanged(true);
				
				planes.get(i).update();
			}
			
			// updating the map
			for(int i = 0; i < planes.size(); i++){
				if(!planes.get(i).isUpdated()) break;	
				
				if(i == planes.size() - 1)
					Scene.map.changed = false;	
			}
			
			slider.setUpdated(false);
		}
	}
	
	public static GridMap getMap(){
		return map;
	}
	
	public static int getMouseX(){
		return mouseX;
	}
	
	public static int getMouseY(){
		return mouseY;
	}
	
	public static boolean isStarted(){
		return started;
	}
	
}
