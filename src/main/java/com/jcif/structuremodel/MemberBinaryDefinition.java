package com.jcif.structuremodel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MemberBinaryDefinition {

	DISPLAY display();

	ENCODING encoding();

	int ordinal();

}
