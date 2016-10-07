package com.jcif.opengl.glpainter.scatterchart;

import java.nio.ByteBuffer;

public class ScatterChart {

	protected ByteBuffer XYs;
	protected ByteBuffer Colors;
	protected int count;

	public ByteBuffer getXYs() {
		return XYs;
	}

	public void setXYs(ByteBuffer xYs) {
		XYs = xYs;
	}

	public ByteBuffer getColors() {
		return Colors;
	}

	public void setColors(ByteBuffer colors) {
		Colors = colors;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
