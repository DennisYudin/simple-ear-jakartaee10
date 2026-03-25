package com.packt.cookbook.libraries.coder;

public enum CoderType {
	TEST(0),
	SHIFT(1);

	private int num;

	CoderType(int num) {
		this.num = num;
	}
}
