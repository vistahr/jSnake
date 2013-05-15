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
package de.vistahr.opengl.util;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class BoxUtil
{

	public static void draw(float boxDimension) {

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2f(0f, boxDimension); // TopLeft
			GL11.glVertex2f(boxDimension, boxDimension); // TopRight
			GL11.glVertex2f(boxDimension, 0f); // BottomRight
			GL11.glVertex2f(0f, 0f); // BottomLeft
		}
		GL11.glEnd();

	}

	public static void draw(float boxWidth, float boxHeight, Texture texture) {
		if(texture != null) {
			GL11.glColor3f(1f, 1f, 1f); // reset color
			GL11.glEnable(GL11.GL_TEXTURE_2D);   
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			texture.bind();
			GL11.glBegin(GL11.GL_QUADS);
			{	
				GL11.glTexCoord2f(0,1);
				GL11.glVertex2f(0f, boxHeight); // TopLeft
				GL11.glTexCoord2f(1,1);
				GL11.glVertex2f(boxWidth, boxHeight); // TopRight
				GL11.glTexCoord2f(1,0);
				GL11.glVertex2f(boxWidth, 0f); // BottomRight
				GL11.glTexCoord2f(0,0);
				GL11.glVertex2f(0f, 0f); // BottomLeft
			}
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
		} else {
			GL11.glBegin(GL11.GL_QUADS);
			{	
				GL11.glVertex2f(0f, boxHeight); // TopLeft
				GL11.glVertex2f(boxWidth, boxHeight); // TopRight
				GL11.glVertex2f(boxWidth, 0f); // BottomRight
				GL11.glVertex2f(0f, 0f); // BottomLeft
			}
			GL11.glEnd();
		}
	}

}
