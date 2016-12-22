package com.jcif.structurestream.impl;

import java.util.ArrayList;
import java.util.List;

import com.jcif.structuremodel.StructureDataset;
import com.jcif.structurestream.StructureProcess;
import com.jcif.structurestream.StructureStream;

public class StructureStreamImpl implements StructureStream {

	protected StructureDataset structureDataset;

	protected List<StructureProcess> processList = new ArrayList<StructureProcess>();

	public StructureStreamImpl(StructureDataset ds) {
		structureDataset = ds;
	}

	@Override
	public StructureStream process(StructureProcess process) {
		processList.add(process);
		return this;
	}

	@Override
	public void start() {
		for (int i = 0; i < structureDataset.getSize(); i++) {
			for (StructureProcess p : processList) {
				p.process(structureDataset, i);
			}

		}

	}

}
