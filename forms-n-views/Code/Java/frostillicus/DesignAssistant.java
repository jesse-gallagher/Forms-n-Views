package frostillicus;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.openntf.domino.*;
import org.openntf.domino.design.*;

public class DesignAssistant implements Serializable {
	private static final long serialVersionUID = 1L;

	public static String createElement(final String databaseDocumentId, final String name, final String type) {
		try {
			Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
			DatabaseDesign design = foreignDB.getDesign();

			Method createElementMethod = design.getClass().getMethod("create" + type);
			Object newElement = createElementMethod.invoke(design);
			Method setNameMethod = newElement.getClass().getMethod("setName", String.class);
			setNameMethod.invoke(newElement, name);
			Method saveMethod = newElement.getClass().getMethod("save");
			saveMethod.invoke(newElement);
			Method getUniversalIDMethod = newElement.getClass().getMethod("getUniversalID");
			return (String)getUniversalIDMethod.invoke(newElement);

		} catch(Exception e) {
			e.printStackTrace();
			FNVUtil.alert(e.getMessage());
		}
		return null;
	}
	public static DesignBase getElement(final String databaseDocumentId, final String name, final String type) {
		try {
			Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
			DatabaseDesign design = foreignDB.getDesign();

			Method getElementMethod = design.getClass().getMethod("get" + type, String.class);
			return (DesignBase)getElementMethod.invoke(design, name);
		} catch(Exception e) {
			e.printStackTrace();
			FNVUtil.alert(e.getMessage());
		}
		return null;
	}

	public static String createView(final String databaseDocumentId, final String name) {
		try {
			Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
			DatabaseDesign design = foreignDB.getDesign();
			DesignView newElement = design.createView();
			newElement.setName(name);
			newElement.save();
			return newElement.getUniversalID();
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

	public static String createFolder(final String databaseDocumentId, final String name) {
		try {
			Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
			DatabaseDesign design = foreignDB.getDesign();
			Folder newElement = design.createFolder();
			newElement.setName(name);
			newElement.save();
			return newElement.getUniversalID();
		} catch(Exception e) {
			e.printStackTrace();
			FNVUtil.alert(e.getMessage());
		}
		return null;
	}
	public static Folder getFolder(final String databaseDocumentId, final String name) {
		Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
		DatabaseDesign design = foreignDB.getDesign();
		return design.getFolder(name);
	}

	public static String createStyleSheet(final String databaseDocumentId, final String name) {
		try {
			Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
			DatabaseDesign design = foreignDB.getDesign();
			StyleSheet newElement = design.createStyleSheet();
			newElement.setName(name);
			newElement.save();
			return newElement.getUniversalID();
		} catch(Exception e) {
			e.printStackTrace();
			FNVUtil.alert(e.getMessage());
		}
		return null;
	}
	public static StyleSheet getStyleSheet(final String databaseDocumentId, final String name) {
		Database foreignDB = FNVUtil.getDatabaseForDocumentId(databaseDocumentId);
		DatabaseDesign design = foreignDB.getDesign();
		return design.getStyleSheet(name);
	}
}
