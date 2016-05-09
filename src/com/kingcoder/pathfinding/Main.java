package com.kingcoder.pathfinding;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.kingcoder.pathfinding.graphics.Window;
import com.kingcoder.pathfinding.util.Input;
import com.kingcoder.pathfinding.util.Scene;

public class Main extends Canvas implements Runnable{
	private static final long serialVersionUID = -2996007049657890496L;
	
	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;
	public static final String TITLE = "Pathfinding algorithms 3.1";
	
	private Thread mainThread;
	private Window window;
	public static Input input;
	private static boolean running;
	
	private Scene scene;
	
	public Main(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		scene = new Scene();
		
		input = new Input();
		addMouseMotionListener(input);
		addMouseListener(input);
		
		running = true;
		
		window = new Window(TITLE);
		window.add(this);
		window.pack();
		window.setVisible();
		
		
		mainThread = new Thread(this, "main");
		mainThread.start();
	}
	
	public void run(){
		// we are limiting the FPS to 60
		long FPS = 1000000000 / 60;
		long timerFPS = System.nanoTime();
		while(running){
			if(System.nanoTime() - timerFPS >= FPS){
				update();
				render();
				timerFPS = System.nanoTime();
			}
		}
	}
	
	public void update(){
		scene.update();
		input.update();
	}
	
	public void render(){
		if(getBufferStrategy() == null){
			createBufferStrategy(3);
		}
		
		Graphics g = getBufferStrategy().getDrawGraphics();
		
		// call g function calls here
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		scene.render(g);
		
		g.dispose();
		getBufferStrategy().show();
	}
	
	public static void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		int ARR_SIZE = 3;
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
	
	public static void main(String[] args){
		new Main();
	}
	
}
