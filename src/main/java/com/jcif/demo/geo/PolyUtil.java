package com.jcif.demo.geo;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

public class PolyUtil {

	public static void normalize(List<ByteBuffer> polygonList) {

		float maxX = 0;
		float maxY = 0;

		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;

		for (ByteBuffer bb : polygonList) {
			FloatBuffer polygon = bb.asFloatBuffer();
			polygon.rewind();
			for (int i = 0; i < polygon.capacity() / 2; i++) {

				float currentLon = polygon.get();
				float currentLat = polygon.get();

				maxX = Math.max(maxX, currentLon);
				maxY = Math.max(maxY, currentLat);
				minX = Math.min(minX, currentLon);
				minY = Math.min(minY, currentLat);

			}

		}
		for (ByteBuffer bb : polygonList) {
			FloatBuffer polygon = bb.asFloatBuffer();
			polygon.rewind();

			for (int i = 0; i < polygon.capacity() / 2; i++) {

				int position = 2 * i;
				float currentx = polygon.get(position);
				currentx = ((currentx - minX) / (maxX - minX) * 2) - 1;
				polygon.put(position, currentx);
				float currenty = polygon.get(position + 1);
				currenty = ((currenty - minY) / (maxY - minY) * 2) - 1;
				polygon.put(position + 1, currenty);

			}
		}

		for (ByteBuffer bb : polygonList) {
			FloatBuffer polygon = bb.asFloatBuffer();
			polygon.rewind();
		}
	}
}
