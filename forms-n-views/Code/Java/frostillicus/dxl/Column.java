package frostillicus.dxl;

import java.io.Serializable;
import javax.xml.xpath.XPathExpressionException;
import org.openntf.domino.utils.xml.XMLNode;

public class Column implements Serializable {
	private static final long serialVersionUID = -8936612099881966324L;

	private XMLNode node;

	public Column(final XMLNode node) {
		this.node = node;
	}

	public String getResizableString() { return String.valueOf(this.isResizable()); }
	public void setResizableString(final String resizable) { this.setResizable(Boolean.valueOf(resizable)); }
	public boolean isResizable() { return this.node.getAttribute("resizable").equals("true"); }
	public void setResizable(final boolean resizable) { this.node.setAttribute("resizable", String.valueOf(resizable)); }

	public String getSeparateMultipleValuesString() { return String.valueOf(this.isSeparateMultipleValues()); }
	public void setSeparateMultipleValuesString(final String separateMultipleValues) { this.setSeparateMultipleValues(Boolean.valueOf(separateMultipleValues)); }
	public boolean isSeparateMultipleValues() { return this.node.getAttribute("separatemultiplevalues").equals("true"); }
	public void setSeparateMultipleValues(final boolean separateMultipleValues) { this.node.setAttribute("separatemultipleValues", String.valueOf(separateMultipleValues)); }

	public String getSortNoAccentString() { return String.valueOf(this.isSortNoAccent()); }
	public void setSortNoAccentString(final String sortNoAccent) { this.setSortNoAccent(Boolean.valueOf(sortNoAccent)); }
	public boolean isSortNoAccent() { return this.node.getAttribute("sortnoaccent").equals("true"); }
	public void setSortNoAccent(final boolean sortNoAccent) { this.node.setAttribute("sortnoaccent", String.valueOf(sortNoAccent)); }

	public String getSortNoCaseString() { return String.valueOf(this.isSortNoCase()); }
	public void setSortNoCaseString(final String sortNoCase) { this.setSortNoCase(Boolean.valueOf(sortNoCase)); }
	public boolean isSortNoCase() { return this.node.getAttribute("sortnocase").equals("true"); }
	public void setSortNoCase(final boolean sortNoCase) { this.node.setAttribute("sortnocase", String.valueOf(sortNoCase)); }

	public String getShowAsLinksString() { return String.valueOf(this.isShowAsLinks()); }
	public void setShowAsLinksString(final String showAsLinks) { this.setShowAsLinks(Boolean.valueOf(showAsLinks)); }
	public boolean isShowAsLinks() { return this.node.getAttribute("showaslinks").equals("true"); }
	public void setShowAsLinks(final boolean showAsLinks) { this.node.setAttribute("showaslinks", showAsLinks ? "true" : "false"); }

	public String getItemName() { return this.node.getAttribute("itemname"); }
	public void setItemName(final String itemName) { this.node.setAttribute("itemname", itemName); }

	public String getSort() { return this.node.getAttribute("sort"); }
	public void setSort(final String sort) { this.node.setAttribute("sort", sort); }

	public String getTitle() {
		XMLNode columnHeader = null;
		columnHeader = this.node.selectSingleNode("columnheader");
		if(columnHeader == null) {
			return "";
		}
		return columnHeader.getAttribute("title");
	}
	public void setTitle(final String title) {

		XMLNode columnHeader = null;
		columnHeader = this.node.selectSingleNode("columnheader");
		if(columnHeader == null) {
			this.node.addChildElement("columnheader");
		}
		columnHeader.setAttribute("title", title);
	}

	public String getFormula() throws XPathExpressionException {
		XMLNode formulaNode = this.node.selectSingleNode("code[@event='value']/formula");
		if(formulaNode != null) {
			return formulaNode.getTextContent();
		}

		// If there's no formula node then that means it's just an item directly
		return this.getItemName();
	}
	public void setFormula(final String formula) throws XPathExpressionException {
		XMLNode formulaNode = this.node.selectSingleNode("code[@event='value']/formula");
		if(formulaNode != null) {
			formulaNode.setTextContent(formula);
		} else {
			XMLNode code = this.node.addChildElement("code");
			code.setAttribute("event", "value");
			formulaNode = code.addChildElement("formula");
			formulaNode.setTextContent(formula);
		}
	}
}