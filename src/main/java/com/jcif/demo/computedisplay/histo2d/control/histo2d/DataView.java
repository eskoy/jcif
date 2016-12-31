package com.jcif.demo.computedisplay.histo2d.control.histo2d;

import java.nio.ByteBuffer;

public class DataView {

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
