package com.jcif.structuremodel;

public interface StructureDataset {

	float getValue(StructureMember member, int index);

	void setValue(StructureMember member, int index, float value);

	String getDisplayValue(StructureMember member, int index);

}
