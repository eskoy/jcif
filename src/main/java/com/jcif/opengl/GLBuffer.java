package com.jcif.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.jcif.opengl.GLBufferFactory.GL_ACCESS;
import com.jcif.opengl.GLBufferFactory.GL_TYPE;
import com.jcif.opengl.GLBufferFactory.GL_USAGE;
import com.jogamp.opengl.GL4;

public class GLBuffer {

	protected int type;

	protected int usage;

	protected int id;

	public int getId() {
		return id;
	}

	public GLBuffer(GL_TYPE ntype, GL_USAGE nusage) {
		this.type = ntype.glValue();
		this.usage = nusage.glValue();
	}

	void create(final GL4 gl) {
		int[] idtab = new int[1];
		gl.glGenBuffers(1, idtab, 0);
		id = idtab[0];

	}

	public void bind(GL4 gl) {
		gl.glBindBuffer(this.type, this.id);
	}

	public void release(GL4 gl) {
		gl.glBindBuffer(this.type, 0);
	}

	public void read(GL4 gl, int offset, ByteBuffer bb, int count) {
		gl.glGetBufferSubData(this.type, offset, count, bb);
	}

	public void write(GL4 gl, int offset, ByteBuffer bb, int count) {
		gl.glBufferSubData(this.type, offset, count, bb);
	}

	public void allocate(GL4 gl, ByteBuffer bb, int count) {

		gl.glBufferData(this.type, count, bb, this.usage);
	}

	

	public void free(GL4 gl) {
		IntBuffer intbb = IntBuffer.allocate(1);
		intbb.put(0, this.id);
		gl.glDeleteBuffers(1, intbb);
	}

	public void allocate(GL4 gl, int count) {
		allocate(gl, null, count);
	}

	public ByteBuffer map(GL4 gl, GL_ACCESS access) {
		return gl.glMapBuffer(this.type, access.glValue);
	}

	public ByteBuffer mapRange(GL4 gl, GL_ACCESS access, final int offset, final int length) {
		return gl.glMapBufferRange(this.type, offset, length, access.glMapValue);
	}

	public void unmap(GL4 gl) {
		gl.glUnmapBuffer(this.type);
	}

}
