package com.jcif.demo.windowtoolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;

public class GpuSWTMain {

	public static void main(String[] args) {

		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("%%%%%%%%%%%%%%%%%%%%%%%%%% FastGpuSWT %%%%%%%%%%%%%%%%%%%%%%%%%%");
		shell.setLayout(new FillLayout());
		shell.setSize(640, 480);

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());

		GLData gldata = new GLData();

		gldata.doubleBuffer = true;
		// need SWT.NO_BACKGROUND to prevent SWT from clearing the window
		// at the wrong times (we use glClear for this instead)
		final GLCanvas glcanvas = new GLCanvas(composite, SWT.NO_BACKGROUND, gldata);
		glcanvas.setCurrent();
		gldata.shareContext = glcanvas;

		final GLContext sharedContext = GLDrawableFactory.getFactory(GLProfile.get(GLProfile.GL4bc))
				.createExternalGLContext();

		GpuRender fastGpuRender = new GpuRender();
		fastGpuRender.init(sharedContext.getGL().getGL4());
		// draw the triangle when the OS tells us that any part of the window
		// needs drawing
		glcanvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent paintevent) {
				Rectangle rectangle = glcanvas.getClientArea();
				glcanvas.setCurrent();
				sharedContext.makeCurrent();
				fastGpuRender.render(sharedContext.getGL().getGL4(), 0, 0, rectangle.width, rectangle.height);
				glcanvas.swapBuffers();
				sharedContext.release();
			}
		});

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		glcanvas.dispose();
		display.dispose();
	}
}