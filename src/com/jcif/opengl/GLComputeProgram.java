package com.jcif.opengl;

import com.jogamp.opengl.GL4;

public class GLComputeProgram extends GLProgram {

	private GLShader computeShader;

	public GLComputeProgram(GL4 gl, String compute) {
		super(gl);

		computeShader = new GLShader(GL4.GL_COMPUTE_SHADER, compute, gl);

		gl.glAttachShader(this.getId(), computeShader.getId());

		gl.glLinkProgram(this.getId());

		int flag = getProgramParameter(gl, GL4.GL_LINK_STATUS);

		if (flag != GL4.GL_TRUE) {
			String message = getProgramInfoLog(gl);
			System.err.println(message);
		}

	}

	public void compute(GL4 gl, int x, int y, int z) {
		gl.glDispatchCompute(x, y, z);
	}

	@Override
	public void dispose(GL4 gl) {
		gl.glDetachShader(this.getId(), computeShader.getId());
		gl.glDeleteShader(computeShader.getId());
		super.dispose(gl);
	}

	public void memoryBarrier(GL4 gl) {
		gl.glMemoryBarrier(GL4.GL_SHADER_STORAGE_BARRIER_BIT);
	}

}
