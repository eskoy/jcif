package com.jcif.structure.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MemberBinaryDefinition {

	DISPLAY display();

	ENCODING encoding();

	int ordinal();

	String pattern() default "";

}
