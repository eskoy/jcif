package com.jcif.demo.computedisplay.histo2d.chart.background;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcif.awt.ColorUtil;
import com.jcif.demo.geo.PolyUtil;
import com.jcif.demo.geo.polyline.PolylineShapeFileUtil;
import com.jcif.opengl.GLPainter;
import com.jcif.opengl.glpainter.grid.Grid;
import com.jcif.opengl.glpainter.grid.GridPainter;
import com.jcif.opengl.glpainter.poly.Poly;
import com.jcif.opengl.glpainter.poly.PolyLinePainter;

public class BackGroundCtrl {

	private final static Logger logger = LoggerFactory.getLogger(BackGroundCtrl.class);

	public static String MAP_FILE_PATH = "10m_coastline.shp";

	protected GridPainter gridPainter = new GridPainter();

	protected Grid grid = new Grid();

	protected GLPainter<?> worldcoaslinepainter;

	public BackGroundCtrl() {
		init();
	}

	protected void init() {

		this.updateGrid(8, Color.CYAN);
		this.worldcoaslinepainter = createWorldCoaslineLayer();
	}

	public GridPainter getGridPainter() {
		return gridPainter;
	}

	public GLPainter<?> getWorldcoaslinepainter() {
		return worldcoaslinepainter;
	}

	protected GLPainter<?> createWorldCoaslineLayer() {

		List<ByteBuffer> polygonBuffer = new ArrayList<>();
		List<Poly> polygonList = new ArrayList<>();
		PolylineShapeFileUtil.loadPolygon(polygonBuffer, MAP_FILE_PATH);

		PolyUtil.normalize(polygonBuffer);

		int paletteSize = 2;
		Color[] color = ColorUtil.computeRandomColor(paletteSize);

		int i = 0;
		for (ByteBuffer current : polygonBuffer) {
			Poly poly = new Poly();
			poly.setColor(color[i++ % paletteSize]);
			poly.setSize(poly.getSize());
			poly.setPolygon(current);
			polygonList.add(poly);
		}

		PolyLinePainter painter = new PolyLinePainter();
		painter.update(polygonList);

		return painter;
	}

	public void updateGrid(int nb, Color col) {
		grid.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 60));
		grid.setStep(nb);
		gridPainter.update(grid);
	}

}
