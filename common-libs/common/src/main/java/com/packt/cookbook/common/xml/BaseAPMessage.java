package com.packt.cookbook.common.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.util.List;

/**
 * base article provider message
 */
public class BaseAPMessage {

	private static class CustomizedXStream extends XStream {

		/**
		 * Instantiates a new Customized x stream.
		 *
		 * @param driver the hierarchical stream driver
		 */
		public CustomizedXStream(DomDriver driver) {
			super(driver);
		}

		@Override
		public Object fromXML(String xml) {
			return super.fromXML(xml.trim());
		}
	}

	/**
	 * param class
	 */
	@XStreamAlias("param")
	public static class Param {

		private String label;
		private String id;
		private String ref;

		private String param;

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * @return the reference
		 */
		public String getReference() {
			return ref;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return param;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @param label the label to set
		 */
		public void setLabel(String label) {
			this.label = label;
		}

		/**
		 * @param reference the reference to set
		 */
		public void setReference(String reference) {
			this.ref = reference;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.param = value;
		}

	}

	/**
	 * converter for param
	 */
	protected static class ParamConverter implements Converter {

		@Override
		public boolean canConvert(Class theClass) {
			return theClass.equals(Param.class);
		}

		@Override
		public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
			Param param = (Param) object;
			safetyAddAttribute(LABEL, param.getLabel(), writer);
			safetyAddAttribute(ID, param.getId(), writer);
			safetyAddAttribute(REFERENCE, param.getReference(), writer);
			if (param.getValue() != null) {
				writer.setValue(param.getValue());
			}
		}

		@Override
		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			Param param = new Param();
			param.setValue(reader.getValue());
			param.setId(reader.getAttribute(ID));
			param.setLabel(reader.getAttribute(LABEL));
			param.setReference(reader.getAttribute(REFERENCE));
			return param;
		}

		private void safetyAddAttribute(String name, Object object, HierarchicalStreamWriter writer) {
			if (object != null) {
				writer.addAttribute(name, object.toString());
			}
		}
	}

	private static final String LABEL = "label";
	private static final String ID = "id";
	private static final String REFERENCE = "ref";

	/**
	 * params
	 */
	@XStreamImplicit(itemFieldName = "param")
	protected List<Param> params;

	/**
	 * xstream
	 */
	@XStreamOmitField
	protected static final XStream xStream = createXStream();

	/**
	 * @return the params
	 */
	public List<Param> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(List<Param> params) {
		this.params = params;
	}

	/**
	 * @param id id of item
	 * @return value or null
	 */
	public String getValue(String id) {
		if (params == null) {
			return null;
		}
		for (Param param : params) {
			if (param.getId().equals(id)) {
				return param.getValue();
			}
		}
		return null;
	}

	/**
	 * Factory method for custom XStream
	 */
	protected static XStream createXStream() {
		XStream xs = new CustomizedXStream(new DomDriver()) {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {
					@Override
					public boolean shouldSerializeMember(Class definedIn, String fieldName) {
						try {
							return definedIn != Object.class || realClass(fieldName) != null;
						} catch (CannotResolveClassException cnrce) {
							return false;
						}
					}
				};
			}
		};
		xs.processAnnotations(new Class<?>[]{Contract.class, Resource.class, Error.class});
		xs.registerConverter(new ParamConverter());
		xs.allowTypesByWildcard(new String[]{"com.packt.cookbook.common.xml.**",
		});
		return xs;
	}
}