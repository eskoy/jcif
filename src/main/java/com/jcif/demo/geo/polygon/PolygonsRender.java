package com.jcif.demo.geo.polygon;

import com.jcif.opengl.glpainter.poly.PolygonsPainter;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

public class PolygonsRender {

	public PolygonsPainter painter = new PolygonsPainter();

	public void init(GL4 gl) {

		this.painter.init(gl);
	}

	public PolygonsPainter getPainter() {
		return painter;
	}

	public void setPainter(PolygonsPainter painter) {
		this.painter = painter;
	}

	public PolygonsRender() {

	}

	public void initViewPort(GL4 gl, int x, int y, int width, int height) {
		gl.glViewport(x, y, width, height);

	}

	public void render(GL4 gl, int x, int y, int width, int height) {
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
		gl.glViewport(x, y, width, height);

		// Enable depth test
		gl.glEnable(GL.GL_DEPTH_TEST);
		// Accept fragment if it closer to the camera than the former one
		gl.glDepthFunc(GL.GL_LESS);

		// Clear screen
		gl.glClearColor(0.1f, 0.0f, 0.1f, 1f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		// shapesLines.setColor(Color.green);
		// painter.update(shapesLines);
		painter.paint(gl);

	}

}
