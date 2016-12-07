package com.jcif.demo.computedisplay.histo2d.model;

import java.nio.ByteBuffer;

public class ViewModel {

	protected int histoXSize = 0;

	protected int histoYSize = 0;

	public int getHistoYSize() {
		return histoYSize;
	}

	public void setHistoYSize(int histoYSize) {
		this.histoYSize = histoYSize;
	}

	protected ByteBuffer[] histoBuffer = new ByteBuffer[2];

	public int getHistoXSize() {
		return histoXSize;
	}

	public void setHistoXSize(int histoSize) {
		this.histoXSize = histoSize;
	}

	public ByteBuffer[] getHistoBuffer() {
		return histoBuffer;
	}

	public void setHistoBuffer(ByteBuffer[] histo) {
		this.histoBuffer = histo;
	}

}
