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
package de.vistahr.map;


import org.lwjgl.util.Point;

import de.vistahr.jsnake.element.IElement;
import de.vistahr.jsnake.element.INonHitableElement;
import de.vistahr.jsnake.element.SnakeElement;

public abstract class AbstractField implements Cloneable
{
	
	private IElement[][] gameField;
	
	private int height;
	private int width;
	
	public AbstractField(int w, int h) {
		width  = w;
		height = h;
		setGameField(new IElement[50][50]);
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	private void setGameField(IElement[][] gameField) {
		this.gameField = gameField;
	}


	public IElement[][] getGameField() {
		return gameField;
	}
	
	public synchronized IElement getElement(int x, int y) {
		return getGameField()[x][y];
	}
	
	public synchronized void setElement(int x, int y, IElement elm) {
		gameField[x][y] = elm;
	}
	
	public void removeElement(int x, int y) {
		setElement(x, y, null);
	}
	
	public AbstractField clone() {
		try {
			AbstractField clonedField = (AbstractField)super.clone();
			return clonedField;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Set an element to a random position on the field
	 * @param map
	 * @param element
	 * @return
	 */
	public boolean setElementToRandomPosition(IElement element) {
		// only getHeight() * getWidth() fields are available
		for(int i = 0; i <= (getHeight() * getWidth()); i++) {
			// generate random position and check the element
			int x = (int)(Math.random() * getWidth());
			int y = (int)(Math.random() * getHeight());
			
			if(getElement(x, y) == null) {
				if(element instanceof SnakeElement) {
					((SnakeElement) element).setPosition(x, y);
				}
				setElement(x, y, element);
				return true;
			}
		}
		return false;
	}
	
	
	public Point getFreeRandomPosition() {
		// only getHeight() * getWidth() fields are available
		for(int i = 0; i <= (getHeight() * getWidth()); i++) {
			// generate random position and check the element
			int x = (int)(Math.random() * getWidth());
			int y = (int)(Math.random() * getHeight());
			
			if(getElement(x, y) == null) {
				return new Point(x, y);
			}
		}
		return null;
	}
	
	
	/**
	 * Remove all hitable elements
	 */
	public void clear() {
		// iterate through the hole gamefield
		for (int x = 0; x <= getWidth()-1; x++) {
			for (int y = 0; y <= getHeight()-1; y++) {
				if(!(getElement(x, y) instanceof INonHitableElement)) {
					removeElement(x, y);
				}
			}
		}		
	}
	
}
