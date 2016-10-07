package com.jcif.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

public class GLDeviceProperties {

	public String GL_VENDOR;

	public String GL_RENDERER;

	public String GL_VERSION;

	public int[] GL_MAX_COMPUTE_WORK_GROUP_COUNT = new int[3];

	public int[] GL_MAX_COMPUTE_WORK_GROUP_SIZE = new int[3];

	public int[] GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = new int[1];

	public GLDeviceProperties(GL4 gl) {
		init(gl);
	}

	public void init(GL4 gl) {

		GL_VENDOR = gl.glGetString(GL.GL_VENDOR);
		GL_RENDERER = gl.glGetString(GL.GL_RENDERER);
		GL_VERSION = gl.glGetString(GL.GL_VERSION);

		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 0, GL_MAX_COMPUTE_WORK_GROUP_COUNT, 0);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 1, GL_MAX_COMPUTE_WORK_GROUP_COUNT, 1);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 2, GL_MAX_COMPUTE_WORK_GROUP_COUNT, 2);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 0, GL_MAX_COMPUTE_WORK_GROUP_SIZE, 0);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 1, GL_MAX_COMPUTE_WORK_GROUP_SIZE, 1);
		gl.glGetIntegeri_v(GL4.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 2, GL_MAX_COMPUTE_WORK_GROUP_SIZE, 2);
		gl.glGetIntegerv(GL4.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS, GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS, 0);

	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();

		buffer.append("################### GLDeviceProperties begin ###################\n");
		buffer.append(String.format("GL_VENDOR: %1$s \n", GL_VENDOR));
		buffer.append(String.format("GL_RENDERER: %1$s \n", GL_RENDERER));
		buffer.append(String.format("GL_VERSION: %1$s \n", GL_VERSION));

		buffer.append(String.format(
				"GL_MAX_COMPUTE_WORK_GROUP_COUNT global (total) work group size x: %1$d , y: %2$d z: %3$d \n",
				GL_MAX_COMPUTE_WORK_GROUP_COUNT[0], GL_MAX_COMPUTE_WORK_GROUP_COUNT[1],
				GL_MAX_COMPUTE_WORK_GROUP_COUNT[2]));

		buffer.append(String.format(
				"GL_MAX_COMPUTE_WORK_GROUP_SIZE local (in one shader) work group sizes x: %1$d , y: %2$d z: %3$d \n",
				GL_MAX_COMPUTE_WORK_GROUP_SIZE[0], GL_MAX_COMPUTE_WORK_GROUP_SIZE[1],
				GL_MAX_COMPUTE_WORK_GROUP_SIZE[2]));

		buffer.append(String.format("GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS  computer shader invocations %1$d\n",
				GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS[0]));
		buffer.append("################### GLDeviceProperties end ###################\n");
		return buffer.toString();
	}

}
