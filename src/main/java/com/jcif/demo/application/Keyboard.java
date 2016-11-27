package com.jcif.demo.application;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class Keyboard implements KeyListener {

	private boolean[] pressed = new boolean[127];
	private boolean[] released = new boolean[127];

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() > 0 && keyEvent.getKeyCode() < pressed.length) {
			pressed[keyEvent.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() > 0 && keyEvent.getKeyCode() < pressed.length) {
			pressed[keyEvent.getKeyCode()] = false;
			released[keyEvent.getKeyCode()] = true;
		}
	}

	public boolean isPressed(short keyCode) {
		return keyCode < pressed.length && pressed[keyCode];
	}

	public boolean isReleased(short keyCode) {
		boolean result = keyCode < pressed.length && released[keyCode];

		released[keyCode] = false;

		return result;
	}
}
