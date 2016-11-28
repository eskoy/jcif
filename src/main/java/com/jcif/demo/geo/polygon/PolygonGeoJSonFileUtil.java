package com.jcif.demo.geo.polygon;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcif.opengl.GLBufferFactory;

public class PolygonGeoJSonFileUtil

{

	public static void loadPolygon(List<ByteBuffer> polygonList, String filePath) {

		try {
			FileInputStream inputStream = new FileInputStream(filePath);

			GeoJsonObject geoJsonObject = new ObjectMapper().readValue(inputStream, GeoJsonObject.class);

			if (geoJsonObject instanceof FeatureCollection) {

				FeatureCollection collection = (FeatureCollection) geoJsonObject;

				Iterator<Feature> iterator = collection.iterator();

				while (iterator.hasNext()) {
					Feature feature = iterator.next();

					GeoJsonObject obj = feature.getGeometry();

					if (obj instanceof MultiPolygon) {

						MultiPolygon multiPolygon = (MultiPolygon) obj;

						List<List<List<LngLatAlt>>> coor = multiPolygon.getCoordinates();

						for (List<List<LngLatAlt>> l : coor) {

							readPolygons(l, polygonList);

						}

					} else if (obj instanceof Polygon) {

						Polygon polygon = (Polygon) obj;

						List<List<LngLatAlt>> coor = polygon.getCoordinates();

						readPolygons(coor, polygonList);

					} else {
						System.err.println(".....");
					}

				}
			}

			inputStream.close();
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected static void readPolygons(List<List<LngLatAlt>> coordinate, List<ByteBuffer> polygonList) {

		for (List<LngLatAlt> ll : coordinate) {

			int polygonSize = ll.size() + 1; // close the polygone sytematic
			ByteBuffer bb = GLBufferFactory.allocate(polygonSize * 2 * Float.BYTES);
			FloatBuffer polygon = bb.asFloatBuffer();

			for (LngLatAlt lalon : ll) {

				polygon.put((float) lalon.getLongitude());
				polygon.put((float) lalon.getLatitude());

			}

			polygon.put((float) ll.get(0).getLongitude());
			polygon.put((float) ll.get(0).getLatitude());

			polygonList.add(bb);
		}

	}

}
