package com.jcif.demo.computedisplay;

import java.awt.Color;

import com.jcif.opengl.glpainter.grid.Grid;
import com.jcif.opengl.glpainter.grid.GridPainter;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

public class GpuRender {

	public GridPainter gridPainter = new GridPainter();

	public Grid grid = new Grid();

	public void init(GL4 gl) {

		this.gridPainter.init(gl);
	}

	public GpuRender() {

	}

	public void initViewPort(GL4 gl, int x, int y, int width, int height) {
		gl.glViewport(x, y, width, height);

	}

	public void render(GL4 gl, int x, int y, int width, int height) {
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
		gl.glViewport(x, y, width, height);
		// Clear screen
		gl.glClearColor(0.1f, 0.0f, 0.1f, 1f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		grid.setStep(5);
		grid.setColor(Color.PINK);

		gridPainter.update(grid);
		gridPainter.paint(gl);

	}

}
