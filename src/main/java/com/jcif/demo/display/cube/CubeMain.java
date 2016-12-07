package com.jcif.demo.display.cube;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;

public class CubeMain {

	public static void main(String[] args) {

		GLJPanel gljpanel = new GLJPanel();

		CubeRender gpuCubeRender = new CubeRender();

		gljpanel.addGLEventListener(new GLEventListener() {

			@Override
			public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {

				gpuCubeRender.render(glautodrawable.getGL().getGL4(), x, y, width, height);
			}

			@Override
			public void init(GLAutoDrawable glautodrawable) {

				gpuCubeRender.init(glautodrawable.getGL().getGL4());
			}

			@Override
			public void dispose(GLAutoDrawable glautodrawable) {
			}

			@Override
			public void display(GLAutoDrawable glautodrawable) {

			}
		});

		final JFrame jframe = new JFrame("One Triangle Swing GLJPanel");
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