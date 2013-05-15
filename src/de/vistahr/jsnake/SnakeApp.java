/**
 * 
 * 	Copyright 2012 Vince. All rights reserved.
 * 	
 * 	Redistribution and use in source and binary forms, with or without modification, are
 * 	permitted provided that the following conditions are met:
 * 	
 * 	   1. Redistributions of source code must retain the above copyright notice, this list of
 * 	      conditions and the following disclaimer.
 * 	
 * 	   2. Redistributions in binary form must reproduce the above copyright notice, this list
 * 	      of conditions and the following disclaimer in the documentation and/or other materials
 * 	      provided with the distribution.
 * 	
 * 	THIS SOFTWARE IS PROVIDED BY Vince ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * 	WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * 	FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Vince OR
 * 	CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * 	CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * 	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * 	ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * 	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * 	ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 	
 * 	The views and conclusions contained in the software and documentation are those of the
 * 	authors and should not be interpreted as representing official policies, either expressed
 * 	or implied, of Vince.
 */
package de.vistahr.jsnake;

import java.util.Date;
import java.util.Timer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

import de.vistahr.jsnake.annotation.DirectionEnum;
import de.vistahr.jsnake.element.Snake;
import de.vistahr.jsnake.exception.SnakeHitElementException;
import de.vistahr.jsnake.task.ElementTask;
import de.vistahr.jsnake.util.ElementManagerUtil;
import de.vistahr.jsnake.util.InputManagerUtil;
import de.vistahr.jsnake.util.RenderManagerUtil;
import de.vistahr.map.AbstractField;
import de.vistahr.map.Map1;

public class SnakeApp
{
	/** Game Settings **/
	public final static String APP_NAME = "jSNake";
	public final static int APP_WIDTH   = 800;
	public final static int APP_HEIGHT 	= 600;
	
	public final boolean fullscreen = false;
	
	
	/** Framedata **/
	private int fps = 0;
	private long lastFPS = 0;
	
	/** Gamedata **/
	private AbstractField gamefield;
	private int level = 1;
	private int points = 0;
	private Date startedAt;
	private boolean running = false;
	private boolean gameOver = false;
	private DirectionEnum currentDirection = DirectionEnum.NONE;
	private double speedCounter = 0;
	
	private Snake snake;
	
	private Timer elementTimer;
	
