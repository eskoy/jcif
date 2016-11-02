package com.jcif.opengl;

import com.jogamp.opengl.GL4;

public interface GLPainter<T> {

	void update(T t);

	void init(GL4 gl);

	void paint(GL4 gl, int... values);
}
