package com.packt.cookbook.common.xml;



import com.packt.cookbook.common.exceptions.XMLActionException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Error
 */
@XStreamAlias("error")
public class Error extends BaseAPMessage {

	private Error() {
	}

	/**
	 * create instance by xml
	 *
	 * @param xml xml
	 * @return error instance
	 * @throws XMLActionException if can't parse xml to
	 */
	public static Error fromString(String xml) throws XMLActionException {
		try {
			Object obj = xStream.fromXML(xml);
			return (Error) obj;
		} catch (Exception e) {
			throw new XMLActionException("failed to parse xml to Error instance", e);
		}
	}

	/**
	 * convert params to contract xml
	 *
	 * @param params map with params id - value
	 * @return string with xml
	 * @throws XMLActionException if fail to create xml
	 */
	public static String toXML(Map<String, String> params) throws XMLActionException {
		if (params == null || params.isEmpty()) {
			return null;
		}
		try {
			Error error = new Error();
			error.params = new ArrayList<>();
			for (Entry<String, String> entry : params.entrySet()) {
				Param param = new Param();
				param.setValue(entry.getValue());
				param.setId(entry.getKey());
				error.params.add(param);
			}
			return xStream.toXML(error);
		} catch (Exception e) {
			throw new XMLActionException("fail to create xml from map", e);
		}
	}
}
