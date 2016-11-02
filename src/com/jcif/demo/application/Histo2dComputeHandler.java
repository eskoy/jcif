package com.jcif.demo.application;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlBufferFactory.GL_ACCESS;
import com.jcif.opengl.glcompute.histo.Histo2d;
import com.jogamp.opengl.GL4;

public class Histo2dComputeHandler {

	private Histo2d compute;

	public Histo2dComputeHandler(GL4 gl) {

		compute = new Histo2d(gl);

	}

	public IntBuffer compute(GL4 gl, GlBuffer gpuSrc_valuesA, GlBuffer gpuSrc_valuesB, GlBuffer gpuSrc_indices,
			int size, float min, float max, int nbins) {
		GlBuffer histo = compute.histogram2D(gl, gpuSrc_indices, size, gpuSrc_valuesA, min, max, nbins, gpuSrc_valuesB,
				min, max, nbins);
		histo.bind(gl);
		IntBuffer histoData = (histo.mapRange(gl, GL_ACCESS.READ_ONLY, 0, nbins * nbins * Integer.BYTES)).asIntBuffer();
		return histoData;
	}

	public ByteBuffer[] createPointWithHisto(IntBuffer histo, int nbins) {
		ByteBuffer bb = GlBufferFactory.allocate(nbins * nbins * Float.BYTES * 2);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();
		ByteBuffer bbColor = GlBufferFactory.allocate(nbins * nbins * Float.BYTES * 4);
		FloatBuffer floatColorbuffervalues = bbColor.asFloatBuffer();

		Random random = new Random();

		float factor = nbins - 1;

		for (int i = 0; i < nbins; i++) {
			for (int j = 0; j < nbins; j++) {
				int index = i * nbins + j;
				if (histo.get(index) != 0) {

					int offset = (i * nbins + j) * 4;
					int offset2 = (i * nbins + j) * 2;
					float x = ((i) / factor * 2f) - 1f;
					float y = ((j) / factor * 2f) - 1f;

					floatbuffervalues.put(offset2 + 0, x);
					floatbuffervalues.put(offset2 + 1, y);

					float r = 0.25f;
					float g = random.nextFloat() * 0.25f;
					float b = random.nextFloat() * 0.25f;

					floatColorbuffervalues.put(offset + 0, r);
					floatColorbuffervalues.put(offset + 1, g);
					floatColorbuffervalues.put(offset + 2, b);
					floatColorbuffervalues.put(offset + 3, 1f);
				}
			}
		}
		return new ByteBuffer[] { bb, bbColor };
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
			floatbuffervalues.put(i, i);
			// X

		}

		return bb;
	}

	public static ByteBuffer updateIndices(int nb, int value) {

		ByteBuffer bb = GlBufferFactory.allocate(nb * Integer.BYTES);
		IntBuffer floatbuffervalues = bb.asIntBuffer();

		Random rand = new Random();

		int test = rand.nextInt(nb);

		for (int i = 0; i < nb; i++) {
			// ID

			floatbuffervalues.put(i, value);
			// X

		}

		return bb;
	}

}
