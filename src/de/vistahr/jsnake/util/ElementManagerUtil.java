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

import de.vistahr.jsnake.annotation.DirectionEnum;
import de.vistahr.jsnake.element.AbstractFoodElement;
import de.vistahr.jsnake.element.INonHitableElement;
import de.vistahr.jsnake.element.Snake;
import de.vistahr.jsnake.element.SnakeElement;
import de.vistahr.jsnake.exception.SnakeHitElementException;
import de.vistahr.map.AbstractField;

public class ElementManagerUtil
{
	
	
	/**
	 * Move the snake to the given direction.
	 * All conditional elements move forward.
	 * At last, set all snakelements on the gamefield
	 * @param snake
	 * @param map
	 * @param direction
	 * @throws SnakeHitElementException
	 */
	public static int moveSnakeAndSetOnGamefield(Snake snake, AbstractField map, DirectionEnum direction) throws SnakeHitElementException  {
		// first clear - later set new positions
		removeSnakeElementsFromGameField(map);
		
		//int i = 0;
		//for(SnakeElement snakeEl: snake.getSnakeElements()) {
		// reverse loop
		SnakeElement snakeEl;
		int i;
		for(i = (snake.getSnakeElements().size()-1); i>=0; i--) {
			
			snakeEl = snake.getSnakeElements().get(i);
			
			int x = snakeEl.getPositionX();
			int y = snakeEl.getPositionY();
			
			// headelement
			if(i == 0) {
				// move to new direction
				if(direction == DirectionEnum.LEFT) {
					int newX = x - 1;
					if(map.getElement(newX, y) instanceof INonHitableElement)
						throw new SnakeHitElementException();
					
					snakeEl.setPosition(newX, y);
				}
				
				if(direction == DirectionEnum.RIGHT) {
					int newX = x + 1;
					if(map.getElement(newX, y) instanceof INonHitableElement)
						throw new SnakeHitElementException();
					
					snakeEl.setPosition(newX, y);
				}
				
				if(direction == DirectionEnum.UP) {
					int newY = y - 1;
					if(map.getElement(x, newY) instanceof INonHitableElement)
						throw new SnakeHitElementException();
					
					snakeEl.setPosition(x, newY);
				}
				
				if(direction == DirectionEnum.DOWN) {
					int newY = y + 1;
					if(map.getElement(x, newY) instanceof INonHitableElement)
						throw new SnakeHitElementException();
					
					snakeEl.setPosition(x, newY);
				}
				
				
			} else {
				// tailelements
				if(direction != DirectionEnum.NONE) {
					SnakeElement beforeSE = snake.getSnakeElements().get(i-1); // get before-element
					x = beforeSE.getPositionX(); 
					y = beforeSE.getPositionY(); 
				}
				snakeEl.setPosition(x, y);
			}
			
			
		} 
		// last step, set on gamefield
		return setSnakeOnGamefield(snake, map);
	}
	
	
	/**
	 * Position all snakeelements on the gamefiled
	 * @param snake
	 * @param map
	 */
	private static int setSnakeOnGamefield(Snake snake, AbstractField map) throws SnakeHitElementException {
		int appendElementCount = 0;
		int pointsOwned = 0;
		
		for(SnakeElement el: snake.getSnakeElements()) {
			if(el.getPositionX() >= 0 &&  el.getPositionY() >= 0) {
				if(map.getElement(el.getPositionX(), el.getPositionY()) instanceof INonHitableElement)
					throw new SnakeHitElementException();
				
				if(map.getElement(el.getPositionX(), el.getPositionY()) instanceof AbstractFoodElement) {
					AbstractFoodElement foodElm = (AbstractFoodElement) map.getElement(el.getPositionX(), el.getPositionY());
					if(foodElm.isValid()) {
						pointsOwned += foodElm.getPoints();
						appendElementCount++; // increment and add later - cause > concurrentmodification	
					}
						
				}
					
				
				map.setElement(el.getPositionX(), el.getPositionY(), el);
			}
		}
		
		// add new snake elements
		for(int i=0; i<appendElementCount; i++ ) {
			snake.appendElement(); 
		}
		
		return pointsOwned;
	}
	
	
	/**
	 * Remove snakeelements
	 * @param map
	 */
	private static void removeSnakeElementsFromGameField(AbstractField map) {
		for (int x = 0; x <= map.getWidth()-1; x++) {
			for (int y = 0; y <= map.getHeight()-1; y++) {
				if(map.getElement(x, y) instanceof SnakeElement)
					map.removeElement(x, y);
			}
		}
			
	}	
	
}
