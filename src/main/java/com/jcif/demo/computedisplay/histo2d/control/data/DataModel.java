package com.jcif.demo.computedisplay.histo2d.control.data;

import com.jcif.opengl.GLBuffer;

public class DataModel {

	private int dataNumber;

	private GLBuffer gpuValueX;

	private GLBuffer gpuValueindices;

	private GLBuffer gpuValueY;

	public int getDataNumber() {
		return dataNumber;
	}

	public GLBuffer getGpuValueY() {
		return gpuValueY;
	}

	public GLBuffer getGpuValueX() {
		return gpuValueX;
	}

	public GLBuffer getGpuValueindices() {
		return gpuValueindices;
	}

	public void setDataNumber(int dataNumber) {
		this.dataNumber = dataNumber;
	}

	public void setGpuValueX(GLBuffer gpuValueX) {
		this.gpuValueX = gpuValueX;
	}

	public void setGpuValueindices(GLBuffer gpuValueindices) {
		this.gpuValueindices = gpuValueindices;
	}

	public void setGpuValueY(GLBuffer gpuValueY) {
		this.gpuValueY = gpuValueY;
	}

	public DataModel() {
	}

}
