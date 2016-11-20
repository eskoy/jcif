package com.jcif.demo.computedisplay.swing.model;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jogamp.opengl.GL4;

public class BusinessModel {

	private int dataNumber;

	private GlBuffer gpuValueB;

	private GlBuffer gpuValueindices;

	private GlBuffer gpuValueA;

	public int getDataNumber() {
		return dataNumber;
	}

	public GlBuffer getGpuValueA() {
		return gpuValueA;
	}

	public GlBuffer getGpuValueB() {
		return gpuValueB;
	}

	public GlBuffer getGpuValueindices() {
		return gpuValueindices;
	}

	public BusinessModel(int size, GL4 gl) {
		this.init(size, gl);
	}

	protected void init(int size, GL4 gl) {
		dataNumber = size;
		gpuValueA = GlBufferFactory.hostoGpuData(this.createNewData(size), gl);
		gpuValueB = GlBufferFactory.hostoGpuData(this.createNewData(size), gl);
		gpuValueindices = GlBufferFactory.hostoGpuData(this.createIndices(size), gl);

	}

	public ByteBuffer createNewData(int nb) {

		ByteBuffer bb = GlBufferFactory.allocate(nb * Float.BYTES);
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

		ByteBuffer bb = GlBufferFactory.allocate(nb * Integer.BYTES);
		IntBuffer floatbuffervalues = bb.asIntBuffer();

		for (int i = 0; i < nb; i++) {
			// ID
			floatbuffervalues.put(i, i);
			// X

		}

		return bb;
	}

}
