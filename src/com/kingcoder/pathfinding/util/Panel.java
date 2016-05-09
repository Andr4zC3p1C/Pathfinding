package com.kingcoder.pathfinding.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import com.kingcoder.pathfinding.Main;
import com.kingcoder.pathfinding.graphics.Checkbox;

public abstract class Panel{
	
	public static final int SIZE = 30 * 16;
	public static final int RENDER_F = 0;
	public static final int RENDER_H = 1;
	public static final int RENDER_G = 2;
	
	protected Panels p;
	protected int x, y;
	protected long timer = System.nanoTime();
	protected long timeItTook = 0;
	protected GridMap map;
	
	private String title;
	
	private int prevX, prevY, prevNode, prevPNode;
	
	private boolean draggingStart = false;
	private boolean draggingGoal = false;
	private boolean updated = false;
	
	private Checkbox checkbox;
	
	// the counter for how many times the algorithm should be run
	private int runCounter = 200;
	
	public Panel(int x, int y, String title, Panels p){
		this.x = x;
		this.y = y;
		this.title = title;
		this.p = p;
		
		// initializing the check-box
		if(p == Panels.astar){
			checkbox = new Checkbox("Jump Point Search", x, y + 500);
		}
		
		map = new GridMap(30, 30);
		map.changed = true;
		updated = false;
	}
	
	protected ArrayList<Node> open = new ArrayList<Node>();
	protected ArrayList<Node> closed = new ArrayList<Node>();
	private ArrayList<Node> path = new ArrayList<Node>();
	
	// for rendering numbers
	protected int renderNum = 0;
	
