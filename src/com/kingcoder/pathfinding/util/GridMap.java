package com.kingcoder.pathfinding.util;

public class GridMap {

	/*
	 * 
	 *  The map consists of nodes
	 *  - there are walls and floors
	 * 	- it's an array of integers
	 * 
	 */
	
	public static final int FLOOR = 0;
	public static final int WALL = 1;
	public static final int START = 2;
	public static final int GOAL = 3;
	public static final int OPEN = 4;
	public static final int CLOSED = 5;
	
	private int width, height;
	public int[][] map;
	public boolean[][] changedNodes;
	
	public boolean changed;
	
	public int startX, startY, goalX, goalY;
	
	public GridMap(int width, int height){
		this.width = width;
		this.height = height;
		
		map = new int[height][width];
		for(int y=0; y < height; y++){
			for(int x=0; x < width; x++){
				map[y][x] = FLOOR;
			}
		}
		
		// initializing the nodes
		startX = 5;
		startY = 26;
		goalX = 2;
		goalY = 1;
		
		int[][] startMap = {
				{0,11},{0,16},
				{1,11},{1,16},
				{2,5},{2,11},{2,16},
				{3,5},{3,11},{3,12},{3,16},
				{4,0},{4,1},{4,2},{4,3},{4,4},{4,5},{4,12},
				{5,12},
				{6,12},{6,16},
				{7,12},{7,16},
				{8,16},
				{9,0},{9,1},{9,2},{9,3},{9,4},{9,16},{9,17},{9,18},{9,19},{9,20},{9,21},{9,22},{9,23},{9,24},{9,25},{9,26},{9,29},
				{10,4},{10,5},{10,6},{10,7},{10,8},{10,9},{10,10},{10,11},{10,12},{10,13},{10,14},{10,15},{10,16},{10,26},{10,29},
				{11,4},
				{12,4},
				{13,4},{13,12},{13,25},{13,28},{13,29},
				{14,12},{14,25},
				{15,12},{15,25},
				{16,0},{16,1},{16,2},{16,3},{16,4},{16,5},{16,6},{16,7},{16,8},{16,9},{16,10},{16,11},{16,12},{16,13},{16,14},{16,15},{16,16},{16,17},{16,18},{16,19},{16,20},{16,23},{16,24},{16,25},
				{17,7},{17,11},{17,19},{17,25},
				{18,7},{18,11},{18,19},{18,25},
				{19,7},{19,11},{19,25},
				{20,7},{20,11},{20,25},
				{21,19},{21,25},{21,26},{21,29},
				{22,19},{22,25},
				{23,0},{23,3},{23,4},{23,5},{23,6},{23,7},{23,11},{23,19},{23,20},{23,23},{23,24},{23,25},
				{24,7},{24,11},{24,19},{24,25},
				{25,7},{25,11},{25,12},{25,13},{25,14},{25,15},{25,16},{25,17},{25,18},{25,19},{25,25},
				{26,7},{26,19},{26,25},
				{27,7},{27,19},{27,25},
				{28,7},{28,11},{28,19},{28,25},
				{29,7},{29,11},{29,19},{29,25}
		};
		
		for(int i=0; i < startMap.length; i++){
				map[startMap[i][0]][startMap[i][1]] = GridMap.WALL;
		}
		
		changedNodes = new boolean[height][width];
		for(int y=0; y < height; y++){
			for(int x=0; x < width; x++){
				changedNodes[y][x] = false;
			}
		}
	}
	
	public void addOrRemoveWall(int x, int y){
		if(map[y][x] == WALL)
			map[y][x] = FLOOR;
		else if(map[y][x] == FLOOR)
			map[y][x] = WALL;
	}
	
	public void clear(){
		for(int y =0; y < map.length; y++){
			for(int x = 0; x < map[0].length; x++){
				if(map[y][x] == WALL)
					map[y][x] = FLOOR;
			}
		}
		
		changed = true;
	}
	
	public boolean isOutsideOfMap(int x, int y){
		return (x < 0 || x > map[0].length - 1 || y < 0 || y > map.length - 1);
	}
	
}
