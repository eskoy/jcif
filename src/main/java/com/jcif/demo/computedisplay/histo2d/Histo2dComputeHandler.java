package com.jcif.demo.computedisplay.histo2d;

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

	public ByteBuffer[] createHisto2dFromBuffer(IntBuffer histo, int nabins, int nbbins) {

		int minCount = Integer.MAX_VALUE;
		int maxCount = Integer.MIN_VALUE;
		int size = 0;
		histo.rewind();
		for (int i = 0; i < histo.capacity(); i++) {
			int count = histo.get();
			if (count != 0) {
				minCount = Math.min(minCount, count);
				maxCount = Math.max(maxCount, count);
				size++;
			}
		}

		ByteBuffer bb = GLBufferFactory.allocate(size * Float.BYTES * 2);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();
		ByteBuffer bbcount = GLBufferFactory.allocate(size * Float.BYTES);
		FloatBuffer floatcountvalues = bbcount.asFloatBuffer();
		ByteBuffer bbColor = GLBufferFactory.allocate(size * Float.BYTES * 4);
		FloatBuffer floatColorbuffervalues = bbColor.asFloatBuffer();

		histo.rewind();
		float factorx = nabins - 1;
		float factory = nbbins - 1;

		for (int i = 0; i < nabins; i++) {
			for (int j = 0; j < nbbins; j++) {

				int count = histo.get();
				if (count != 0) {

					float normalizecount = (count - minCount) / maxCount;
					floatcountvalues.put(normalizecount);

					float x = (i / factorx) * 2f - 1f;
					float y = (j / factory) * 2f - 1f;

					floatbuffervalues.put(x);
					floatbuffervalues.put(y);

					float r = 0.25f;
					float g = 0.25f;
					float b = 0.25f;

					floatColorbuffervalues.put(r);
					floatColorbuffervalues.put(g);
					floatColorbuffervalues.put(b);
					floatColorbuffervalues.put(1f);
				}
			}
		}
		return new ByteBuffer[] { bb, bbColor, bbcount };
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
