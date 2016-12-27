package com.jcif.demo.computedisplay.histo2d.data.viewdata;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.util.DataUtilities;
import com.jcif.opengl.util.DataUtilities.DATA_TYPE;
import com.jogamp.opengl.GL4;

public class BusinessModel {

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

	public BusinessModel(int size, GL4 gl) {
		this.init(size, gl);
	}

	protected void init(int size, GL4 gl) {
		dataNumber = size;
		gpuValueX = GLBufferFactory.hostoGpuData(DataUtilities.createNewData1d(dataNumber, DATA_TYPE.LINEAR), gl);
		gpuValueY = GLBufferFactory.hostoGpuData(DataUtilities.createNewData1d(dataNumber, DATA_TYPE.COSINUS), gl);
		gpuValueindices = GLBufferFactory.hostoGpuData(this.createIndices(size), gl);

	}

	public ByteBuffer createIndices(int nb) {

		ByteBuffer bb = GLBufferFactory.allocate(nb * Integer.BYTES);
		IntBuffer floatbuffervalues = bb.asIntBuffer();

		for (int i = 0; i < nb; i++) {
			// ID
			floatbuffervalues.put(i, i);
			// X

		}

		return bb;
	}

}
