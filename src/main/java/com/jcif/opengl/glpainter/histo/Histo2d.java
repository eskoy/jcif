package com.jcif.opengl.glpainter.histo;

import java.nio.ByteBuffer;

public class Histo2d {

	protected ByteBuffer XYs;
	protected ByteBuffer Counts;

	public ByteBuffer getCounts() {
		return Counts;
	}

	public void setCounts(ByteBuffer counts) {
		Counts = counts;
	}

	protected ByteBuffer Colors;

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

}
