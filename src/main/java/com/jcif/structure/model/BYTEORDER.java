package com.jcif.structure.model;

import java.nio.ByteOrder;

public enum BYTEORDER {

	LITTLE_ENDIAN(ByteOrder.LITTLE_ENDIAN), BIG_ENDIAN(ByteOrder.BIG_ENDIAN);

	private BYTEORDER(ByteOrder o) {
		order = o;
	}

	private ByteOrder order;

	ByteOrder order() {
		return order;
	}
}
