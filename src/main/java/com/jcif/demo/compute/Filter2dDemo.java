package com.jcif.demo.compute;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLBufferFactory.GL_ACCESS;
import com.jcif.opengl.GLBufferFactory.GL_TYPE;
import com.jcif.opengl.GLBufferFactory.GL_USAGE;
import com.jcif.opengl.GLSharedContextInstance;
import com.jcif.opengl.glcompute.filter.GlComputeFilter2d;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class Filter2dDemo {

	public static void main(String[] args) {
		GLContext sharedContext = GLSharedContextInstance.getInstance().getGLSharedContext();
		sharedContext.makeCurrent();

		GL4 gl = sharedContext.getGL().getGL4();

		GlComputeFilter2d computeHandler = new GlComputeFilter2d(gl);

		// creqte data
		int size = 20;
		float min = 0;
		float max = 0.5f;

		ByteBuffer bbdataA = createNewData(size);

		ByteBuffer bbdataB = createNewData(size);

		ByteBuffer bb = createIndices(size);

		GLBuffer gpuSrc_valuesB = GLBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.STATIC_DRAW);
		gpuSrc_valuesB.bind(gl);
		gpuSrc_valuesB.allocate(gl, bbdataB, bbdataB.capacity());
		gpuSrc_valuesB.release(gl);

		GLBuffer gpuSrc_valuesA = GLBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.STATIC_DRAW);
		gpuSrc_valuesA.bind(gl);
		gpuSrc_valuesA.allocate(gl, bbdataA, bbdataA.capacity());
		gpuSrc_valuesA.release(gl);

		GLBuffer gpuSrc_indices = GLBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.STATIC_DRAW);
		gpuSrc_indices.bind(gl);
		gpuSrc_indices.allocate(gl, bb, bb.capacity());
		gpuSrc_indices.release(gl);

		long time = System.currentTimeMillis();

		GLBuffer filter = computeHandler.filter2d(gl, gpuSrc_indices, size, gpuSrc_valuesA, min, max, gpuSrc_valuesB,
				min, max);
		filter.bind(gl);
		IntBuffer filterData = (filter.mapRange(gl, GL_ACCESS.READ_ONLY, 0, size * Integer.BYTES)).asIntBuffer();
		filter.unmap(gl);
		filter.release(gl);

		int sum = 0;
		for (int i = 0; i < filterData.capacity(); i++) {
			int value = filterData.get();
			System.err.println(" i :" + value);

		}

		time = System.currentTimeMillis() - time;
		System.err.println("elpased time in ms   :" + time);

	}

	public static ByteBuffer createNewData(int nb) {

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

	public static ByteBuffer createIndices(int nb) {

		ByteBuffer bb = GLBufferFactory.allocate(nb * Integer.BYTES);
		IntBuffer floatbuffervalues = bb.asIntBuffer();

		for (int i = 0; i < nb; i++) {
			// ID
			floatbuffervalues.put(i, i + 1);
			// X

		}

		return bb;
	}

}
