package com.kingcoder.pathfinding.util;

public class Node {
	public static final int INFINITY_G = 1000000;
	
	public int x;
	public int y;
	public int g;
	public int h;
	public int f;
	
	public Node parent;
	
	public void initH(int goalX, int goalY){
		h = Math.abs(goalX - x) * 10 + Math.abs(goalY - y) * 10;
	}
	
	public boolean equals(Node node){
		if(x == node.x && y == node.y){
			return true;
		}
		
		return false;
	}
}
