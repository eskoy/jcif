package com.jcif.opengl.glcompute.filter;

import com.jcif.opengl.GLComputeProgram;
import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlBufferFactory.GL_TYPE;
import com.jcif.opengl.GlBufferFactory.GL_USAGE;
import com.jcif.opengl.GlUtil;
import com.jogamp.opengl.GL4;

public class Filter2d {

	protected GLComputeProgram program;

	public Filter2d(GL4 gl) {
		initialize(gl);
	}

	public void initialize(GL4 gl) {

		this.initShaders(gl);

	}

	protected void initShaders(final GL4 gl) {

		String prog = GlUtil.loadAsText(getClass(), "Filter2d.comp");

		this.program = new GLComputeProgram(gl, prog);

	}

	public GlBuffer filter2d(GL4 gl, GlBuffer gpuSrc_indices, int pulseCount, GlBuffer gpuSrc_valuesA, float minValueA,
			float maxValueA, GlBuffer gpuSrc_valuesB, float minValueB, float maxValueB) {

		GlBuffer gpuDst_counts = GlBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.DYNAMIC_DRAW);
		gpuDst_counts.bind(gl);
		gpuDst_counts.allocate(gl, pulseCount * Integer.BYTES);
		gpuDst_counts.release(gl);

		this.program.beginUse(gl);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 0, gpuDst_counts);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 1, gpuSrc_indices);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 2, gpuSrc_valuesA);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 3, gpuSrc_valuesB);
		this.program.setUniform(gl, 0, pulseCount);
		this.program.setUniform(gl, 1, minValueA, maxValueA, minValueB, maxValueB);
		this.program.compute(gl, 4096, 1, 1);
		// this.program.memoryBarrier();
		this.program.endUse(gl);

		return gpuDst_counts;
	}

}
