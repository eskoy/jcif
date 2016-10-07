package com.jcif.opengl.glpainter.grid;

import java.awt.Color;

public class Grid {

	protected Color color;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	protected int step;

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
}
