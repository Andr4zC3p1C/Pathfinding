package com.kingcoder.pathfinding.util;

public enum Panels{
	astar(false), dijkstra(false), gbfs(false);
	
	public boolean jps;
	
	private Panels(boolean jps){
		this.jps = jps;
	}
	
}