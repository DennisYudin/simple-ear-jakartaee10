package com.mobimoney.pcl.configuration;


import com.packt.cookbook.libraries.common.StringHelper;
import com.packt.cookbook.libraries.common.configuration.AbstractConfiguration;
import com.packt.cookbook.libraries.common.logging.Log4jHelper;
import com.packt.cookbook.libraries.common.logging.LogHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides functionality of reading components configuration from XML config file
 */
public class ConfiguratorImpl implements Configurator {

	private static final LogHelper LOG = Log4jHelper.getLogger(ConfiguratorImpl.class);

	private final Object NULL = new Object();

	/**
	 * DOM representation of XML configuration
	 */
	protected volatile Document configurationSource;

	/**
	 * Components configuration already read cache
	 */
	protected Map<Object, Object> configurations = new ConcurrentHashMap<>();

	private void clear() {
		configurations.clear();
	}

	/**
	 * Initializing configurator from input stream of XML-configuration
	 *
	 * @param fw - input streams of configuration files
	 */
	public ConfiguratorImpl(ConfigurationFileWrapper... fw) {
		init(fw);
	}


	@Override
	public void init(ConfigurationFileWrapper... fw) {
		String currentFile = null;
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			currentFile = fw[0].getFileName();
			Document source = documentBuilder.parse(fw[0].getInputStream());

			for (int i = 1; i < fw.length; i++) {
				currentFile = fw[i].getFileName();
				if (fw[i].getInputStream() == null) {
					continue;
				}
				Document doc = documentBuilder.parse(fw[i].getInputStream());
				NodeList nodes = doc.getDocumentElement().getChildNodes();
				for (int j = 0; j < nodes.getLength(); j++) {
					Node node = source.importNode(nodes.item(j), true);
					source.getDocumentElement().appendChild(node);
				}
			}
			currentFile = null;

			//create empty document 
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument();

			//append to new document empty root tag of source document
			Node root = doc.importNode(source.getDocumentElement(), false);
			doc.appendChild(root);

			//merge into new document tags of source document
			mergeRoot(doc, doc.getDocumentElement(), source.getDocumentElement().getChildNodes());

			configurationSource = doc;
			clear();

		} catch (Exception e) {
			String msg = StringHelper.isEmpty(currentFile) ? "Configurator initialization error" : "Configurator initialization error. Check file: " + currentFile;
			LOG.error(msg, e);
			throw new RuntimeException(msg + ": " + e.getMessage(), e);
		} finally {
			for (ConfigurationFileWrapper wrapper : fw) {
				try {
					if (wrapper.getInputStream() != null) {
						wrapper.getInputStream().close();
					}
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}

	/**
	 * Initializing configurator from XML file with name and path specified
	 *
	 * @param filePaths - paths to configuration XML files
	 */
	public ConfiguratorImpl(String... filePaths) {
		this(getIss(filePaths));
	}

	private static ConfigurationFileWrapper[] getIss(String... filePaths) {
		ConfigurationFileWrapper[] wrappers = new ConfigurationFileWrapper[filePaths.length];

		for (int i = 0; i < filePaths.length; i++) {
			try {
				ConfigurationFileWrapper fw = new ConfigurationFileWrapper(new FileInputStream(filePaths[i]), filePaths[i]);
				wrappers[i] = fw;
			} catch (FileNotFoundException e) {
				LOG.warn("Error reading config file, skip: " + filePaths[i], e);
			}
		}
		return wrappers;
	}

	private static void mergeNodes(Document doc, Element target, NodeList list) {
		Map<String, List<Node>> map = new HashMap<>();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			List<Node> nodes = map.computeIfAbsent(node.getNodeName(), k -> new ArrayList<>());
			nodes.add(node);
		}

		map.forEach((name, nodes) -> {
			if (nodes.size() > 1) {
				for (Node node : nodes) {
					target.appendChild(doc.importNode(node, true));
				}
			} else {
				processNode(doc, target, nodes.get(0));
			}
		});
	}

	private static void mergeRoot(Document doc, Element target, NodeList list) {
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			processNode(doc, target, node);
		}
	}

