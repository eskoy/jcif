package com.jcif.opengl;

import com.jogamp.opengl.GL4;

public class GLShader {

	public static String GL_SHADER_VERSION = "#version 430 core";

	public static String NEW_LINE = System.getProperty("line.separator");

	protected int id;

	public int getId() {
		return id;
	}

	public GLShader(int type, GL4 gl, String... shaderString) {

		StringBuffer buffer = new StringBuffer();
		buffer.append(GL_SHADER_VERSION);
		buffer.append(NEW_LINE);
		for (String res : shaderString)
			buffer.append(res);
		createAndCompileShader(type, buffer.toString(), gl);
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
