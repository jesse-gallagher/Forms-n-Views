package frostillicus;
import java.io.Serializable;

import org.openntf.domino.*;
import org.openntf.domino.design.*;

public class DesignAssistant implements Serializable {
	private static final long serialVersionUID = 1L;

	public static String createView(final String databaseDocumentId, final String name) {
		try {
			Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
			DatabaseDesign design = foreignDB.getDesign();
			DesignView newView = design.createView();
			newView.setName(name);
			newView.save();
			return newView.getUniversalID();
		} catch(Exception e) {
			e.printStackTrace();
			FNVUtil.alert(e.getMessage());
		}
		return null;
	}
	public static DesignView getView(final String databaseDocumentId, final String name) {
		Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
		DatabaseDesign design = foreignDB.getDesign();
		return design.getView(name);
	}
}