	public void render(Graphics g){
		
		// drawing the floor
		for(int y=0; y < map.map.length; y++){
			for(int x=0; x < map.map[0].length; x++){
				if(map.map[y][x] == GridMap.FLOOR){
					g.setColor(Scene.floorColor);
					g.fillRect(this.x + x*16, this.y + y*16, 16, 16);
				}
			}
		}
		
		// drawing the closed
		if(closed != null){
			for(int i=0; i < closed.size(); i++){
				Node n = closed.get(i);
				int x = n.x;
				int y = n.y;
				g.setColor(Scene.closedColor);
				g.fillRect(this.x + x*16, this.y + y*16, 16, 16);
				
				// drawing the numbers inside the squares
				g.setColor(Scene.regularFontColor);
				g.setFont(Scene.regularFont);
			}
		}
		
		// drawing the open
		if(open != null){
			for(int i=0; i < open.size(); i++){
				Node n = open.get(i);
				int x = n.x;
				int y = n.y;
				g.setColor(Scene.openColor);
				g.fillRect(this.x + x*16, this.y + y*16, 16, 16);
				
				// drawing the numbers inside the squares
				g.setColor(Scene.regularFontColor);
				g.setFont(Scene.regularFont);
			}
		}
		
		// drawing the path
		if(path != null){
			for(int i=0; i < path.size(); i++){
				Node n = path.get(i);
				int x = n.x;
				int y = n.y;
				g.setColor(Scene.pathColor);
				g.fillRect(this.x + x*16, this.y + y*16, 16, 16);
			}
		}
		
		// drawing walls
		for(int y=0; y < map.map.length; y++){
			for(int x=0; x < map.map[0].length; x++){
				if(map.map[y][x] == GridMap.WALL){
					g.setColor(Scene.wallColor);
					g.fillRect(this.x + x*16, this.y + y*16, 16, 16);
				}
			}
		}
		
		// drawing start
		g.setColor(Scene.startColor);
		g.fillRect(this.x + map.startX*16, this.y + map.startY*16, 16, 16);
		g.setColor(Scene.regularFontColor);
		g.setFont(Scene.regularFont);
		g.drawString("S", this.x + map.startX * 16 + 4, this.y + (map.startY+1) * 16 - 4);
		
		// drawing goal
		g.setColor(Scene.goalColor);
		g.fillRect(this.x + map.goalX*16, this.y + map.goalY*16, 16, 16);
		g.setColor(Scene.regularFontColor);
		g.setFont(Scene.regularFont);
		g.drawString("G", this.x + map.goalX * 16 + 4, this.y + (map.goalY+1) * 16 - 4);
		
		// drawing the grid
		g.setColor(Scene.gridColor);
		for(int i=0; i < 31; i++){
			g.drawLine(x + i*16, y, x + i*16, y + 480);
			g.drawLine(x, y + i * 16, x + 480, y + i*16);
		}
		
		// drawing the arrows
		if(path != null && p == Panels.astar && p.jps == true){
			for(int i=0; i < path.size(); i++){
				Node n = path.get(i);
				int x = n.x;
				int y = n.y;		

				if(i+1 < path.size()){
					g.setColor(Color.BLUE);
					Node n2 = path.get(i+1);
					Main.drawArrow(g, this.x + x*16 + 8, this.y + y*16 + 8, this.x + n2.x*16 + 8, this.y + n2.y*16 + 8);
				}
			}
		}
		
		// drawing the mouse wall
		if(Scene.getMouseX() > x && Scene.getMouseX() < x+480 && Scene.getMouseY() > y && Scene.getMouseY() < y+480){
			int tx = (Scene.getMouseX() - x) / 16;
			int ty = (Scene.getMouseY() - y) / 16;
			
			if(map.startX == tx && map.startY == ty){
				g.setColor(Scene.startColor);
				g.fillRect(this.x + tx*16 - 3, this.y + ty*16 - 3, 16 + 6, 16 + 6);
			}else if(map.goalX == tx && map.goalY == ty){
				g.setColor(Scene.goalColor);
				g.fillRect(this.x + tx*16 - 3, this.y + ty*16 - 3, 16 + 6, 16 + 6);
			}else{
				g.setColor(Scene.fadeWallColor);
				g.fillRect(this.x + tx*16, this.y + ty*16, 16, 16);
			}
		}
		
		
		// drawing the title and statistics
		g.setColor(Scene.backgroundColor);
		g.fillRect(x, y - 135, 480, 100);
		
		g.setColor(Scene.titleFontColor);
		g.setFont(Scene.titleFont);
		g.drawString(title, x + 10, y - 100);
		
		g.setColor(Scene.regularFontColor);
		g.setFont(Scene.regularFont);
		g.drawString("Time it took to find the path: ", x + 10, y - 75);
		
		// timer
		g.setColor(Scene.numberColor);
		g.drawString(String.valueOf((double)timeItTook/ 1000000) + " ms", x + 10, y - 50);
		
		// run counter
		g.setColor(Color.RED);
		g.drawString("run " + runCounter + " times", x + 300, y - 50);
		
		// drawing the AStar check-box
		if(p == Panels.astar){
			checkbox.render(g);
		}
	}

	public void update(){
		if(p == Panels.astar){
			checkbox.update();
			if(checkbox.updated()) setChanged(true);
			
			p.jps = checkbox.getChecked();
		}
		
		// if button pressed then add/remove wall and re do path-finding process
		if(Scene.isStarted()){
			if(Main.input.buttonPressed(MouseEvent.BUTTON1)){
				if(Scene.getMouseX() > x && Scene.getMouseX() < x+480 && Scene.getMouseY() > y && Scene.getMouseY() < y+480){
					int tx = (Scene.getMouseX() - x) / 16;
					int ty = (Scene.getMouseY() - y) / 16;
					
					if(draggingStart){
						map.startX = tx;
						map.startY = ty;
					}else if(draggingGoal){
						map.goalX = tx;
						map.goalY = ty;
					}else{
						if(tx == map.startX && ty == map.startY){
							draggingStart = true;
						}else if(tx == map.goalX && ty == map.goalY){
							draggingGoal = true;
						}else{
							if(!map.changedNodes[ty][tx]){
								map.addOrRemoveWall(tx, ty);
								map.changedNodes[ty][tx] = true;
							}
						}		
					}
					
					map.changed = true;
					Scene.map.map = map.map;
					Scene.map.startX = map.startX;
					Scene.map.startY = map.startY;
					Scene.map.goalX = map.goalX;
					Scene.map.goalY = map.goalY;
					Scene.map.changed = true;
					updated = false;
				}
			}else{
				if(map.changed){
					for(int j=0; j < map.changedNodes.length; j++){
						for(int i=0; i < map.changedNodes[0].length; i++){
							map.changedNodes[j][i] = false;
						}
					}
					
					draggingStart = false;
					draggingGoal = false;
					
					// run the algorithm 1000 times and measure the time taken for path-finding
					timer = System.nanoTime();
					
					for(int i=0; i < runCounter; i++){
						path = runAlgorithm(map.startX, map.startY, map.goalX, map.goalY);
					}
					
					timeItTook = System.nanoTime() - timer;
					
					map.changed = false;
				}else{
					map.map = Scene.map.map;
					map.startX = Scene.map.startX;
					map.startY = Scene.map.startY;
					map.goalX = Scene.map.goalX;
					map.goalY = Scene.map.goalY;
					
					if(Scene.map.changed){
						
						// run the algorithm runCounter times and measure the time taken for path-finding
						timer = System.nanoTime();
						
						for(int i=0; i < runCounter; i++){
							path = runAlgorithm(map.startX, map.startY, map.goalX, map.goalY);
						}
						
						timeItTook = System.nanoTime() - timer;
						
						updated = true;
					}
				}
				
			}
		}
	}
	
