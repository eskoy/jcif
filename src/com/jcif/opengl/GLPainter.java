package com.jcif.opengl;

import com.jogamp.opengl.GL4;

public interface GLPainter<T> {

	void paint(GL4 gl, T object, int... values);
}
