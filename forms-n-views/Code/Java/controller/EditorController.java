package controller;

import java.io.*;
import java.util.*;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.openntf.domino.*;
import org.openntf.domino.design.*;

import frostillicus.FNVUtil;
import frostillicus.controller.BasicXPageController;

public class EditorController extends BasicXPageController {
	private static final long serialVersionUID = 1L;

	public String save() {
		try {
			DesignBase note = (DesignBase)FNVUtil.resolveVariable("designNote");
			note.reattach(FNVUtil.getDatabaseForDocumentId((String)FNVUtil.resolveVariable("databaseDocumentId")));
			boolean result = note.save();
			if(result) {
				FNVUtil.toaster("Saved successfully!");
				FNVUtil.dojoPublish("/save/success", "message", (String)FNVUtil.resolveVariable("uniqueKey"));
			}
			return result ? "xsp-success" : "xsp-failure";
		} catch(Exception e) {
			System.out.println("Some kind of failure! " + e);
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			e.printStackTrace();
			FNVUtil.alert("Exception:\n\n" + w.toString());

			return "xsp-failure";
		}
	}

	public void validateFormula(final FacesContext facesContext, final UIComponent component, final Object value) {
		UIInput input = (UIInput)component;

		try {
			Document tempDoc = FNVUtil.getDatabase().createDocument();
			tempDoc.replaceItemValue("Value", value);

			List<Object> result = FNVUtil.getSession().evaluate(" @CheckFormulaSyntax(Value) ", tempDoc);
			if(!"1".equals(result.get(0))) {
				FNVUtil.invalidateField(facesContext, input, "Invalid formula: " + result.get(0));
			}
		} catch(Exception ne) {
			StringWriter writer = new StringWriter();
			ne.printStackTrace(new PrintWriter(writer));

			FNVUtil.invalidateField(facesContext, input, writer.toString());
		}
	}
	public void validateItemName(final FacesContext facesContext, final UIComponent component, final Object value) {
		if(value != null) {
			String name = value.toString();
			if(name.length() > 0 && name.matches("^[\\w_\\$][\\w\\d_\\$]*$")) {
				return;
			}
		}
		FNVUtil.invalidateField(facesContext, (UIInput)component, "Illegal item name: " + value);

	}
}
