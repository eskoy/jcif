package com.jcif.structure.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MemberMemoryDefinition {

	DISPLAY display();

	String displayPattern() default "";

	ENCODING encoding();

	int encodingSize() default -1;

	int ordinal();

	int structureSize();

	BYTEORDER order() default BYTEORDER.LITTLE_ENDIAN;

}
