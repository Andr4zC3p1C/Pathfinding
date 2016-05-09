package com.kingcoder.pathfinding.algorithms;

import java.util.ArrayList;

import com.kingcoder.pathfinding.util.GridMap;
import com.kingcoder.pathfinding.util.Node;
import com.kingcoder.pathfinding.util.Panel;
import com.kingcoder.pathfinding.util.Panels;

public class AStar extends Panel{
	// Combines the Dijkstra and Greedy Best-First Search algorithms
	// fast and always finds the shortest path
	
	private int goalX;
	private int goalY;
	
	private boolean JPS = false;
	
	public AStar(int x, int y){
		super(x, y, "A*", Panels.astar);
		renderNum = RENDER_F;
	}

	public ArrayList<Node> runAlgorithm(int startX, int startY, int goalX, int goalY) {
		open.clear();
		closed.clear();
		
		this.goalX = goalX;
		this.goalY = goalY;
		
		Node start = new Node();
		start.x = startX;
		start.y = startY;
		start.g = 0;
		start.initH(goalX, goalY);
		
		open.add(start);
		
		Node goal = new Node();
		goal.x = goalX;
		goal.y = goalY;
		
		if(p.jps){ // with JPS
			while(!open.isEmpty()){
				Node current = getLowestF();
				open.remove(current);
				closed.add(current);
				
				if(current.equals(goal)){
					goal = current;
					break;
				}
				
				for(int dx = -1; dx < 2; dx++){
					for(int dy = -1; dy < 2; dy++){
						if(dx == 0 && dy == 0) continue; // not moving
						
						if(Math.abs(dx) == 1 && Math.abs(dy) == 1){
							if(!map.isOutsideOfMap(current.x, current.y + dy) && !map.isOutsideOfMap(current.x + dx, current.y)){
								if(map.map[current.y + dy][current.x] == GridMap.WALL){
									if(map.map[current.y][current.x + dx] == GridMap.WALL){
										continue;
									}
								}
							}
						}
						
						Node n = JPS(current, dx, dy, current, goal);
						if(n == null) continue;
						if(isInClosed(n.x, n.y)) continue; // is in closed so we can just skip the node
						
						if(!isInOpen(n.x, n.y)){
							n.initH(goalX, goalY);
							n.f = n.g + n.h;
							n.parent = current;
							open.add(n);
						}else{
							n  = getNodeFromOpen(n.x, n.y);
							int newCost = current.g + distance(current, n);
							if(newCost < n.g){
								n.g = newCost;
								n.f = n.g + n.h;
								n.parent = current;
							}
						}
					}
				}
			}
		}else{ // without JPS
			while(!open.isEmpty()){		
				Node current = getLowestF();
				open.remove(current);
				closed.add(current);
				
				if(current.equals(goal)){
					goal = current;
					break;
				}
				
				// checking all the neighbors
				for(Node neighbor : neighbors(current)){
					if(neighbor == null) continue;
					
					int newCost = current.g + distance(current, neighbor);
					if(newCost < neighbor.g){
						neighbor.g = newCost;
						neighbor.f = neighbor.g + neighbor.h;
						neighbor.parent = current;
					}
				}
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
					result[i].g = n.g + distance(n, result[i]);
					result[i].initH(goalX, goalY);
					result[i].f = result[i].g + result[i].h;
					result[i].parent = n;
					open.add(result[i]);
				}
			}else {
				result[i] = null;
			}
		}
		
