package com.jcif.opengl.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;

import com.jcif.opengl.GLBufferFactory;

public class DataUtilities {

	public enum DATA_TYPE {

		WHITE_NOISE, LINEAR, MODULO, COSINUS, SINUS;

	}

	public static ByteBuffer createNewData1d(int nb, DATA_TYPE type, int... param) {

		ByteBuffer bb = GLBufferFactory.allocate(nb * Float.BYTES);
		FloatBuffer floatbuffervalues = bb.asFloatBuffer();

		switch (type) {
		case WHITE_NOISE:
			Random rnd = new Random();
			for (int i = 0; i < nb; i++) {
				floatbuffervalues.put(i, rnd.nextFloat());
			}
			break;

		case LINEAR:
			for (int i = 0; i < nb; i++) {
				floatbuffervalues.put(i, i * 1f / nb);
			}
			break;

		case MODULO:
			for (int i = 0; i < nb; i++) {
				int modulo = 10;
				if (param.length != 0)
					modulo = param[0];

				floatbuffervalues.put(i, (i % modulo) * 1f / modulo);
			}
			break;
		case COSINUS:
			for (int i = 0; i < nb; i++) {
				floatbuffervalues.put(i, ((float) Math.cos((i * (2 * Math.PI)) / nb) + 1) / 2);
			}
			break;

		case SINUS:
			for (int i = 0; i < nb; i++) {
				floatbuffervalues.put(i, ((float) Math.sin((i * (2 * Math.PI)) / nb) + 1) / 2);
			}
			break;

		default:
			break;
		}

		return bb;
	}

}