	public boolean isUpdated(){
		return updated;
	}
	
	public void setRunCounter(int counter){
		runCounter = counter;
	}
	
	
	public void setChanged(boolean changed){
		map.changed = changed;
	}
	
	
	// algorithm functions
	public abstract ArrayList<Node> runAlgorithm(int startX, int startY, int goalX, int goalY);
	
	protected boolean isInClosed(int x, int y){	
		for(int i=0; i < closed.size(); i++){
			Node nTest = closed.get(i);
			if(nTest.x == x && nTest.y == y){
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean isInOpen(int x, int y){	
		for(int i=0; i < open.size(); i++){
			Node nTest = open.get(i);
			
			if(nTest.x == x && nTest.y == y){
				return true;
			}
		}
		
		return false;
	}
	
	
	protected int distance(Node n1, Node n2){
		float x = Math.abs(n1.x - n2.x);
		float y = Math.abs(n1.y - n2.y);
		float l = (float)(Math.sqrt(x*x + y*y));
		
		return (int)(l * 10);
	}
	
	protected Node getNodeFromOpen(int x, int y){	
		for(int i=0; i < open.size(); i++){
			Node n = open.get(i);
			if(x == n.x && y == n.y){
				return n;
			}
		}
		
		return null; // the node does not exist --> void node
	}
	
	protected Node getNodeFromClosed(int x, int y){
		for(int i=0; i < closed.size(); i++){
			Node n = closed.get(i);
			if(x == n.x && y == n.y){
				return n;
			}
		}
		
		return null; // the node does not exist --> void node
	}
	
	protected Node getLowestG(){
		int min = 999999;
		int index = 0;
		for(int i=0; i < open.size(); i++){
			Node n = open.get(i);
			if(n.g < min){
				min = n.g;
				index = i;
			}
		}
		
		return open.get(index);
	}
	
	protected Node getLowestH(){
		int min = 999999;
		int index = 0;
		for(int i=0; i < open.size(); i++){
			Node n = open.get(i);
			if(n.h < min){
				min = n.h;
				index = i;
			}
		}
		
		return open.get(index);
	}
	
	protected Node getLowestF(){
		int min = 999999;
		int index = 0;
		for(int i=0; i < open.size(); i++){
			Node n = open.get(i);
			if(n.f < min){
				min = n.f;
				index = i;
			}
		}
		
		return open.get(index);
	}
	
	protected ArrayList<Node> retracePath(Node start, Node goal){
		ArrayList<Node> path = new ArrayList<Node>();
		Node current = goal;
		
		// creating the path
		while(!current.equals(start)){
			path.add(current);
			current = current.parent;
		}
		
		path.add(start);
		
		// reversing the path
		Collections.reverse(path);
		
		return path;
	}
}
