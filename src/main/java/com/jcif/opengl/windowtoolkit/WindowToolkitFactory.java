package com.jcif.opengl.windowtoolkit;

import com.jcif.opengl.windowtoolkit.impl.GLSwingPainterController;
import com.jogamp.opengl.awt.GLCanvas;

public class WindowToolkitFactory {

	public static GLPainterController<GLCanvas> newSwingGLPainterController() {

		return new GLSwingPainterController();

	}

}
