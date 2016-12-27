package com.jcif.structure.process;

import java.nio.FloatBuffer;

import com.jcif.structure.model.StructureDataset;
import com.jcif.structure.model.StructureMember;

public class NormalizeProcess implements StructureProcess {

	protected StructureMember member;
	protected float[] minMax;
	protected FloatBuffer normalizedata;

	public NormalizeProcess(StructureMember m, float[] mm, FloatBuffer fb) {
		member = m;
		minMax = mm;
		normalizedata = fb;

	}

	@Override
	public void process(StructureDataset ds, int index) {

		// from min max to -1 1
		float nv = ((ds.getValue(member, index) - minMax[0]) / (minMax[1] - minMax[0]) * 2) - 1;

		normalizedata.put(index, nv);

	}

}
