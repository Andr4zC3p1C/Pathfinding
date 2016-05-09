package com.kingcoder.pathfinding.algorithms;

import java.util.ArrayList;

import com.kingcoder.pathfinding.util.GridMap;
import com.kingcoder.pathfinding.util.Node;
import com.kingcoder.pathfinding.util.Panel;
import com.kingcoder.pathfinding.util.Panels;

public class GBFS extends Panel{
	// Greedy Best-First Search
	// really fast but not sure to find the shortest path
	
	public GBFS(int x, int y){
		super(x, y, "Greedy Best-First Search", Panels.gbfs);
		renderNum = RENDER_H;
	}

	private int goalX;
	private int goalY;
	
	public ArrayList<Node> runAlgorithm(int startX, int startY, int goalX, int goalY) {
		open.clear();
		closed.clear();
		
		this.goalX = goalX;
		this.goalY = goalY;
		
		Node start = new Node();
		start.x = startX;
		start.y = startY;
		
		open.add(start);
		
		Node goal = new Node();
		goal.x = goalX;
		goal.y = goalY;
		
		while(!open.isEmpty()){
			
			Node current = getLowestH();
			open.remove(current);
			closed.add(current);
			
			if(current.equals(goal)){
				goal = current;
				break;
			}
			
			// checking all the neighbors
			for(Node neighbor : neighbors(current)){
				if(neighbor == null) continue;
			}
		}
		
		// retracing the path
		if(goal.parent != null){
			return retracePath(start, goal);
		}
		
		return null;
	}
	
	private Node[] neighbors(Node n){
		Node[] result = new Node[8];
		
		// setting the nodes
		int xl = n.x - 1;
		int xm = n.x;
		int xr = n.x + 1;
		int yd = n.y - 1;
		int ym = n.y;
		int yu = n.y + 1;
		
		for(int i=0; i < 8; i++){
			int x = 0;
			int y = 0;
			
			switch(i){
			case 0:
				x = xm;
				y = yd;
				break;
				
			case 1:
				x = xl;
				y = ym;
				break;
				
			case 2:
				x = xr;
				y = ym;
				break;
				
			case 3:
				x = xm;
				y = yu;
				break;
				
			case 4:
				x = xl;
				y = yd;
				try{
					if(map.map[n.y][n.x - 1] == GridMap.WALL && map.map[n.y - 1][n.x] == GridMap.WALL){
						result[i] = null;
						continue;
					}
				}catch(ArrayIndexOutOfBoundsException e){}
				
				break;
				
			case 5:
				x = xr;
				y = yd;
				try{
					if(map.map[n.y][n.x + 1] == GridMap.WALL && map.map[n.y - 1][n.x] == GridMap.WALL){
						result[i] = null;
						continue;
					}
				}catch(ArrayIndexOutOfBoundsException e){}
				
				break;
				
			case 6:
				x = xl;
				y = yu;
				try{
					if(map.map[n.y][n.x - 1] == GridMap.WALL && map.map[n.y + 1][n.x] == GridMap.WALL){
						result[i] = null;
						continue;
					}
				}catch(ArrayIndexOutOfBoundsException e){}
				
				break;
				
			case 7:
				x = xr;
				y = yu;
				try{
					if(map.map[n.y][n.x + 1] == GridMap.WALL && map.map[n.y + 1][n.x] == GridMap.WALL){
						result[i] = null;
						continue;
					}
				}catch(ArrayIndexOutOfBoundsException e){}
				
				break;
			}
			
			// checking if the node is out of bounds
			if(map.isOutsideOfMap(x, y)){
				result[i] = null;
				continue;
			}
			
			if(!isInClosed(x, y) && map.map[y][x] != GridMap.WALL){
				if(isInOpen(x, y)){
					result[i] = getNodeFromOpen(x, y);
				}else{
					result[i] = new Node();
					result[i].x = x;
					result[i].y = y;
					result[i].initH(goalX, goalY);
					result[i].parent = n;
					open.add(result[i]);
				}
			}else {
				result[i] = null;
			}
		}
		
		return result;
	}
	
}
