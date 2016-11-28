package com.jcif.demo.geo.polyline;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

import com.jcif.opengl.GLBufferFactory;

public class PolylineShapeFileUtil {

	public static void loadPolygon(List<ByteBuffer> polygonList, String filePath) {

		try {

			FileInputStream is = new FileInputStream(filePath);
			ValidationPreferences prefs = new ValidationPreferences();
			prefs.setMaxNumberOfPointsPerShape(16650);
			ShapeFileReader r = new ShapeFileReader(is, prefs);

			ShapeFileHeader h = r.getHeader();
			System.out.println("The shape type of this files is " + h.getShapeType());

			AbstractShape s;
			while ((s = r.next()) != null) {

				switch (s.getShapeType()) {
				case POLYLINE:
					PolylineShape aPolyline = (PolylineShape) s;

					for (int i = 0; i < aPolyline.getNumberOfParts(); i++) {
						PointData[] coordinate = aPolyline.getPointsOfPart(i);
						readPolygons(coordinate, polygonList);
					}
					break;
				default:
					System.out.println("Read other type of shape.");
				}

			}

			is.close();
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected static void readPolygons(PointData[] coordinate, List<ByteBuffer> polygonList) {

		ByteBuffer bb = GLBufferFactory.allocate(coordinate.length * 2 * Float.BYTES);
		FloatBuffer polygon = bb.asFloatBuffer();
		for (PointData ll : coordinate) {

			polygon.put((float) ll.getX());
			polygon.put((float) ll.getY());

		}

		polygonList.add(bb);
	}

	public static void main(String[] args) {

	}
}
