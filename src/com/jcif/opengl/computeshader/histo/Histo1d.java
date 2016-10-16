package com.jcif.opengl.computeshader.histo;

import com.jcif.opengl.GLComputeProgram;
import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlUtil;
import com.jcif.opengl.GlBufferFactory.GL_TYPE;
import com.jcif.opengl.GlBufferFactory.GL_USAGE;
import com.jogamp.opengl.GL4;

public class Histo1d {

	protected GLComputeProgram program;

	public Histo1d(GL4 gl) {
		initialize(gl);
	}

	public void initialize(final GL4 gl) {

		this.initShaders(gl);

	}

	protected void initShaders(final GL4 gl) {

		String prog = GlUtil.loadAsText(getClass(), "Histo1d.cs");

		this.program = new GLComputeProgram(gl, prog);

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
	public GlBuffer histogram1D(GL4 gl, GlBuffer gpuSrc_indices, int pulseCount, GlBuffer gpuSrc_values, float minValue,
			float maxValue, int binCount) {

		GlBuffer gpuDst_counts = GlBufferFactory.newGLBuffer(gl, GL_TYPE.ATOMIC_COUNTER_BUFFER, GL_USAGE.DYNAMIC_DRAW);
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
