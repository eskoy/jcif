package com.jcif.demo.computing;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlSharedContextInstance;
import com.jcif.opengl.GlBufferFactory.ACCESS;
import com.jcif.opengl.GlBufferFactory.TYPE;
import com.jcif.opengl.GlBufferFactory.USAGE;
import com.jcif.opengl.computeshader.histo.Histo1d;
import com.jogamp.nativewindow.AbstractGraphicsDevice;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;

public class Histo1dDemo {

	public static void main(String[] args) {
		final GLDrawableFactory factory = GLDrawableFactory.getFactory(GLProfile.get(GLProfile.GL4bc));
		final AbstractGraphicsDevice device = GlSharedContextInstance.getInstance().getGLSharedAutoDrawable()
				.getNativeSurface().getGraphicsConfiguration().getScreen().getDevice();
		GLCapabilitiesImmutable caps = GlSharedContextInstance.getInstance().getGLCapabilities();
		final GLDrawable drawable = factory.createDummyDrawable(device, true, caps, null);
		drawable.setRealized(true);
		GLContext sharedContext = drawable
				.createContext(GlSharedContextInstance.getInstance().getGLSharedAutoDrawable().getContext());
		sharedContext.makeCurrent();
		GL4 gl = sharedContext.getGL().getGL4();

		Histo1d computeHandler = new Histo1d(gl);

		// creqte data
		int size = 6000000;
		float min = 0;
		float max = 1;
		int nbins = 10;
		ByteBuffer bbdata = createNewData(size);
		ByteBuffer bb = createIndices(size);

		GlBuffer gpuSrc_values = GlBufferFactory.newGLBuffer(gl, TYPE.ARRAY_BUFFER, USAGE.STATIC_DRAW);

		gpuSrc_values.bind(gl);
		gpuSrc_values.allocate(gl, bbdata, bbdata.capacity());
		gpuSrc_values.release(gl);

		GlBuffer gpuSrc_indices = GlBufferFactory.newGLBuffer(gl, TYPE.ARRAY_BUFFER, USAGE.STATIC_DRAW);

		gpuSrc_indices.bind(gl);
		gpuSrc_indices.allocate(gl, bb, bb.capacity());
		gpuSrc_indices.release(gl);

		long time = System.currentTimeMillis();

		GlBuffer histo = computeHandler.histogram1D(gl, gpuSrc_indices, size, gpuSrc_values, min, max, nbins);
		histo.bind(gl);
		IntBuffer histoData = (histo.mapRange(gl, ACCESS.READ_ONLY, 0, 10 * Integer.BYTES)).asIntBuffer();
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
		System.err.println("elpased time in gpu  ms   :" + time);

		time = System.currentTimeMillis();
		int[] histoarray = calcHistogram(bbdata.asFloatBuffer(), bb.asIntBuffer(), min, max, nbins);
		sum = 0;
		for (int i = 0; i < histoarray.length; i++) {
			int bar = histoarray[i];
			sum += bar;
			System.err.println(" " + i + " " + bar);
		}
		System.err.println(" total " + sum);

		time = System.currentTimeMillis() - time;
		System.err.println("elpased time cpu in ms :" + time);
	}

	public static ByteBuffer createNewData(int nb) {

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

	public static ByteBuffer createIndices(int nb) {

		ByteBuffer bb = GlBufferFactory.allocate(nb * Integer.BYTES);
		IntBuffer floatbuffervalues = bb.asIntBuffer();

		for (int i = 0; i < nb; i++) {
			// ID
			floatbuffervalues.put(i, i + 1);
			// X

		}

		return bb;
	}

	public static int[] calcHistogram(FloatBuffer data, IntBuffer index, float min, float max, int numBins) {
		final int[] result = new int[numBins];
		final double binSize = (max - min) / numBins;

		for (int i = 0; i < data.capacity(); i++) {
			if (index.get(i) != 0) {
				int bin = (int) ((data.get(i) - min) / binSize);
				if (bin < 0) {
					/* this data is smaller than min */ } else if (bin >= numBins) {
					/* this data point is bigger than max */ } else {
					result[bin] += 1;
				}
			}
		}
		return result;
	}

}
