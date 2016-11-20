package com.jcif.demo.computedisplay;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jcif.opengl.GlSharedContextInstance;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;

public class GpuSwingSharedContextMain {

	public static void main(String[] args) {

		GLContext sharedContext = GlSharedContextInstance.getInstance().getGLSharedContext();
		System.err.println(GlSharedContextInstance.getInstance().getGLDeviceProperties());

		GpuRender gpuRender = new GpuRender();

		GLJPanel gljpanel = new GLJPanel(null);
		gljpanel.setSharedContext(sharedContext);

		gljpanel.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {

				gpuRender.render(glautodrawable.getGL().getGL4(), x, y, width, height);
			}

			@Override
			public void init(GLAutoDrawable glautodrawable) {

				gpuRender.init(glautodrawable.getGL().getGL4());
			}

			@Override
			public void dispose(GLAutoDrawable glautodrawable) {
			}

			@Override
			public void display(GLAutoDrawable glautodrawable) {

			}
		});

		final JFrame jframe = new JFrame(" GpuSwingSharedContextMain ");
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