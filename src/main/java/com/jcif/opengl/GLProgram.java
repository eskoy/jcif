package com.jcif.opengl;

import java.util.IdentityHashMap;
import java.util.Map;

import com.jogamp.opengl.GL4;

public abstract class GLProgram {

	protected int id;

	private Map<String, Integer> uniformLocations = new IdentityHashMap<String, Integer>();

	public GLProgram(GL4 gl) {
		this.id = gl.glCreateProgram();
	}

	public int getId() {
		return id;
	}

	public void beginUse(GL4 gl) {
		gl.glUseProgram(this.getId());
	}

	public void endUse(GL4 gl) {
		gl.glUseProgram(0);
	}

	protected void dispose(GL4 gl) {
		gl.glDeleteProgram(this.getId());
	}

	public boolean hasGlError(GL4 gl) {

		int error = gl.glGetError();
		if (error != 0) {
			String glerror = "";

			switch (error) {
			case GL4.GL_INVALID_ENUM:
				glerror = "GL_INVALID_ENUM";
				break;

			case GL4.GL_INVALID_VALUE:
				glerror = "GL_INVALID_VALUE";
				break;

			case GL4.GL_INVALID_OPERATION:
				glerror = "GL_INVALID_OPERATION";
				break;

			case GL4.GL_OUT_OF_MEMORY:
				glerror = "GL_OUT_OF_MEMORY";
				break;
			default:
				break;
			}

			System.err.println(String.format("GL ERROR :  %d   %s + \n", error, glerror));
		}
		// return true on error
		return error == GL4.GL_NO_ERROR ? false : true;
	}



	public int getShaderParameter(GL4 gl, int obj, int paramName) {
		final int params[] = new int[1];
		gl.glGetShaderiv(obj, paramName, params, 0);
		return params[0];
	}

	public String getProgramInfoLog(GL4 gl) {
		String result ="No Log";
		int size = getProgramParameter(gl, GL4.GL_INFO_LOG_LENGTH);
		if (size > 1) {
		final int[] retLength = new int[1];
		final byte[] bytes = new byte[size + 1];
		gl.glGetProgramInfoLog(this.getId(), size, retLength, 0, bytes, 0);
		result = new String(bytes);
		}
		return result;
	}

	public int getProgramParameter(GL4 gl, int paramName) {
		final int params[] = new int[1];
		gl.glGetProgramiv(this.getId(), paramName, params, 0);
		return params[0];
	}

	protected int getUniformLocation(GL4 gl, String uniform) {
		Integer result = uniformLocations.get(uniform);
		if (result == null) {
			result = gl.glGetUniformLocation(this.getId(), uniform);
			uniformLocations.put(uniform, result);
		}
		return result;
	}

	public void setUniform(GL4 gl, String uniform, float... values) {
		int loc = getUniformLocation(gl, uniform);
		setUniform(gl, loc, values);
	}

	public void setUniform(GL4 gl, int loc, float... values) {
		switch (values.length) {
		case 1:
			gl.glUniform1f(loc, values[0]);
			break;
		case 2:
			gl.glUniform2f(loc, values[0], values[1]);
			break;
		case 3:
			gl.glUniform3f(loc, values[0], values[1], values[2]);
			break;
		case 4:
			gl.glUniform4f(loc, values[0], values[1], values[2], values[3]);
			break;

		default:
			gl.glUniform1fv(loc, values.length, values, 0);
			break;
		}

		hasGlError(gl);

	}

	public void setUniform(GL4 gl, String uniform, int... values) {
		int loc = getUniformLocation(gl, uniform);
		setUniform(gl, loc, values);
	}

	public void setUniform(GL4 gl, int loc, int... values) {
		switch (values.length) {
		case 1:
			gl.glUniform1i(loc, values[0]);
			break;
		case 2:
			gl.glUniform2i(loc, values[0], values[1]);
			break;
		case 3:
			gl.glUniform3i(loc, values[0], values[1], values[2]);
			break;
		case 4:
			gl.glUniform4i(loc, values[0], values[1], values[2], values[3]);
			break;

		default:
			gl.glUniform1iv(loc, values.length, values, 0);
			break;
		}

		hasGlError(gl);
	}

	public void bindBufferBase(GL4 gl, final int buffertype, final int bindingId, GLBuffer buffer) {
		gl.glBindBufferBase(buffertype, bindingId, buffer.getId());

	}
}
