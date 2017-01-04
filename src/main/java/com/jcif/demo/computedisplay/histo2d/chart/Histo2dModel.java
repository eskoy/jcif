package com.jcif.demo.computedisplay.histo2d.chart;

import java.nio.ByteBuffer;

public class Histo2dModel {

	protected float pointSize = 3f;

	public float getPointSize() {
		return pointSize;
	}

	public void setPointSize(float pointSize) {
		this.pointSize = pointSize;
	}

	protected int histoXSize = 0;

	protected int histoYSize = 0;

	public int getHistoYSize() {
		return histoYSize;
	}

	public void setHistoYSize(int hy) {
		this.histoYSize = hy;
	}

	protected ByteBuffer[] histoBuffer = new ByteBuffer[2];

	public int getHistoXSize() {
		return histoXSize;
	}

	public void setHistoXSize(int hx) {
		this.histoXSize = hx;
	}

	public ByteBuffer[] getHistoBuffer() {
		return histoBuffer;
	}

	public void setHistoBuffer(ByteBuffer[] histo) {
		this.histoBuffer = histo;
	}

}
