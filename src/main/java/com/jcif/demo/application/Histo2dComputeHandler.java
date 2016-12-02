package com.jcif.demo.application;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLBufferFactory.GL_ACCESS;
import com.jcif.opengl.glcompute.histo.GlComputeHisto2d;
import com.jogamp.opengl.GL4;

public class Histo2dComputeHandler {

	private GlComputeHisto2d compute;

	public Histo2dComputeHandler(GL4 gl) {

		compute = new GlComputeHisto2d(gl);

	}

	public IntBuffer compute(GL4 gl, GLBuffer gpuSrc_valuesA, GLBuffer gpuSrc_valuesB, GLBuffer gpuSrc_indices,
			int size, float min, float max, int nabins, int nbbins) {
		GLBuffer histo = compute.histogram2D(gl, gpuSrc_indices, size, gpuSrc_valuesA, min, max, nabins, gpuSrc_valuesB,
				min, max, nbbins);
		histo.bind(gl);
		IntBuffer histoData = (histo.mapRange(gl, GL_ACCESS.READ_ONLY, 0, nabins * nbbins * Integer.BYTES))
				.asIntBuffer();
		return histoData;
	}

	public void filterPoint(ByteBuffer[] bb, float x, float y, float size) {
		FloatBuffer xy = bb[0].asFloatBuffer();

		FloatBuffer color = bb[1].asFloatBuffer();

		float minx = x - size;

		float maxx = x + size;

		float miny = y - size;
		float maxy = y + size;

		int count = xy.capacity() / 2;

		for (int i = 0; i < count; i++) {
			int xyindex = 2 * i;
			float currentx = xy.get(xyindex + 0);
			float currenty = xy.get(xyindex + 1);

			if ((minx < currentx) && (currentx < maxx) && (miny < currenty) && (currenty < maxy)) {
				int colorindex = 4 * i;
				color.put(colorindex + 0, 1);
				color.put(colorindex + 1, 0);
				color.put(colorindex + 2, 0);
				color.put(colorindex + 3, 1f);

			}

		}

	}

	public ByteBuffer[] createPointWithHisto(IntBuffer histo, int nabins, int nbbins) {
		ByteBuffer bb = GLBufferFactory.allocate(nabins * nbbins * Float.BYTES * 2);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();
		ByteBuffer bbColor = GLBufferFactory.allocate(nabins * nbbins * Float.BYTES * 4);
		FloatBuffer floatColorbuffervalues = bbColor.asFloatBuffer();

		Random random = new Random();

		float factor = nabins - 1;

		for (int i = 0; i < nabins; i++) {
			for (int j = 0; j < nbbins; j++) {
				int index = i * nabins + j;
				if (histo.get(index) != 0) {

					int offset = (i * nabins + j) * 4;
					int offset2 = (i * nabins + j) * 2;
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
			floatbuffervalues.put(i, i);
			// X

		}

		return bb;
	}

	public static ByteBuffer updateIndices(int nb, int value) {

		ByteBuffer bb = GLBufferFactory.allocate(nb * Integer.BYTES);
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
