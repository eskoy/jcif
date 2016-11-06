package com.jcif.opengl.windowtoolkit;

import java.util.ArrayList;
import java.util.List;

import com.jcif.opengl.GLPainter;
import com.jcif.opengl.glpainter.backgroung.BackGround;
import com.jcif.opengl.glpainter.backgroung.BackGroundPainter;
import com.jogamp.opengl.GLContext;

public abstract class GLPainterController<V> {

	protected BackGround backGround = new BackGround();
	protected BackGroundPainter defaultBackGroundPainter = new BackGroundPainter();

	protected List<GLPainter<?>> painterList = new ArrayList<GLPainter<?>>();

	public void addPainter(GLPainter<?> painter) {
		this.painterList.add(painter);
	}

	public void removePainter(GLPainter<?> painter) {
		this.painterList.remove(painter);
	}

	public abstract void setSharedContext(GLContext glContext);

	public abstract V getDisplayComponent();

	public abstract void repaint();

	public BackGround getBackGround() {
		return backGround;
	}

}
