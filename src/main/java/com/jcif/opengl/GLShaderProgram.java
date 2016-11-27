package com.jcif.opengl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL4;

public class GLShaderProgram extends GLProgram {

	protected final static Logger logger = LoggerFactory.getLogger(GLShaderProgram.class);

	protected GLShader vertShader;

	protected GLShader fragShader;

	protected boolean debug = false;

	public GLShaderProgram(GL4 gl, GLSLProvider vertex, GLSLProvider fragment) {

		this(gl, vertex.getGLSL(), fragment.getGLSL());

		if (debug) {
			try {
				File file = new File(vertex.getClass().getSimpleName() + ".vert");
				PrintWriter vertexWriter = new PrintWriter(file);
				vertexWriter.print(vertex.getGLSL());
				vertexWriter.close();
				file = new File(fragment.getClass().getSimpleName() + ".frag");
				PrintWriter fragmentWriter = new PrintWriter(file);
				fragmentWriter.print(fragment.getGLSL());
				fragmentWriter.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public GLShaderProgram(GL4 gl, String vertex, String fragment) {
		super(gl);

		vertShader = new GLShader(GL4.GL_VERTEX_SHADER, gl, vertex);
		fragShader = new GLShader(GL4.GL_FRAGMENT_SHADER, gl, fragment);
		gl.glAttachShader(this.getId(), vertShader.getId());
		gl.glAttachShader(this.getId(), fragShader.getId());
		gl.glLinkProgram(this.getId());
	}

	@Override
	public void dispose(GL4 gl) {
		gl.glDetachShader(this.getId(), vertShader.getId());
		gl.glDeleteShader(vertShader.getId());
		gl.glDetachShader(this.getId(), fragShader.getId());
		gl.glDeleteShader(fragShader.getId());
		super.dispose(gl);
	}

}
