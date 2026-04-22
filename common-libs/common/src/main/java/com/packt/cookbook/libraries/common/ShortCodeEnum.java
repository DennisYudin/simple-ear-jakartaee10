package com.packt.cookbook.libraries.common;

/**
 * This interface used by enum to indicate that it is
 * short code base enum, i.e. each value has it's own unique
 * constant code and can return it.
 */
public interface ShortCodeEnum {

	/**
	 * @return code of enum's value
	 */
	short getCode();
}
