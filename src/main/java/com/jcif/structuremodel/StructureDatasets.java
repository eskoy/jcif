package com.jcif.structuremodel;

import com.jcif.structuremodel.impl.ArrayOfStructureDatasetImpl;

public class StructureDatasets {

	public static StructureDataset newArrayOfStructureDatasetImpl(int size, StructureMember... definitions) {

		return new ArrayOfStructureDatasetImpl(size, definitions);
	}

}
