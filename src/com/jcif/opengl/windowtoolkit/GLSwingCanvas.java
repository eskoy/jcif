package com.jcif.opengl.windowtoolkit;

import java.util.ArrayList;
import java.util.List;

import com.jcif.opengl.GLPainter;
import com.jcif.opengl.glpainter.backgroung.BackGroundPainter;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

public class GLSwingCanvas extends GLCanvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected List<GLPainter<?>> painterList = new ArrayList<GLPainter<?>>();

	public void addPainter(GLPainter<?> painter) {
		this.painterList.add(painter);

	}

	@Override
	public void repaint() {
		this.display();
	}

	public GLSwingCanvas()

	{

		// init with default background
		BackGroundPainter defaultBackGroundPainter = new BackGroundPainter();
		this.addPainter(defaultBackGroundPainter);

		// register gl event
		this.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
				glautodrawable.getContext().makeCurrent();

				for (GLPainter<?> painter : painterList) {
					painter.paint(glautodrawable.getGL().getGL4(), x, y, width, height);

				}

				glautodrawable.getContext().release();
				;
			}

			@Override
			public void init(GLAutoDrawable glautodrawable) {
				glautodrawable.getContext().makeCurrent();
				for (GLPainter<?> painter : painterList) {
					painter.init(glautodrawable.getGL().getGL4());
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
				for (GLPainter<?> painter : painterList) {
					painter.paint(glautodrawable.getGL().getGL4());
				}

				glautodrawable.getContext().release();
				;

			}
		});
	}

}
