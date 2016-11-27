package com.jcif.opengl.windowtoolkit.impl;

import com.jcif.opengl.GLPainter;
import com.jcif.opengl.windowtoolkit.GLPainterController;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

public class GLSwingPainterController extends GLPainterController<GLCanvas> {

	protected GLCanvas gLCanvas = new GLCanvas();

	public GLSwingPainterController() {
		super();

		// init with default background
		this.addPainter(defaultBackGroundPainter);
		// register gl event
		gLCanvas.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
				// glautodrawable.getContext().makeCurrent();
				// GL4 gl = glautodrawable.getGL().getGL4();

				backGround.getViewPort()[0] = x;
				backGround.getViewPort()[1] = y;
				backGround.getViewPort()[2] = width;
				backGround.getViewPort()[3] = height;
				defaultBackGroundPainter.update(backGround);
				// glautodrawable.getContext().release();

			}

			@Override
			public void init(GLAutoDrawable glautodrawable) {
				glautodrawable.getContext().makeCurrent();
				GL4 gl = glautodrawable.getGL().getGL4();
				for (GLPainter<?> painter : painterList) {
					painter.init(gl);
				}
				glautodrawable.getContext().release();
				;
			}

			@Override
			public void dispose(GLAutoDrawable glautodrawable) {
			}

			@Override
			public void display(GLAutoDrawable glautodrawable) {
				glautodrawable.getContext().makeCurrent();
				GL4 gl = glautodrawable.getGL().getGL4();
				for (GLPainter<?> painter : painterList) {
					painter.paint(gl);
				}

				glautodrawable.getContext().release();
				;

			}
		});

	}

	@Override
	public GLCanvas getDisplayComponent() {

		return gLCanvas;
	}

	@Override
	public void setSharedContext(GLContext glContext) {
		gLCanvas.setSharedContext(glContext);

	}

	@Override
	public void repaint() {
		gLCanvas.display();

	}

}
