package frostillicus;

import java.io.*;
import java.util.*;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import lotus.domino.*;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import frostillicus.dxl.AbstractDXLDesignNote;

public class EditorController implements Serializable {
	private static final long serialVersionUID = 2190279771794028971L;

	public String save() {
		try {
			AbstractDXLDesignNote note = (AbstractDXLDesignNote)ExtLibUtil.resolveVariable(ExtLibUtil.getXspContext().getFacesContext(), "designNote");
			String result = note.save();
			if("xsp-success".equals(result)) {
				FNVUtil.toaster("Saved successfully!");
				FNVUtil.dojoPublish("/save/success", "message", note.getUniqueKey());
			}
			return result;
		} catch(Exception e) {
			System.out.println("Some kind of failure! " + e);
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			e.printStackTrace();
			FNVUtil.alert("Exception:\n\n" + w.toString());

			return "xsp-failure";
		}
	}

	@SuppressWarnings("unchecked")
	public void validateFormula(FacesContext facesContext, UIComponent component, Object value) throws NotesException {
		UIInput input = (UIInput)component;

		try {
			Document tempDoc = ExtLibUtil.getCurrentDatabase().createDocument();
			tempDoc.replaceItemValue("Value", value);

			List<Object> result = ExtLibUtil.getCurrentSession().evaluate(" @CheckFormulaSyntax(Value) ", tempDoc);
			if(!"1".equals(result.get(0))) {
				FNVUtil.invalidateField(facesContext, input, "Invalid formula: " + result.get(0));
			}

			tempDoc.recycle();
		} catch(NotesException ne) {
			StringWriter writer = new StringWriter();
			ne.printStackTrace(new PrintWriter(writer));

			FNVUtil.invalidateField(facesContext, input, writer.toString());
		}
	}
	public void validateItemName(FacesContext facesContext, UIComponent component, Object value) {
		if(value != null) {
			String name = value.toString();
			if(name.length() > 0 && name.matches("^[\\w_\\$][\\w\\d_\\$]*$")) {
				return;
			}
		}
		FNVUtil.invalidateField(facesContext, (UIInput)component, "Illegal item name: " + value);

	}
}
