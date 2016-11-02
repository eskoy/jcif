package com.jcif.opengl.glpainter.backgroung;

import com.jcif.opengl.GLPainter;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL4;

public class BackGroundPainter implements GLPainter<BackGround> {

	protected BackGround backGround;

	int[] viewPort;

	@Override
	public void update(BackGround t) {
		backGround = t;

	}

	@Override
	public void init(GL4 gl) {

	}

	@Override
	public void paint(GL4 gl, int... newviewport) {

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);

		if (newviewport.length == 4)
			viewPort = newviewport;
		gl.glViewport(viewPort[0], viewPort[1], viewPort[2], viewPort[3]);

		// Clear screen
		gl.glClearColor(0.1f, 0.0f, 0.1f, 1f);
		gl.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);

	}

}
