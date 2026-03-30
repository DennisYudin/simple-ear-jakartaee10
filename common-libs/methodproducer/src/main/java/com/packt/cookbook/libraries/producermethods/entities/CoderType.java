package com.packt.cookbook.libraries.producermethods.entities;

public enum CoderType {
	TEST(0),
	SHIFT(1);

	private int num;

	CoderType(int num) {
		this.num = num;
	}
}
