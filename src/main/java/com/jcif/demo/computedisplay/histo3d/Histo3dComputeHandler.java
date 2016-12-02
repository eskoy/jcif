package com.jcif.demo.computedisplay.histo3d;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLBufferFactory.GL_ACCESS;
import com.jcif.opengl.glcompute.histo.GlComputeHisto3d;
import com.jogamp.opengl.GL4;

public class Histo3dComputeHandler {

	private GlComputeHisto3d compute;

	public Histo3dComputeHandler(GL4 gl) {

		compute = new GlComputeHisto3d(gl);

	}

	public IntBuffer compute(GL4 gl, GLBuffer gpuSrc_indices, int size, GLBuffer[] gpuSrc_values, float[] minValues,
			float[] maxValues, int[] binCounts) {

		GLBuffer histo = compute.histogram3D(gl, gpuSrc_indices, size, gpuSrc_values, minValues, maxValues, binCounts);
		histo.bind(gl);
		IntBuffer histoData = (histo.mapRange(gl, GL_ACCESS.READ_ONLY, 0,
				binCounts[0] * binCounts[1] * binCounts[2] * Integer.BYTES)).asIntBuffer();
		return histoData;
	}

	public ByteBuffer[] createPointWithHisto(IntBuffer histo, int[] binCounts) {
		ByteBuffer bb = GLBufferFactory.allocate(binCounts[0] * binCounts[1] * binCounts[2] * Float.BYTES * 2);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();
		ByteBuffer bbColor = GLBufferFactory.allocate(binCounts[0] * binCounts[1] * binCounts[2] * Float.BYTES * 4);
		FloatBuffer floatColorbuffervalues = bbColor.asFloatBuffer();

		Random random = new Random();

		float factor = binCounts[0] - 1;

		for (int i = 0; i < binCounts[0]; i++) {
			for (int j = 0; j < binCounts[1]; j++) {
				for (int k = 0; k < binCounts[2]; k++) {

					int index = i * binCounts[0] + j * binCounts[1] + k;
					if (histo.get(index) != 0) {

						int offset = (i * j * binCounts[1] + k) * 4;
						int offset2 = (i * j * binCounts[1] + k) * 2;
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
		}
		return new ByteBuffer[] { bb, bbColor };
	}

}