	private UnicodeFont fontBig;
	private UnicodeFont fontMedium;
	
	
	private void startOpenGL(boolean fullscreen) {
		try {
			Display.setDisplayMode(new DisplayMode(APP_WIDTH,APP_HEIGHT));
			Display.setTitle(APP_NAME); 
			Display.setInitialBackground(0f,0f,0f);
			Display.setResizable(false);
			Display.setFullscreen(fullscreen);
			Display.create();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// 2d
		RenderManagerUtil.enableOrthoView(Display.getWidth(), Display.getHeight());
	}
	
	
	private void updateFPS() {
		if(lastFPS == 0)
			lastFPS = getTime(); // call before loop to init fps timer
	    if (getTime() - lastFPS > 1000) {
	        //Display.setTitle(" FPS: " + fps); 
	        fps = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
	    fps++;
	}
	
	
	private long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	
	private void gameLoop() {
		while (true) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			updateFPS(); // set framerate to 60 FPS
			render(); // draw
			
			// poll keyboard 
			int keyPressedInput = InputManagerUtil.pollKeyboard();
			
			DirectionEnum moveTo = InputManagerUtil.inputToDirection(keyPressedInput, currentDirection);
			try {
				// if nothing is pressed, the current snake direction will be set
				if(moveTo == DirectionEnum.NONE)
					moveTo = currentDirection;
				
				// set new current direction and move
				currentDirection = moveTo;
				
				if(speedCounter <= 0 && snake != null) {
					addPoints(ElementManagerUtil.moveSnakeAndSetOnGamefield(snake, gamefield, moveTo));
					resetSpeedCounter();
					
				} else {
					speedCounter--;
				}
				
				
			} catch (SnakeHitElementException e) {
				gameOver();
			}
				

			// press space to start
			if(keyPressedInput == Keyboard.KEY_SPACE)
				startOrPause();
			
			
			if(Display.isCloseRequested() || keyPressedInput == Keyboard.KEY_ESCAPE) {
				stop();
				break;
			}
			
			
		}
	}
	
	
	private void render() {
		if(running == true)
			RenderManagerUtil.drawBackground(Display.getWidth(), Display.getHeight());
		
		//RenderManagerUtil.drawFieldGrid(gamefield); // DEBUG GRID
		RenderManagerUtil.drawElements(gamefield);
		
		// render text after drawing elements
		drawGameTitle();
		drawGameMenu();
		drawGameOver();
		drawHUD();
		drawFPS();
		drawCredits();
		
		Display.update();
		Display.sync(60);
	}
	
	
	private void drawGameTitle() {
		if(running == false && gameOver == false) {
			RenderManagerUtil.drawTitle(APP_NAME);
		}
	}
	
	
	private void drawGameMenu() {
		if(running == false) {
			// Draw game menu
			RenderManagerUtil.drawString(175, (Display.getHeight() / 2) - 50, "* press space to start the game", Color.orange);
			RenderManagerUtil.drawString(250, (Display.getHeight() / 2), "* press esc to exit", Color.orange);
		}
	}
	
	
	private void drawGameOver() {
		if(running == false && gameOver == true) {
			// Draw game menu
			RenderManagerUtil.drawTitle("Game Over");
			RenderManagerUtil.drawString(250, (Display.getHeight() / 2) + 100, "level " + level + " with " + points + " points", Color.red);
		}
	}	
	
	private void drawHUD() {
		if(running == true) {
			RenderManagerUtil.drawString(20, 20, "Level " + level + " Points " + points, Color.white);
		}
	}

	
	private void drawFPS() {
		if(running == true) {
			RenderManagerUtil.drawString(Display.getWidth() - 50, Display.getHeight() - 30, "" + fps, Color.white);
		}
	}
	
	
	private void drawCredits() {
		if(running == false) {
			RenderManagerUtil.drawString(30, Display.getHeight() - 30, "by vistahr - 2012", Color.gray);
		}
	}
	
	private void startOrPause() {
		// NEW GAME
		if(running == false) {
			// prepare map
			gamefield.clear();
			resetSpeedCounter();
			
			// set snake to random position
			snake = new Snake(gamefield.getFreeRandomPosition());
			snake.appendElement();
			snake.appendElement();
			snake.appendElement();
			
			running   = true;
			gameOver  = false;
			startedAt = new Date();
			level  	  = 1;
			points 	  = 0;
			
			// every 5 seconds new elements
			elementTimer = new Timer();
			elementTimer.schedule(new ElementTask(gamefield), 1000, 2000);

		} else {
			// pause - set Direction to none. Snake is not moving
			currentDirection = DirectionEnum.NONE;
		}
	}
	
	
	private void addPoints(int points) {
		this.points += points;
		updateLevel();
		Display.setTitle(APP_NAME); 
	}
	
	/**
	 * every 6 points, increment 1 level
	 */
	private void updateLevel() {
		if(points % 8 == 0 && points != 0) {
			level++;
			points += 2;
		}
	}
	
	
	private void gameOver() {
		if(running == true) {
			running = false;
			gameOver = true;
			gamefield.clear();
			// stop adding new elements
			elementTimer.cancel();
			elementTimer.purge();
		}
	}
	
	private void stop() {
		if(running == true) {
			running = false;
			Display.destroy();
			elementTimer.cancel();
			elementTimer.purge();
		}
	}
	
	
	private void resetSpeedCounter() {
		speedCounter = (int)(((double) 1 / (double) (level + 5)) * (double) 70);
	}
	
	
	private void initGame() {
		gamefield = new Map1();
	}
	
	
	public void runApplication(boolean fullscreen, AbstractField map) {
		startOpenGL(fullscreen);
		initGame();
		gameLoop();
	}
	
	
	
}
