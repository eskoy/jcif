package com.jcif.opengl.glcompute.histo;

import com.jcif.opengl.GLBuffer;
import com.jcif.opengl.GLBufferFactory;
import com.jcif.opengl.GLBufferFactory.GL_TYPE;
import com.jcif.opengl.GLBufferFactory.GL_USAGE;
import com.jcif.opengl.GLComputeProgram;
import com.jogamp.opengl.GL4;

public class GlComputeHisto3d {

	protected GLComputeProgram program;

	public GlComputeHisto3d(GL4 gl) {
		initialize(gl);
	}

	public void initialize(GL4 gl) {

		this.initShaders(gl);

	}

	protected void initShaders(final GL4 gl) {

		this.program = new GLComputeProgram(gl, GLSLHISTO.HISTO3D);

	}

	/**
	 * Builds up a 2D histogram of the float (A,B) values of each pulse. The
	 * destination GPU-buffer is initially reset to zero and is firstly ordered
	 * along A then B.
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
	 *            The identifier of the source GPU-buffer containing the A
	 *            values to analyse.
	 * @param minValue
	 *            The lower bound of the range used for counting A values.
	 * @param maxValue
	 *            The upper bound of the range used for counting A values.
	 * @param binCount
	 *            The number of bins of the range used for counting A values.
	 */
	public GLBuffer histogram3D(GL4 gl, GLBuffer gpuSrc_indices, int pulseCount, GLBuffer[] gpuSrc_values,
			float[] minValues, float[] maxValues, int[] binCounts) {

		GLBuffer gpuDst_counts = GLBufferFactory.newGLBuffer(gl, GL_TYPE.ATOMIC_COUNTER_BUFFER, GL_USAGE.DYNAMIC_DRAW);
		gpuDst_counts.bind(gl);
		gpuDst_counts.allocate(gl, binCounts[0] * binCounts[1] * binCounts[2] * Integer.BYTES);
		gpuDst_counts.release(gl);

		// binCount*(v-minValue)/(maxValue-minValue)
		float factorA = binCounts[0] / (maxValues[0] - minValues[0]);
		float incrementA = -minValues[0] * factorA;

		float factorB = binCounts[1] / (maxValues[1] - minValues[1]);
		float incrementB = -minValues[1] * factorB;

		float factorC = binCounts[2] / (maxValues[2] - minValues[2]);
		float incrementC = -minValues[2] * factorC;

		this.program.beginUse(gl);

		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 0, gpuDst_counts);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 1, gpuSrc_indices);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 2, gpuSrc_values[0]);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 3, gpuSrc_values[1]);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 4, gpuSrc_values[2]);

		this.program.setUniform(gl, 0, pulseCount, binCounts[0], binCounts[1], binCounts[2]);
		this.program.setUniform(gl, 1, factorA, factorB, factorC);
		this.program.setUniform(gl, 2, incrementA, incrementB, incrementC);
		this.program.compute(gl, 4096, 1, 1);
		// this.program.memoryBarrier();
		this.program.endUse(gl);

		return gpuDst_counts;
	}

}
