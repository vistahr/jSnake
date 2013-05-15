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
package de.vistahr.jsnake.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;

import de.vistahr.map.AbstractField;
import de.vistahr.opengl.util.BoxUtil;
import de.vistahr.opengl.util.LineUtil;

public class RenderManagerUtil
{
	
	private static int bgDisplayList = 0;
	private static HashMap<Integer, UnicodeFont> fontCache = new HashMap<Integer, UnicodeFont>();
	
	
	public static void enableOrthoView(int width, int height) {
		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glViewport(0,0,width,height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix(); 
	    GL11.glLoadIdentity();
		
	    GL11.glOrtho(0, width, height, 0, 1, -1);
	
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
	    GL11.glLoadIdentity();
	}

	
	public static void disableOrthoView() {
		GL11.glMatrixMode( GL11.GL_PROJECTION ); 
	    GL11.glPopMatrix();  
	    GL11.glMatrixMode( GL11.GL_MODELVIEW );  
	    GL11.glPopMatrix(); 
	}
	
	
	public static void drawBackground(int width, int height) {
		ArrayList<Texture> bgTextures = new ArrayList<Texture>();
		bgTextures.add(TextureManagerUtil.loadJPGTexture("res/texture/gras2_1.jpg"));
		bgTextures.add(TextureManagerUtil.loadJPGTexture("res/texture/gras2_2.jpg"));
		bgTextures.add(TextureManagerUtil.loadJPGTexture("res/texture/gras2_3.jpg"));
		
		int texSize = 10;
		
		// generate display list to generate and store a static random background
		if(bgDisplayList == 0) {
			bgDisplayList = GL11.glGenLists(1);
			
			GL11.glNewList(bgDisplayList, GL11.GL_COMPILE);
			GL11.glPushMatrix(); // store	
			for (int x = 0; x <= (width/texSize)-1; x++) {
				GL11.glPushMatrix(); 
				for (int y = 0; y <= (height/texSize)-1; y++) {
					BoxUtil.draw(texSize, texSize, bgTextures.get((int)(Math.random()*bgTextures.size())));
					GL11.glTranslatef(0, texSize, 0f);
				}
				GL11.glPopMatrix(); // restore
				GL11.glTranslatef(texSize, 0, 0f);
			}
		    GL11.glPopMatrix(); // restore
		    GL11.glEndList();
		    
		} else {
			// call list
			 GL11.glCallList(bgDisplayList);
		}
		
	    
	}
	

	public static void drawFieldGrid(AbstractField map) {
		// white line
		GL11.glColor3f(0.5f, 0.5f, 0.5f);
		
		// set worldmatrix to stack
		GL11.glPushMatrix();
		for(int i = 1; i<= map.getHeight(); i++) {
			// from bottom to top on the x-axis
			LineUtil.drawHorizontal(Display.getWidth(), 1);
			GL11.glTranslatef(0.0f, (Display.getHeight()/map.getHeight()), 0f);
		}
		// restore to world matrix from stack
	    GL11.glPopMatrix();
	    
	    // set worldmatrix to stack
  		GL11.glPushMatrix();
  		// rotate to vertical
  		GL11.glRotatef(90, 0f, 0f, 1f);
  		for(int i = 1; i<= map.getWidth(); i++) {
  			// from bottom to top on the x-axis
  			LineUtil.drawHorizontal(Display.getHeight(), 1);
  			GL11.glTranslatef(0f, -(Display.getWidth()/map.getWidth()), 0f);
  		}
  		// restore to world matrix from stack
  	    GL11.glPopMatrix();
	}
	
	
	public static void drawElements(AbstractField map) {
		// iterate through the hole gamefield
		for (int x = 0; x <= map.getWidth()-1; x++) {
			for (int y = 0; y <= map.getHeight()-1; y++) {
				if(map.getElement(x, y) != null) {
					map.getElement(x, y).draw(x * (Display.getWidth()/map.getWidth()), y * (Display.getHeight()/map.getHeight()), 
						 (Display.getWidth()/map.getWidth()), (Display.getHeight()/map.getHeight()));
				}
			}
		}
	}
	
	public static UnicodeFont getFont(int size) {
		
		if(fontCache.containsKey(size))
			return fontCache.get(size);
		
		String fontPath = "res/font/ALPNSCND.TTF";
		UnicodeFont font = null;
		try {
			font = new UnicodeFont(fontPath, size, true, false);
			font.getEffects().add(new ColorEffect(java.awt.Color.white));
			font.addAsciiGlyphs();
			font.loadGlyphs();
			
			fontCache.put(size, font);
			
		} catch (SlickException e) {
			e.printStackTrace();	
		}
		
		return font;
	}
	
	
	public static void drawTitle(String title) {
		getFont(20).drawString((Display.getWidth() / 2) - 100, (Display.getHeight() / 2) - 100, title, Color.magenta);
		// slick workourund
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0, 0, 0, 0);
	}
	
	
	public static void drawString(int x, int y, String str, Color col) {
		getFont(10).drawString(x, y, str, col);
		// slick workourund
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0, 0, 0, 0);
	}
	
	

	
}
