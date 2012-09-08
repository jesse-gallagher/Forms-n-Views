package frostillicus.dxl;

import javax.xml.xpath.XPathExpressionException;
import com.raidomatic.xml.*;

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


}
