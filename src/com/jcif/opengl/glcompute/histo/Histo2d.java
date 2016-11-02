package com.jcif.opengl.glcompute.histo;

import com.jcif.opengl.GLComputeProgram;
import com.jcif.opengl.GlBuffer;
import com.jcif.opengl.GlBufferFactory;
import com.jcif.opengl.GlBufferFactory.GL_TYPE;
import com.jcif.opengl.GlBufferFactory.GL_USAGE;
import com.jcif.opengl.GlUtil;
import com.jogamp.opengl.GL4;

public class Histo2d {

	protected GLComputeProgram program;

	public Histo2d(GL4 gl) {
		initialize(gl);
	}

	public void initialize(GL4 gl) {

		this.initShaders(gl);

	}

	protected void initShaders(final GL4 gl) {

		String prog = GlUtil.loadAsText(getClass(), "Histo2d.comp");

		this.program = new GLComputeProgram(gl, prog);

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
	 * @param gpuSrc_valuesA
	 *            The identifier of the source GPU-buffer containing the A
	 *            values to analyse.
	 * @param minValueA
	 *            The lower bound of the range used for counting A values.
	 * @param maxValueA
	 *            The upper bound of the range used for counting A values.
	 * @param binCountA
	 *            The number of bins of the range used for counting A values.
	 * @param gpuSrc_valuesB
	 *            The identifier of the source GPU-buffer containing the B
	 *            values to analyse.
	 * @param minValueB
	 *            The lower bound of the range used for counting B values.
	 * @param maxValueB
	 *            The upper bound of the range used for counting B values.
	 * @param binCountB
	 *            The number of bins of the range used for counting B values.
	 * @return Success.
	 */
	public GlBuffer histogram2D(GL4 gl, GlBuffer gpuSrc_indices, int pulseCount, GlBuffer gpuSrc_valuesA,
			float minValueA, float maxValueA, int binCountA, GlBuffer gpuSrc_valuesB, float minValueB, float maxValueB,
			int binCountB) {

		GlBuffer gpuDst_counts = GlBufferFactory.newGLBuffer(gl, GL_TYPE.ATOMIC_COUNTER_BUFFER, GL_USAGE.DYNAMIC_DRAW);
		gpuDst_counts.bind(gl);
		gpuDst_counts.allocate(gl, binCountA * binCountB * Integer.BYTES);
		gpuDst_counts.release(gl);

		// binCount*(v-minValue)/(maxValue-minValue)
		float factorA = binCountA / (maxValueA - minValueA);
		float incrementA = -minValueA * factorA;

		float factorB = binCountB / (maxValueB - minValueB);
		float incrementB = -minValueB * factorB;

		this.program.beginUse(gl);

		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 0, gpuDst_counts);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 1, gpuSrc_indices);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 2, gpuSrc_valuesA);
		this.program.bindBufferBase(gl, GL4.GL_SHADER_STORAGE_BUFFER, 3, gpuSrc_valuesB);

		this.program.setUniform(gl, 0, pulseCount, binCountA, binCountB);
		this.program.setUniform(gl, 1, factorA, factorB, incrementA, incrementB);
		this.program.compute(gl, 4096, 1, 1);
		// this.program.memoryBarrier();
		this.program.endUse(gl);

		return gpuDst_counts;
	}

}
