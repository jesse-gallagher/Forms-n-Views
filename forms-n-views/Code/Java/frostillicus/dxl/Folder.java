package frostillicus.dxl;

import java.io.*;
import java.util.*;
import javax.xml.xpath.XPathExpressionException;

import lotus.domino.*;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.raidomatic.xml.*;

import frostillicus.FNVUtil;

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

	public static String create(String databaseDocumentId, String name) throws Exception {
		DxlImporter importer = null;
		try {
			InputStream is = Stylesheet.class.getResourceAsStream("/frostillicus/dxl/folder.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder xmlBuilder = new StringBuilder();
			while(reader.ready()) {
				xmlBuilder.append(reader.readLine());
				xmlBuilder.append("\n");
			}
			is.close();
			String xml = xmlBuilder.toString().replace("name=\"\"", "name=\"" + FNVUtil.xmlEncode(name) + "\"");

			importer = ExtLibUtil.getCurrentSession().createDxlImporter();
			importer.setDesignImportOption(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE);
			importer.setReplicaRequiredForReplaceOrUpdate(false);
			Document databaseDoc = ExtLibUtil.getCurrentDatabase().getDocumentByUNID(databaseDocumentId);
			Database foreignDB = ExtLibUtil.getCurrentSessionAsSignerWithFullAccess().getDatabase(databaseDoc.getItemValueString("Server"), databaseDoc.getItemValueString("FilePath"));
			importer.importDxl(xml, foreignDB);

			Document importedDoc = foreignDB.getDocumentByID(importer.getFirstImportedNoteID());
			return importedDoc.getUniversalID();
		} catch(Exception e) {
			e.printStackTrace();
			if(importer != null) {
				System.out.println(importer.getLog());
			}
		}
		return null;
	}
}
