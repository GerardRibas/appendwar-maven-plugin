/**
 * 
 */
package es.gerardribas.appendwar;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Gerard
 * 
 */
public final class WebXmlAppenderTool {

	private static WebXmlAppenderTool instance;

	private WebXmlAppenderTool() {
	}

	public static WebXmlAppenderTool getInstance() {
		if (instance == null) {
			instance = new WebXmlAppenderTool();
		}
		return instance;
	}

	public File appendWebXml(File projectWebXml, File baseTemplateProjectXml,
			File finalWebXml, Log log) throws ParserConfigurationException,
			SAXException, IOException, TransformerException {
		log.debug("Start appendWebXml");
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document applicationDocument = docBuilder.parse(projectWebXml);
		Document baseTemplateDocument = docBuilder
				.parse(baseTemplateProjectXml);
		Node webAppApplicationWebElement = applicationDocument.getFirstChild();
		log.debug("Appending childs from webXml to ParentXml");
		for (int i = 0; i < webAppApplicationWebElement.getChildNodes()
				.getLength(); i++) {
			Node childNode = webAppApplicationWebElement.getChildNodes()
					.item(i);
			Node toImportNode = baseTemplateDocument
					.importNode(childNode, true);
			baseTemplateDocument.getFirstChild().appendChild(toImportNode);
			log.debug("Node=" + toImportNode.getNodeName() + " appended");
		}
		log.debug("End appending childs from webXml to ParentXml");
		writeToFile(baseTemplateDocument, finalWebXml, log);
		log.debug("End appendWebXml");
		return finalWebXml;
	}

	private void writeToFile(Document documentToWrite, File destFile, Log log)
			throws TransformerException {
		log.debug("Start writeToFile");
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(documentToWrite);
		StreamResult result = new StreamResult(destFile);
		transformer.transform(source, result);
		log.debug("End writeToFile");
	}

}
