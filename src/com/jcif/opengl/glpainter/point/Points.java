package com.jcif.opengl.glpainter.point;

import java.nio.ByteBuffer;

public class Points {

	protected ByteBuffer XYs;
	protected ByteBuffer Colors;
	protected int count = 0;
	protected float pointSize = 3f;

	public float getPointSize() {
		return pointSize;
	}

	public void setPointSize(float pointSize) {
		this.pointSize = pointSize;
	}

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
