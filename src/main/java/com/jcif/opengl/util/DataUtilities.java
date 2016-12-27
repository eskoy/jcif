package com.jcif.opengl.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;

import com.jcif.opengl.GLBufferFactory;

public class DataUtilities {

	public enum DATA_TYPE {

		WHITE_NOISE, LINEAR, MODULO, COSINUS;

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
				floatbuffervalues.put(i, (i % param[0]) * 1f / param[0]);
			}
			break;
		case COSINUS:
			for (int i = 0; i < nb; i++) {
				floatbuffervalues.put(i, ((float) Math.cos((i * (2 * Math.PI)) / nb) + 1) / 2);
			}
			break;

		default:
			break;
		}

//		for (int i = 0; i < nb; i++) {
//			System.err.println(floatbuffervalues.get(i));
//		}
		return bb;
	}

}
