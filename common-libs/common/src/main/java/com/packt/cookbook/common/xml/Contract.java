package com.packt.cookbook.common.xml;



import com.packt.cookbook.common.exceptions.XMLActionException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Contract class
 */
@XStreamAlias("contract")
public class Contract extends BaseAPMessage {

	private Contract() {
	}

	/**
	 * create instance by xml
	 *
	 * @param xml xml
	 * @return resource instance
	 * @throws XMLActionException if can't parse xml to contract
	 */
	public static Contract fromString(String xml) throws XMLActionException {
		try {
			Object obj = xStream.fromXML(xml);
			return (Contract) obj;
		} catch (Exception e) {
			throw new XMLActionException("failed to parse xml to Contract instance", e);
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
			Contract resource = new Contract();
			resource.params = new ArrayList<>();
			for (Entry<String, String> entry : params.entrySet()) {
				Param param = new Param();
				param.setValue(entry.getValue());
				param.setId(entry.getKey());
				resource.params.add(param);
			}
			return xStream.toXML(resource);
		} catch (Exception e) {
			throw new XMLActionException("fail to create xml from map", e);
		}
	}

}
