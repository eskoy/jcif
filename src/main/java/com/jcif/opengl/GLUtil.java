package com.jcif.opengl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GLUtil {

	public static String loadAsText(Class cls, String name) {
		byte[] buffer = new byte[1024];
		int nr;
		try (InputStream in = cls.getResourceAsStream(name); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			while ((nr = in.read(buffer)) > 0) {
				out.write(buffer, 0, nr);
			}

			return new String(out.toByteArray(), "UTF-8");
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static float[] colorAsVec4(Color col) {
		return new float[] { col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, col.getAlpha() / 255f };

	}

}
