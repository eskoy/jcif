package com.jcif.demo.geo.polyline;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.jcif.awt.ColorUtil;
import com.jcif.demo.geo.PolyUtil;
import com.jcif.opengl.glpainter.poly.Poly;
import com.jcif.opengl.glpainter.poly.PolyLinePainter;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;

public class PolylineMain {

	public static String MAP_FILE_PATH = "10m_coastline.shp";

	public static void main(String[] args) {

		GLJPanel gljpanel = new GLJPanel();

		List<ByteBuffer> polygonBuffer = new ArrayList<>();
		List<Poly> polygonList = new ArrayList<>();
		PolylineShapeFileUtil.loadPolygon(polygonBuffer, MAP_FILE_PATH);

		PolyUtil.normalize(polygonBuffer);

		Color[] color = ColorUtil.computeRandomColor(polygonBuffer.size());

		int i = 0;
		for (ByteBuffer current : polygonBuffer) {
			Poly poly = new Poly();
			poly.setColor(color[i++]);
			poly.setSize(poly.getSize());
			poly.setPolygon(current);
			polygonList.add(poly);
		}

		PolyLinePainter painter = new PolyLinePainter();
		painter.update(polygonList);
		PolylineRender aGpuLinesRender = new PolylineRender();
		aGpuLinesRender.setPainter(painter);

		gljpanel.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {

				aGpuLinesRender.render(glautodrawable.getGL().getGL4(), x, y, width, height);
			}

			@Override
			public void init(GLAutoDrawable glautodrawable) {

				aGpuLinesRender.init(glautodrawable.getGL().getGL4());
			}

			@Override
			public void dispose(GLAutoDrawable glautodrawable) {
			}

			@Override
			public void display(GLAutoDrawable glautodrawable) {

			}
		});

		final JFrame jframe = new JFrame("Draw shape file polyline with OpenGl line !!!!!  ");
		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowevent) {
				jframe.dispose();
				System.exit(0);
			}
		});

		jframe.getContentPane().add(gljpanel, BorderLayout.CENTER);
		jframe.setSize(640, 480);
		jframe.setVisible(true);
	}
}