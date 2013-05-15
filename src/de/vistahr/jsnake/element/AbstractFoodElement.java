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
package de.vistahr.jsnake.element;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import de.vistahr.jsnake.util.TextureManagerUtil;
import de.vistahr.opengl.util.BoxUtil;

public abstract class AbstractFoodElement implements IElement
{

	private final int strength;
	private final int points;
	private int secondsToDestroy; // counter to zero
	
	
	public AbstractFoodElement(int strength, int points, int maxSecondsToDestroy) {
		this.strength = strength;
		this.points = points;
		this.secondsToDestroy = (int)(Math.random() * maxSecondsToDestroy);
		startDestroyTask();
	}
	
	
	public int getStrength() {
		return strength;
	}
	

	public int getPoints() {
		return points;
	}

	public boolean isValid() {
		if(secondsToDestroy == 0)
			return false;
			
		return true;
	}


	
	private void startDestroyTask() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// increment every second
				if(secondsToDestroy > 0) {
					secondsToDestroy--;
					
				} else {
					timer.cancel();
					timer.purge();
				}
			}
		}, 1000, 1000);
	}

	
	@Override
	public void draw(float positionX, float positionY, float width, float height) {
		if(isValid()) {
			// set worldmatrix to stack
			GL11.glPushMatrix();
			// move to position
		 	GL11.glTranslatef(positionX, positionY - 3, 0f);
		 	// bind texture to quad
		 	Texture foodTexture = TextureManagerUtil.loadGIFTexture("res/texture/food2.gif");
		 	// draw a single cube
		    BoxUtil.draw(40, 20, foodTexture);
			// restore to world matrix from stack
		    GL11.glPopMatrix();	
		}
	}	
	
}