		return result;
	}
	
	
	public Node JPS(Node initialNode, int dx, int dy, Node startNode, Node goalNode){
		Node n = new Node();
		n.x = initialNode.x + dx;
		n.y = initialNode.y + dy;
		n.g = initialNode.g + distance(initialNode, n);
		
		// check if n is an OBSTICLE or OUTSIDE of the grid
		if(n.x < 0 || n.x > map.map.length - 1 || n.y < 0 || n.y > map.map.length - 1){
			return null;
		}
		if(map.map[n.y][n.x] == GridMap.WALL){
			return null;
		}
		
		// checking if the node is the goal node
		if(n.equals(goalNode)){
			return n;
		}
		
		// checking if the node is a forced neighbor
		if(hasForcedNeighbor(n, dx, dy)){
			return n;
		}
		
		// if diagonal
		if(Math.abs(dx) == 1 && Math.abs(dy) == 1){
			
			if(Math.abs(dx) == 1 && Math.abs(dy) == 1){
				if(!map.isOutsideOfMap(n.x, n.y + dy) && !map.isOutsideOfMap(n.x + dx, n.y)){
					if(map.map[n.y + dy][n.x] == GridMap.WALL){
						if(map.map[n.y][n.x + dx] == GridMap.WALL){
							return null;
						}
					}
				}
			}
			
			// horizontal check
			if(JPS(n, dx, 0, startNode, goalNode) != null){
				return n;
			}
			
			// vertical check
			if(JPS(n, 0, dy, startNode, goalNode) != null){
				return n;
			}
		}
		
		return JPS(n, dx, dy, startNode, goalNode);
	}
	
	private boolean hasForcedNeighbor(Node n, int dx, int dy){
		boolean a = false;
		boolean b = false;
		
		if(dx == 1 && dy == 1){ // diagonal up & right
			if(!map.isOutsideOfMap(n.x - 1, n.y) && !map.isOutsideOfMap(n.x - 1, n.y + 1))
				a = (map.map[n.y][n.x - 1] == GridMap.WALL && !(map.map[n.y + 1][n.x - 1] == GridMap.WALL));
			
			if(!map.isOutsideOfMap(n.x, n.y - 1) && !map.isOutsideOfMap(n.x + 1, n.y - 1))
				b = (map.map[n.y - 1][n.x] == GridMap.WALL && !(map.map[n.y - 1][n.x + 1] == GridMap.WALL));

		}else if(dx == -1 && dy == -1){ // diagonal down & left
			if(!map.isOutsideOfMap(n.x + 1, n.y) && !map.isOutsideOfMap(n.x + 1, n.y - 1))
				a = (map.map[n.y][n.x + 1] == GridMap.WALL && !(map.map[n.y - 1][n.x + 1] == GridMap.WALL));
			
			if(!map.isOutsideOfMap(n.x, n.y + 1) && !map.isOutsideOfMap(n.x - 1, n.y + 1))
				b = (map.map[n.y + 1][n.x] == GridMap.WALL && !(map.map[n.y + 1][n.x - 1] == GridMap.WALL));
			
		}else if(dx == -1 && dy == 1){ // diagonal up & left
			if(!map.isOutsideOfMap(n.x + 1, n.y) && !map.isOutsideOfMap(n.x + 1, n.y + 1))
				a = (map.map[n.y][n.x + 1] == GridMap.WALL && !(map.map[n.y + 1][n.x + 1] == GridMap.WALL));
			
			if(!map.isOutsideOfMap(n.x, n.y - 1) && !map.isOutsideOfMap(n.x - 1, n.y - 1))
				b = (map.map[n.y - 1][n.x] == GridMap.WALL && !(map.map[n.y - 1][n.x - 1] == GridMap.WALL));
			
		}else if(dx == 1 && dy == -1){ // diagonal down & right
			if(!map.isOutsideOfMap(n.x - 1, n.y) && !map.isOutsideOfMap(n.x - 1, n.y - 1))
				a = (map.map[n.y][n.x - 1] == GridMap.WALL && !(map.map[n.y - 1][n.x - 1] == GridMap.WALL));
				
			if(!map.isOutsideOfMap(n.x, n.y + 1) && !map.isOutsideOfMap(n.x + 1, n.y + 1))
				b = (map.map[n.y + 1][n.x] == GridMap.WALL && !(map.map[n.y + 1][n.x + 1] == GridMap.WALL));
			
		}else if(dx == 1 && dy == 0){ // horizontal right
			if(!map.isOutsideOfMap(n.x, n.y + 1) && !map.isOutsideOfMap(n.x + 1, n.y + 1))
				a = (map.map[n.y + 1][n.x] == GridMap.WALL && !(map.map[n.y + 1][n.x + 1] == GridMap.WALL));
			
			if(!map.isOutsideOfMap(n.x, n.y - 1) && !map.isOutsideOfMap(n.x + 1, n.y - 1))
				b = (map.map[n.y - 1][n.x] == GridMap.WALL && !(map.map[n.y - 1][n.x + 1] == GridMap.WALL));
			
		}else if(dx == -1 && dy == 0){ // horizontal left
			if(!map.isOutsideOfMap(n.x, n.y + 1) && !map.isOutsideOfMap(n.x - 1, n.y + 1))
				a = (map.map[n.y + 1][n.x] == GridMap.WALL && !(map.map[n.y + 1][n.x - 1] == GridMap.WALL));
			
			if(!map.isOutsideOfMap(n.x, n.y - 1) && !map.isOutsideOfMap(n.x - 1, n.y - 1))
				b = (map.map[n.y - 1][n.x] == GridMap.WALL && !(map.map[n.y - 1][n.x - 1] == GridMap.WALL));
			
		}else if(dx == 0 && dy == 1){ // vertical up
			if(!map.isOutsideOfMap(n.x + 1, n.y) && !map.isOutsideOfMap(n.x + 1, n.y + 1))
				a = (map.map[n.y][n.x + 1] == GridMap.WALL && !(map.map[n.y + 1][n.x + 1] == GridMap.WALL));
			
			if(!map.isOutsideOfMap(n.x - 1, n.y) && !map.isOutsideOfMap(n.x - 1, n.y + 1))
				b = (map.map[n.y][n.x - 1] == GridMap.WALL && !(map.map[n.y + 1][n.x - 1] == GridMap.WALL));
			
		}else if(dx == 0 && dy == -1){ // vertical down
			if(!map.isOutsideOfMap(n.x + 1, n.y) && !map.isOutsideOfMap(n.x + 1, n.y - 1))
				a = (map.map[n.y][n.x + 1] == GridMap.WALL && !(map.map[n.y - 1][n.x + 1] == GridMap.WALL));
			
			if(!map.isOutsideOfMap(n.x - 1, n.y) && !map.isOutsideOfMap(n.x - 1, n.y - 1))
				b = (map.map[n.y][n.x - 1] == GridMap.WALL && !(map.map[n.y - 1][n.x - 1] == GridMap.WALL));
		}
			
		return a || b;
	}
	
}
