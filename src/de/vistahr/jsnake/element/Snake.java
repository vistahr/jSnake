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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.util.Point;

public class Snake
{

	private List<SnakeElement> snakeElements;
	

	public Snake(int x, int y) {
		snakeElements = Collections.synchronizedList(new ArrayList<SnakeElement>());
		addNewElement(x, y);
	}
	
	public Snake(Point p) {
		this(p.getX(), p.getY());
	}

	
	public synchronized List<SnakeElement> getSnakeElements() {
		return snakeElements;
	}
	
	
	private synchronized void addNewElement(int x, int y) {
		snakeElements.add(new SnakeElement(x, y));
	}
	
	
	public synchronized void appendElement() {
		addNewElement(-1, -1);
	}
	
	
	public synchronized void removeElement() {
		// headelement cannot removed
		if(snakeElements.size() > 1) {
			snakeElements.remove(snakeElements.size()-1);
		}
	}
	
}
