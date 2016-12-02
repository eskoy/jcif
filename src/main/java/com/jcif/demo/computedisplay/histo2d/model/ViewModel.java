package com.jcif.demo.computedisplay.swing.model;

import java.nio.ByteBuffer;

public class ViewModel {

	protected int histoSize = 0;

	protected ByteBuffer[] scatterchartdata = new ByteBuffer[2];

	public int getHistoSize() {
		return histoSize;
	}

	public void setHistoSize(int histoSize) {
		this.histoSize = histoSize;
	}

	public ByteBuffer[] getScatterchartdata() {
		return scatterchartdata;
	}

	public void setScatterchartdata(ByteBuffer[] scatterchartdata) {
		this.scatterchartdata = scatterchartdata;
	}

}
