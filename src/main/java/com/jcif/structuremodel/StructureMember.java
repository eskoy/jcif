package com.jcif.structuremodel;

public interface StructureMember {

	int ordinal();

	default MemberBinaryDefinition getBinaryDefinition() {
		return StructureDefinitionSupport.getBinaryDefinition(this);
	}

	default MemberMemoryDefinition getMemoryDefinition() {
		return StructureDefinitionSupport.getMemoryDefinition(this);
	}

}
