package frostillicus.dxl;

import java.io.*;

import javax.xml.xpath.XPathExpressionException;

import lotus.domino.*;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.raidomatic.xml.*;

import frostillicus.FNVUtil;

public class View extends Folder {
	private static final long serialVersionUID = -8774232556021141733L;

	public View(String databaseDocumentId, String viewDocumentId) throws Exception {
		super(databaseDocumentId, viewDocumentId);
	}


	public String getSelectionFormula() throws XPathExpressionException {
		XMLNode formula = getDxl().selectSingleNode("/view/code[@event='selection']/formula");
		if(formula != null) {
			return formula.getText();
		}
		return null;
	}
	public void setSelectionFormula(String selectionFormula) throws XPathExpressionException {
		XMLNode formula = getDxl().selectSingleNode("/view/code[@event='selection']/formula");
		if(formula != null) {
			formula.setTextContent(selectionFormula);
		}
	}

	public static String create(String databaseDocumentId, String name) throws Exception {
		DxlImporter importer = null;
		try {
			InputStream is = Stylesheet.class.getResourceAsStream("/dxl/view.xml");
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
