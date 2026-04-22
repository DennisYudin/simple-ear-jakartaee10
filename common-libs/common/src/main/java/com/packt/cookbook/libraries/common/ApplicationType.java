package com.packt.cookbook.libraries.common;

/**
 * type of application
 */
public enum ApplicationType implements ShortCodeEnum {
	/** common art */
	COMMON("common", (short) 0),
	/** article type */
	ARTICLE("article", (short) 1),
	/** transaction type */
	TRANSACTION("transaction", (short) 2),
	/** client type */
	CLIENT("client", (short) 3),
	/** pos type */
	POS("pos", (short) 4),
	/** sms type */
	SMS("sms", (short) 5),
	/** external component */
	EXTERNAL("external", (short) 6),
	/** back office */
	BO("bo", (short) 7);

	private String path;
	private short code;

	ApplicationType(String path, short code) {
		this.path = path;
		this.code = code;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the code
	 */
	@Override
	public short getCode() {
		return code;
	}
}
