package com.jcif.demo.computedisplay.histo3d.model;

import java.nio.ByteBuffer;

public class ViewModel {

	protected int histoSize = 0;

	protected ByteBuffer[] histoBuffer = new ByteBuffer[2];

	public int getHistoSize() {
		return histoSize;
	}

	public void setHistoSize(int histoSize) {
		this.histoSize = histoSize;
	}

	public ByteBuffer[] getHistoBuffer() {
		return histoBuffer;
	}

	public void setHistoBuffer(ByteBuffer[] histo) {
		this.histoBuffer = histo;
	}

}
