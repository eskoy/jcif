package com.jcif.structure.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StructureDefinitionSupport {

	private static Map<StructureMember, MemberBinaryDefinition> memberBinaryDefinitionMap = new HashMap<>();
	private static Map<StructureMember, MemberMemoryDefinition> memberMemoryDefinitionMap = new HashMap<>();

	public static MemberBinaryDefinition getBinaryDefinition(StructureMember key) {
		if (memberBinaryDefinitionMap.get(key) == null) {
			parseAllMemberBinaryDefinition(key);
		}
		return memberBinaryDefinitionMap.get(key);
	}

	public static MemberMemoryDefinition getMemoryDefinition(StructureMember key) {
		if (memberMemoryDefinitionMap.get(key) == null) {
			parseAllMemberMemoryDefinition(key);
		}
		return memberMemoryDefinitionMap.get(key);
	}

	protected static void parseAllMemberMemoryDefinition(StructureMember key) {

		for (Field currentfield : key.getClass().getDeclaredFields()) {

			MemberMemoryDefinition memberMemoryDefinition = currentfield
					.getDeclaredAnnotation(MemberMemoryDefinition.class);
			if (memberMemoryDefinition != null) {
				Object obj;
				try {
					obj = currentfield.get(key);
					if (obj instanceof StructureMember) {

						memberMemoryDefinitionMap.put((StructureMember) obj, memberMemoryDefinition);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {

					e.printStackTrace();
				}

			}
		}
	}

	protected static void parseAllMemberBinaryDefinition(StructureMember key) {

		for (Field currentfield : key.getClass().getDeclaredFields()) {

			MemberBinaryDefinition memberBinaryDefinition = currentfield
					.getDeclaredAnnotation(MemberBinaryDefinition.class);
			if (memberBinaryDefinition != null) {
				Object obj;
				try {
					obj = currentfield.get(key);
					if (obj instanceof StructureMember) {

						memberBinaryDefinitionMap.put((StructureMember) obj, memberBinaryDefinition);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {

					e.printStackTrace();
				}

			}
		}
		;

	}

}
