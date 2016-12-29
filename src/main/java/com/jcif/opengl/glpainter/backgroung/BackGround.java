package com.jcif.opengl.glpainter.backgroung;

import java.awt.Color;

public class BackGround {

	protected int[] viewPort = { 0, 0, 100, 100 };

	protected Color color = Color.BLACK;

	public int[] getViewPort() {
		return viewPort;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setViewPort(int[] viewPort) {
		this.viewPort = viewPort;
	}

}
