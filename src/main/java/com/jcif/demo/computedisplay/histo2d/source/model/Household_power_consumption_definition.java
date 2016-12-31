package com.jcif.demo.computedisplay.histo2d.source.model;

import com.jcif.structure.model.DISPLAY;
import com.jcif.structure.model.ENCODING;
import com.jcif.structure.model.MemberBinaryDefinition;
import com.jcif.structure.model.MemberMemoryDefinition;
import com.jcif.structure.model.StructureMember;

public enum Household_power_consumption_definition implements StructureMember {

	@MemberBinaryDefinition(display = DISPLAY.DATE, encoding = ENCODING.STRING, ordinal = 0)
	@MemberMemoryDefinition(display = DISPLAY.DATE, encoding = ENCODING.FLOAT, ordinal = 0, structureSize = 288)
	Date, //
	@MemberBinaryDefinition(display = DISPLAY.TIME, encoding = ENCODING.STRING, ordinal = 1)
	@MemberMemoryDefinition(display = DISPLAY.TIME, encoding = ENCODING.FLOAT, ordinal = 1, structureSize = 288)
	Time, //
	@MemberBinaryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.STRING, ordinal = 2)
	@MemberMemoryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.FLOAT, ordinal = 2, structureSize = 288)
	Global_active_power, //
	@MemberBinaryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.STRING, ordinal = 3)
	@MemberMemoryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.FLOAT, ordinal = 3, structureSize = 288)
	Global_reactive_power, //
	@MemberBinaryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.STRING, ordinal = 4)
	@MemberMemoryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.FLOAT, ordinal = 4, structureSize = 288)
	Voltage, //
	@MemberBinaryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.STRING, ordinal = 5)
	@MemberMemoryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.FLOAT, ordinal = 5, structureSize = 288)
	Global_intensity, //
	@MemberBinaryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.STRING, ordinal = 6)
	@MemberMemoryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.FLOAT, ordinal = 6, structureSize = 288)
	Sub_metering_1, //
	@MemberBinaryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.STRING, ordinal = 7)
	@MemberMemoryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.FLOAT, ordinal = 7, structureSize = 288)
	Sub_metering_2, //
	@MemberBinaryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.STRING, ordinal = 8)
	@MemberMemoryDefinition(display = DISPLAY.NUMBER, encoding = ENCODING.FLOAT, ordinal = 8, structureSize = 288)
	Sub_metering_3;
}
