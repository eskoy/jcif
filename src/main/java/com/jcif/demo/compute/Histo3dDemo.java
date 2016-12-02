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
import com.jcif.opengl.glcompute.histo.GlComputeHisto3d;
import com.jogamp.nativewindow.AbstractGraphicsDevice;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;

public class Histo3dDemo {

	public static void main(String[] args) {
		final GLDrawableFactory factory = GLDrawableFactory.getFactory(GLProfile.get(GLProfile.GL4bc));
		final AbstractGraphicsDevice device = GLSharedContextInstance.getInstance().getGLSharedAutoDrawable()
				.getNativeSurface().getGraphicsConfiguration().getScreen().getDevice();
		GLCapabilitiesImmutable caps = GLSharedContextInstance.getInstance().getGLCapabilities();

		final GLDrawable drawable = factory.createDummyDrawable(device, true, caps, null);
		drawable.setRealized(true);
		GLContext sharedContext = drawable
				.createContext(GLSharedContextInstance.getInstance().getGLSharedAutoDrawable().getContext());

		sharedContext.makeCurrent();
		GL4 gl = sharedContext.getGL().getGL4();

		GlComputeHisto3d computeHandler = new GlComputeHisto3d(gl);

		// creqte data
		int size = 6000000;

		ByteBuffer bbdataA = createNewData(size);
		ByteBuffer bbdataB = createNewData(size);
		ByteBuffer bbdataC = createNewData(size);
		ByteBuffer bb = createIndices(size);

		GLBuffer gpuSrc_valuesC = GLBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.STATIC_DRAW);
		gpuSrc_valuesC.bind(gl);
		gpuSrc_valuesC.allocate(gl, bbdataC, bbdataC.capacity());
		gpuSrc_valuesC.release(gl);

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

		GLBuffer[] buffer = { gpuSrc_valuesA, gpuSrc_valuesB, gpuSrc_valuesB };

		float[] min = { 0, 0, 0 };
		float[] max = { 1, 1, 1 };
		int[] nbins = { 10, 10, 10 };

		GLBuffer histo = computeHandler.histogram3D(gl, gpuSrc_indices, size, buffer, min, max, nbins);
		histo.bind(gl);
		IntBuffer histoData = (histo.mapRange(gl, GL_ACCESS.READ_ONLY, 0,
				nbins[0] * nbins[1] * nbins[2] * Integer.BYTES)).asIntBuffer();
		histo.unmap(gl);
		histo.release(gl);

		int sum = 0;
		for (int i = 0; i < histoData.capacity(); i++) {
			int bar = histoData.get();
			sum += bar;
			System.err.println(" " + i + " " + bar);
		}
		System.err.println(" total " + sum);

		time = System.currentTimeMillis() - time;
		System.err.println("elpased time in ms   :" + time);

		while (true) {
		}

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
