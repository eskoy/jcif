package com.jcif.demo.computedisplay.histo2d.data.viewdata;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jogamp.opengl.GL4;

public class BusinessModel {

	private int dataNumber;

	private GLBuffer gpuValueB;

	private GLBuffer gpuValueindices;

	private GLBuffer gpuValueA;

	public int getDataNumber() {
		return dataNumber;
	}

	public GLBuffer getGpuValueA() {
		return gpuValueA;
	}

	public GLBuffer getGpuValueB() {
		return gpuValueB;
	}

	public GLBuffer getGpuValueindices() {
		return gpuValueindices;
	}

	public BusinessModel(int size, GL4 gl) {
		this.init(size, gl);
	}

	protected void init(int size, GL4 gl) {
		dataNumber = size;
		gpuValueA = GLBufferFactory.hostoGpuData(this.createNewData(size), gl);
		gpuValueB = GLBufferFactory.hostoGpuData(this.createNewData(size), gl);
		gpuValueindices = GLBufferFactory.hostoGpuData(this.createIndices(size), gl);

	}

	public ByteBuffer createNewData(int nb) {

		ByteBuffer bb = GLBufferFactory.allocate(nb * Float.BYTES);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();

		Random rnd = new Random();
		for (int i = 0; i < nb; i++) {
			// ID
			floatbuffervalues.put(i, rnd.nextFloat());
			// X

		}

		return bb;
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
