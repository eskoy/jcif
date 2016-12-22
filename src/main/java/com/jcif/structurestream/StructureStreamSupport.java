package com.jcif.structurestream;

import com.jcif.structuremodel.StructureDataset;
import com.jcif.structurestream.impl.StructureStreamImpl;

public class StructureStreamSupport {

	public static StructureStream stream(StructureDataset ds) {
		return new StructureStreamImpl(ds);
	}
}
