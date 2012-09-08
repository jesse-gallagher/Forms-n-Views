package com.raidomatic.xml;

import java.io.*;
import java.util.Date;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.XResult;
import com.ibm.commons.xml.XResultUtils.SingleNode;

public class XMLBean implements XResult, Serializable {
	private static final long serialVersionUID = -4568212801990607234L;

	private SingleNode m_xResultHelper;
	/** XML document DOM tree */
	private Document xmlDoc;

	public XMLBean(Document xmlDoc) {
		this.xmlDoc = xmlDoc;
	}

	/**
	 * This method returns an {@link XResult} implementation that will be
	 * used as starting point for the XPath resolver to resolve the XPath expression
	 * language statement
	 * 
	 * @return XResult implementation that represents a single XML node
	 */
	private SingleNode getXResultImpl() {
		if (m_xResultHelper==null) {

			m_xResultHelper=new BasicSingleNode(xmlDoc.getDocumentElement());
		}
		return m_xResultHelper;
	}
	private class BasicSingleNode extends SingleNode implements Serializable {
		private static final long serialVersionUID = -4008386976754296212L;

		public BasicSingleNode(Node node) {
			super(node);
		}

		@Override
		protected String getText(Object node) {
			//dummy implementation needed for getText(); 
			if (node instanceof Node) {
				String sVal = "";
				NodeList childNodes = ((Node)node).getChildNodes();

				for (int i = 0; i < childNodes.getLength(); i++) {
					Node currChildNode = childNodes.item(i);
					if (currChildNode.getNodeType() == Node.TEXT_NODE) {
						sVal = currChildNode.getNodeValue();
					}
				}
				return sVal;
			}
			else
				return "";
		}
	}

	//methods from XResult interface, just pass them over to the SingleNode class implementation

	public boolean getBooleanValue() throws XMLException {
		return getXResultImpl().getBooleanValue();
	}

	public Date getDateValue() throws XMLException {
		return getXResultImpl().getDateValue();
	}

	@SuppressWarnings("unchecked")
	public Iterator getNodeIterator() {
		return getXResultImpl().getNodeIterator();
	}

	public Object[] getNodes() {
		return getXResultImpl().getNodes();
	}

	public double getNumberValue() throws XMLException {
		return getXResultImpl().getNumberValue();
	}

	public Object getSingleNode() throws XMLException {
		return getXResultImpl().getSingleNode();
	}

	public String getStringValue() throws XMLException {
		return getXResultImpl().getStringValue();
	}

	@SuppressWarnings("unchecked")
	public Iterator getValueIterator() {
		return getXResultImpl().getValueIterator();
	}

	public int getValueType() {
		return getXResultImpl().getValueType();
	}

	public String[] getValues() {
		return getXResultImpl().getValues();
	}

	public boolean isEmpty() {
		return getXResultImpl().isEmpty();
	}

	public boolean isMultiple() {
		return getXResultImpl().isMultiple();
	}

	public boolean isValue() {
		return getXResultImpl().isValue();
	}
}
