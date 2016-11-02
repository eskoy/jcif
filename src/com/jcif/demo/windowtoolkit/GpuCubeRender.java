package com.jcif.demo.windowtoolkit;

import java.awt.Color;

import com.jcif.opengl.glpainter.cube.Cube;
import com.jcif.opengl.glpainter.cube.CubePainter;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

public class GpuCubeRender {

	public CubePainter painter;
	public Cube cube = new Cube();

	public void init(GL4 gl) {

		this.painter = new CubePainter(gl);
	}

	public GpuCubeRender() {

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
		painter.resize(x, y, width, height);

		cube.setColor(Color.green);
		painter.update(cube);
		painter.paint(gl, width, height);

	}

}