	/**
	 * Returns string representation of DOM element with its child recursively
	 *
	 * @param node - node to be printed
	 * @return
	 */
	private static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			LOG.debug("nodeToString Transformer Exception", te);
		}
		return sw.toString();
	}

	private static void processNode(Document doc, Element target, Node node) {
		NodeList listByName = target.getElementsByTagName(node.getNodeName());
		if (listByName.getLength() > 0) {
			mergeNodes(doc, (Element) listByName.item(0), node.getChildNodes());
		} else {
			target.appendChild(doc.importNode(node, true));
		}
	}

	/**
	 * @see com.mobimoney.pcl.configuration.Configurator#getConfiguration(java.lang.Class)
	 */
	@Override
	public <T extends AbstractConfiguration> T getConfiguration(Class<T> clazz) {
		return getConfiguration(clazz, null);
	}

	private <T extends AbstractConfiguration> T checkNReturn(Class<T> clazz, Object conf, T defaultConf) {
		if (conf == NULL) {
			LOG.info("Configuration for '" + clazz + "' not found. Default configuration will be used.");
			return defaultConf;
		}
		return clazz.cast(conf);
	}


	@Override
	public <T extends AbstractConfiguration> T getConfiguration(Class<T> clazz, T defaultConf) {
		// configuration part will be found in config file by class name
		String configurationName = clazz.getSimpleName().intern(); // get canonical string, it's can be used for synchronization

		//check if this configuration was already read
		Object candidate = configurations.get(configurationName);
		if (candidate != null) {
			return checkNReturn(clazz, candidate, defaultConf);
		}

		synchronized (configurationName) {
			candidate = configurations.get(configurationName); // double-check
			if (candidate != null) {
				return checkNReturn(clazz, candidate, defaultConf);
			}

			T conf = null;

			StringReader sr = null;
			try {
				// reading configuration from string
				NodeList elements = configurationSource.getElementsByTagName(configurationName);
				if (elements.getLength() != 1) {
					throw new ConfiguratorException("Cannot found exactly one configuration tag '" + configurationName
							+ "' in configuration file.");
				}

				sr = new StringReader(nodeToString(elements.item(0)));
				conf = JAXB.unmarshal(sr, clazz);
			} catch (Exception e) {
				LOG.warn("Error reading configuration for '" + clazz + "'. Default configuration will be used. "
						+ e.getMessage());
			} finally {
				try {
					if (sr != null) {
						sr.close();
					}
				} catch (Exception e) {
				}
			}

			// saving read configuration
			if (conf == null) {
				configurations.put(configurationName, NULL);
				return defaultConf;
			}
			configurations.put(configurationName, conf);
			return conf;
		}
	}

	/**
	 * @see com.mobimoney.pcl.configuration.Configurator#getConfigurationAsStream(java.lang.String)
	 */
	@Override
	public <T extends AbstractConfiguration> InputStream getConfigurationAsStream(String configurationTagName) {
		try {
			// reading configuration from string
			NodeList elements = configurationSource.getElementsByTagName(configurationTagName);
			if (elements.getLength() != 1) {
				throw new ConfiguratorException("Cannot found exactly one configuration tag '" + configurationTagName
						+ "' in configuration file.");
			}

			String nodeToString = nodeToString(elements.item(0));
			String xmlEncoding = configurationSource.getXmlEncoding();
			if (xmlEncoding == null) {
				xmlEncoding = "UTF-8";
			}
			return new ByteArrayInputStream(nodeToString.getBytes(xmlEncoding));
		} catch (Exception e) {
			LOG.warn("Error reading configuration for '" + configurationTagName + "'. " + e.getMessage());
		}
		return null;
	}

	/**
	 * Returns read or default configuration in XML format
	 */
	@Override
	public String toString() {
		return nodeToString(configurationSource);
	}
}
