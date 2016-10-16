package com.jcif.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.jogamp.opengl.GL4;

public abstract class GlBufferFactory {

	/**
	 * 
	 * This enum defines the type of GL buffer object to create with QGLBuffer.
	 * 
	 * <li>{@link #ARRAY_BUFFER}</li> ARRAY_BUFFER object for use when
	 * specifying vertex arrays.
	 * <li>{@link #ELEMENT_ARRAY_BUFFER}</li> ELEMENT_ARRAY_BUFFER buffer object
	 * for use with \c{glDrawElements()}.
	 * <li>{@link #PIXEL_PACK_BUFFER}</li> PIXEL_PACK_BUFFER object for reading
	 * pixel data from the GL server (for example, with \c{glReadPixels()}). Not
	 * supported under OpenGL/ES. data to the GL server (for example, with
	 * \c{glTexImage2D()}). Not supported under OpenGL/ES.0
	 **/

	public enum GL_TYPE {

		ARRAY_BUFFER(GL4.GL_ARRAY_BUFFER), //
		ATOMIC_COUNTER_BUFFER(GL4.GL_ATOMIC_COUNTER_BUFFER), //
		ELEMENT_ARRAY_BUFFER(GL4.GL_ELEMENT_ARRAY_BUFFER), //
		PIXEL_PACK_BUFFER(GL4.GL_PIXEL_PACK_BUFFER), //
		PIXEL_UNPACK_BUFFER(GL4.GL_PIXEL_UNPACK_BUFFER);
		protected int glValue;

		GL_TYPE(int value) {
			this.glValue = value;

		}

		public int glValue() {
			return glValue;
		}

	}

	/**
	 * 
	 * This enum defines the usage pattern of a QGLBuffer object.
	 * 
	 * \value StreamDraw The data will be set once and used a few times for
	 * drawing operations. Under OpenGL/ES 1.1 this is identical to StaticDraw.
	 * \value StreamRead The data will be set once and used a few times for
	 * reading data back from the GL server. Not supported under OpenGL/ES.
	 * \value StreamCopy The data will be set once and used a few times for
	 * reading data back from the GL server for use in further drawing
	 * operations. Not supported under OpenGL/ES. \value StaticDraw The data
	 * will be set once and used many times for drawing operations. \value
	 * StaticRead The data will be set once and used many times for reading data
	 * back from the GL server. Not supported under OpenGL/ES. \value StaticCopy
	 * The data will be set once and used many times for reading data back from
	 * the GL server for use in further drawing operations. Not supported under
	 * OpenGL/ES. \value DynamicDraw The data will be modified repeatedly and
	 * used many times for drawing operations. \value DynamicRead The data will
	 * be modified repeatedly and used many times for reading data back from the
	 * GL server. Not supported under OpenGL/ES. \value DynamicCopy The data
	 * will be modified repeatedly and used many times for reading data back
	 * from the GL server for use in further drawing operations. Not supported
	 * under OpenGL/ES.
	 */

	public enum GL_USAGE {

		STREAM_DRAW(GL4.GL_STREAM_DRAW), //
		STREAM_READ(GL4.GL_STREAM_READ), //
		STREAM_COPY(GL4.GL_STREAM_COPY), //
		STATIC_DRAW(GL4.GL_STATIC_DRAW), //
		STATIC_READ(GL4.GL_STATIC_READ), //
		STATIC_COPY(GL4.GL_STATIC_COPY), //
		DYNAMIC_DRAW(GL4.GL_DYNAMIC_DRAW), //
		DYNAMIC_READ(GL4.GL_DYNAMIC_READ), //
		DYNAMIC_COPY(GL4.GL_DYNAMIC_COPY);//

		protected int glValue;

		GL_USAGE(int value) {
			this.glValue = value;

		}

		public int glValue() {
			return glValue;
		}
	}

	/**
	 * This enum defines the access mode for \value ReadOnly The buffer will be
	 * mapped for reading only. \value WriteOnly The buffer will be mapped for
	 * writing only. \value ReadWrite The buffer will be mapped for reading and
	 * writing.
	 */

	public enum GL_ACCESS {

		READ_ONLY(GL4.GL_READ_ONLY, GL4.GL_MAP_READ_BIT), //
		WRITE_ONLY(GL4.GL_WRITE_ONLY, GL4.GL_MAP_WRITE_BIT), //
		READ_WRITE(GL4.GL_READ_WRITE, -1); //

		protected int glValue;
		protected int glMapValue;

		GL_ACCESS(int value, int mapvalue) {
			this.glValue = value;
			this.glMapValue = mapvalue;
		}

		public int glValue() {
			return glValue;
		}

		public int glMapValue() {
			return glMapValue;
		}
	};

	public static GlBuffer newGLBuffer(final GL4 gl, GL_TYPE type, GL_USAGE usage) {
		GlBuffer gpubuffer = new GlBuffer(type, usage);
		gpubuffer.create(gl);
		return gpubuffer;
	}

	public static GlBuffer hostoGpuData(ByteBuffer bbdataA, GL4 gl) {
		GlBuffer gpuhisto = GlBufferFactory.newGLBuffer(gl, GL_TYPE.ARRAY_BUFFER, GL_USAGE.STATIC_DRAW);
		gpuhisto.bind(gl);
		gpuhisto.allocate(gl, bbdataA, bbdataA.capacity());
		gpuhisto.release(gl);
		return gpuhisto;
	}

	public static ByteBuffer allocate(int nb) {
		return ByteBuffer.allocateDirect(nb).order(ByteOrder.LITTLE_ENDIAN);

	}

	public static ByteBuffer asFloatBuffer(float... values) {
		ByteBuffer bb = allocate(values.length * Float.BYTES);
		bb.asFloatBuffer().put(values);
		bb.flip();
		return bb;
	}

	public static ByteBuffer asIntBuffer(int... values) {
		ByteBuffer bb = allocate(values.length * Integer.BYTES);
		bb.asIntBuffer().put(values);
		bb.flip();
		return bb;
	}

}
