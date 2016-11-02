package com.jcif.opengl.windowtoolkit;

import java.util.ArrayList;
import java.util.List;

import com.jcif.opengl.GLPainter;
import com.jcif.opengl.glpainter.backgroung.BackGround;
import com.jcif.opengl.glpainter.backgroung.BackGroundPainter;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

public class GLSwingCanvas extends GLCanvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BackGround backGround = new BackGround();
	BackGroundPainter defaultBackGroundPainter = new BackGroundPainter();

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
		this.addPainter(defaultBackGroundPainter);

		// register gl event
		this.addGLEventListener(new GLEventListener() {

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

}
