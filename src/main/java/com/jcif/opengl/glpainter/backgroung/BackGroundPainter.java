package com.jcif.opengl.glpainter.backgroung;

import com.jcif.opengl.GLPainter;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL4;

public class BackGroundPainter implements GLPainter<BackGround> {

	protected BackGround backGround = new BackGround();

	public BackGround getBackGround() {
		return backGround;
	}

	public void setBackGround(BackGround backGround) {
		this.backGround = backGround;
	}

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

		gl.glViewport(this.backGround.getViewPort()[0], this.backGround.getViewPort()[1],
				this.backGround.getViewPort()[2], this.backGround.getViewPort()[3]);

		// Clear screen
		gl.glClearColor(0.1f, 0.0f, 0.1f, 1f);
		gl.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);

	}

}
