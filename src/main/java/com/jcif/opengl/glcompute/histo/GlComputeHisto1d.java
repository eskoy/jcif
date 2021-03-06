package com.jcif.opengl.glcompute.histo;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLBufferFactory.GL_TYPE;
import com.jcif.opengl.GLBufferFactory.GL_USAGE;
import com.jcif.opengl.GLComputeProgram;
import com.jogamp.opengl.GL4;

public class GlComputeHisto1d {

	protected GLComputeProgram program;

	public GlComputeHisto1d(GL4 gl) {
		initialize(gl);
	}

	public void initialize(final GL4 gl) {

		this.initShaders(gl);

	}

	protected void initShaders(final GL4 gl) {

		// String prog = GLUtil.loadAsText(getClass(), "Histo1d.comp");

		this.program = new GLComputeProgram(gl, GLSLHISTO.HISTO1D);

	}

	/**
	 * Builds up an histogram of the float value of each pulse. The destination
	 * GPU-buffer is initially reset to zero.
	 * 
	 * @param gpuSrc_indices
	 *            The identifier of the source GPU-buffer containing the indices
	 *            of the pulses to analyse.
	 * @param pulseCount
	 *            The number of pulses to consider.
	 * @param gpuDst_counts
	 *            The identifier of the destination GPU-buffer to store the
	 *            counts of each bin.
	 * @param gpuSrc_values
	 *            The identifier of the source GPU-buffer containing the values
	 *            to analyse.
	 * @param minValue
	 *            The lower bound of the range used for counting values.
	 * @param maxValue
	 *            The upper bound of the range used for counting values.
	 * @param binCount
	 *            The number of bins of the range used for counting values.
	 * @return Success.
	 */
	public GLBuffer histogram1D(GL4 gl, GLBuffer gpuSrc_indices, int pulseCount, GLBuffer gpuSrc_values, float minValue,
			float maxValue, int binCount) {

		GLBuffer gpuDst_counts = GLBufferFactory.newGLBuffer(gl, GL_TYPE.ATOMIC_COUNTER_BUFFER, GL_USAGE.DYNAMIC_DRAW);
		gpuDst_counts.bind(gl);
		gpuDst_counts.allocate(gl, binCount * Integer.BYTES);
		gpuDst_counts.release(gl);

		// binCount*(v-minValue)/(maxValue-minValue)
		float factor = binCount / (maxValue - minValue);
		float increment = -minValue * factor;

		this.program.beginUse(gl);

		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 0, gpuDst_counts);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 1, gpuSrc_indices);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 2, gpuSrc_values);

		this.program.setUniform(gl, 0, pulseCount, binCount);
		this.program.setUniform(gl, 1, factor, increment);
		this.program.compute(gl, 4096, 1, 1);
		// this.program.memoryBarrier();
		this.program.endUse(gl);

		return gpuDst_counts;
	}

}
