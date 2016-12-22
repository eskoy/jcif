package com.jcif.structurestream;

import com.jcif.structuremodel.StructureDataset;
import com.jcif.structuremodel.StructureMember;

public class MinMaxProcess implements StructureProcess {

	protected StructureMember[] members;
	protected float[][] minMaxs;

	public float[][] getMinMaxs() {
		return minMaxs;
	}

	public MinMaxProcess(StructureMember... m) {
		members = m;
		minMaxs = new float[members.length][2];

		for (float[] minmax : minMaxs) {
			minmax[0] = Float.MAX_VALUE;
			minmax[1] = Float.MIN_VALUE;
		}
	}

	@Override
	public void process(StructureDataset ds, int index) {
		int i = 0;
		for (float[] minmax : minMaxs) {
			minmax[0] = Math.min(minmax[0], ds.getValue(members[i], index));
			minmax[1] = Math.max(minmax[0], ds.getValue(members[i], index));
			i++;
		}

	}

}
