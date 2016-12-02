package com.jcif.opengl.glpainter.histo;

import java.nio.ByteBuffer;

public class Histo3d {

	protected ByteBuffer XYZs;
	protected ByteBuffer Colors;
	protected int count = 0;
	protected float pointSize = 3f;

	public float getPointSize() {
		return pointSize;
	}

	public void setPointSize(float pointSize) {
		this.pointSize = pointSize;
	}

	public ByteBuffer getXYZs() {
		return XYZs;
	}

	public void setXYZs(ByteBuffer xYZs) {
		XYZs = xYZs;
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
