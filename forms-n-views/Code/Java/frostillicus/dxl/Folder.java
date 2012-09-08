package frostillicus.dxl;

import java.util.*;
import javax.xml.xpath.XPathExpressionException;
import com.raidomatic.xml.*;

public class Folder extends AbstractDXLDesignNote {
	private static final long serialVersionUID = -8774232556021141733L;

	public Folder(String databaseDocumentId, String designDocumentId) throws Exception {
		super(databaseDocumentId, designDocumentId);
	}

	public List<Column> getColumns() throws XPathExpressionException {
		List<XMLNode> columnNodes = getDxl().selectNodes("//column");
		List<Column> result = new ArrayList<Column>(columnNodes.size());
		for(XMLNode columnNode : columnNodes) {
			result.add(new Column(columnNode));
		}
		return result;
	}
	public void addColumn() throws XPathExpressionException {
		// Create the column node and set the defaults
		// Make sure to add the node before any items
		XMLNode node;
		XMLNode item = this.getRootNode().selectSingleNode("//item");
		if(item != null) {
			node = this.getRootNode().insertChildElementBefore("column", item);
		} else {
			node = this.getRootNode().addChildElement("column");
		}

		node.setAttribute("hidedetailrows", "false");
		node.setAttribute("width", "10");
		node.setAttribute("resizable", "true");
		node.setAttribute("separatemultiplevalues", "false");
		node.setAttribute("sortnoaccent", "false");
		node.setAttribute("sortnocase", "true");
		node.setAttribute("showaslinks", "false");
	}
	public void removeColumn(int index) throws XPathExpressionException {
		List<XMLNode> columnNodes = getDxl().selectNodes("//column");
		columnNodes.remove(index);
	}
	public void swapColumns(int a, int b) throws XPathExpressionException {
		XMLNodeList columnNodes = (XMLNodeList)getDxl().selectNodes("//column");
		columnNodes.swap(a, b);
	}
}
