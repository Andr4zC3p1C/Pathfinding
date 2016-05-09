package com.kingcoder.pathfinding.algorithms;

import java.util.ArrayList;

import com.kingcoder.pathfinding.util.GridMap;
import com.kingcoder.pathfinding.util.Node;
import com.kingcoder.pathfinding.util.Panel;
import com.kingcoder.pathfinding.util.Panels;

public class Dijkstra extends Panel{
	// finds the cheapest path
	// slow
	
	public Dijkstra(int x, int y){
		super(x, y, "DIJKSTRA", Panels.dijkstra);
		renderNum = RENDER_G;
	}

	public ArrayList<Node> runAlgorithm(int startX, int startY, int goalX, int goalY) {
		open.clear();
		closed.clear();
		
		// creating all the nodes for the whole map
		Node startNode = new Node();
		startNode.x = startX;
		startNode.y = startY;
		startNode.g = 0;
		
		open.add(startNode);
		
		Node goalNode = new Node();
		goalNode.x = goalX;
		goalNode.y = goalY;
		
		while(!open.isEmpty()){		
			Node currentNode = getLowestG();
			open.remove(currentNode);
			closed.add(currentNode);
			
			// if we reached the goal we can stop
			if(currentNode.equals(goalNode)){
				goalNode = currentNode;
				break;
			}
			
			// checking all the neighbors
			for(Node neighbor : neighbors(currentNode)){
				if(neighbor == null) continue;
				
				int newCost = currentNode.g + distance(currentNode, neighbor);
				if(newCost < neighbor.g){
					neighbor.g = newCost;
					neighbor.parent = currentNode;
				}
			}
		}
		
		// retracing the path
		if(goalNode.parent != null){
			return retracePath(startNode, goalNode);
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
					result[i].g = 1000;
					open.add(result[i]);
				}
			}else {
				result[i] = null;
			}
		}
		
		return result;
	}
}
