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

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

import de.vistahr.jsnake.util.TextureManagerUtil;
import de.vistahr.opengl.util.BoxUtil;

public class SnakeElement implements IElement, INonHitableElement
{
	
	private Point point;

	
	public SnakeElement(int x, int y) {
		point = new Point(x, y);
	}
	
	public int getPositionX() {
		return point.getX();
	}

	public int getPositionY() {
		return point.getY();
	}

	public void setPosition(int positionX, int positionY) {
		point.setX(positionX);
		point.setY(positionY);
	}
		
	

	@Override
	public void draw(float positionX, float positionY, float width, float height) {
		
		// set worldmatrix to stack
		GL11.glPushMatrix();
		// move to position
	 	GL11.glTranslatef(positionX, positionY, 0f);
	 	// bind texture to quad
	 	Texture wallTexture = TextureManagerUtil.loadJPGTexture("res/texture/snake.jpg");
	 	// draw a single cube
	    BoxUtil.draw(width, height, wallTexture);
	    
	    // draw tongue
	    //Color color = new Color(Color.RED);
	    //GL11.glColor3ub(color.getRedByte(), color.getGreenByte(), color.getBlueByte());
	    //GL11.glTranslatef(15, 5, 0f);
	    //LineUtil.drawHorizontal(5, 2);
	    
		// restore to world matrix from stack
	    GL11.glPopMatrix();	
	}


}
