package com.jcif.opengl;

import com.jogamp.opengl.GL4;

public class GLShader {

	protected int id;

	public int getId() {
		return id;
	}

	public GLShader(int type, String shaderString, GL4 gl) {

		createAndCompileShader(type, shaderString, gl);
	}

	protected void createAndCompileShader(int type, String shaderString, GL4 gl) {

		this.id = gl.glCreateShader(type);

		String[] vlines = new String[] { shaderString };
		int[] vlengths = new int[] { vlines[0].length() };

		gl.glShaderSource(this.id, vlines.length, vlines, vlengths, 0);
		gl.glCompileShader(this.id);

		int[] compiled = new int[1];
		gl.glGetShaderiv(this.id, GL4.GL_COMPILE_STATUS, compiled, 0);

		if (compiled[0] == 0) {
			int[] logLength = new int[1];
			gl.glGetShaderiv(this.id, GL4.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(this.id, logLength[0], (int[]) null, 0, log, 0);

			throw new IllegalStateException("Error compiling the shader: " + new String(log));
		}

	}

}
