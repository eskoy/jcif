package com.jcif.opengl.glpainter.polygon;

import java.awt.Color;
import java.nio.ByteBuffer;

public class Polygon {

	protected ByteBuffer verticeBuffer;

	protected Color color = Color.RED;

	protected float size = 2f;

	public Polygon() {

	}

	public ByteBuffer getPolygon() {
		return verticeBuffer;
	}

	public void setPolygon(ByteBuffer polygon) {
		this.verticeBuffer = polygon;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
