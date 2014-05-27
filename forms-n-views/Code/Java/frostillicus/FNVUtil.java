package frostillicus;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.extlib.util.ExtLibUtil;

import org.openntf.domino.*;
import org.openntf.domino.utils.XSPUtil;

public enum FNVUtil {
	;

	public static Session getSession() {
		return XSPUtil.getCurrentSession();
	}
	public static Session getSessionAsSigner() {
		return XSPUtil.getCurrentSessionAsSigner();
	}
	public static Session getSessionAsSignerWithFullAccess() {
		return XSPUtil.getCurrentSessionAsSignerWithFullAccess();
	}
	public static Database getDatabase() {
		return XSPUtil.getCurrentDatabase();
	}

	public static Database getDatabaseForDocumentId(final String databaseDocumentId) {
		Document databaseDoc = FNVUtil.getDatabase().getDocumentByUNID(databaseDocumentId);
		Database foreignDB = FNVUtil.getSessionAsSignerWithFullAccess().getDatabase(databaseDoc.getItemValueString("Server"), databaseDoc.getItemValueString("FilePath"));
		return foreignDB;
	}


	public static Object resolveVariable(final String name) {
		return ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), name);
	}

	public static void dojoPublish(final String channel, final String type, final String message) {
		getViewRoot().postScript("dojo.publish(\"" + channel.replace("\"", "\\\"") + "\", [{ type: \"" + type.replace("\"", "\\\"") + "\", message: \"" + message.replace("\"", "\\\"") + "\" }])");
	}
	public static void toaster(final String message) {
		dojoPublish("/toaster", "message", message);
	}

	public static void alert(final String message) {
		getViewRoot().postScript("XSP.alert(\"" + message.replace("\"", "\\\"").replace("\r\n", "\n").replace("\n", "\\n") + "\")");
	}

	public static void invalidateField(final FacesContext facesContext, final UIInput input, final String errorMessage) {
		input.setValid(false);

		FacesMessage message = new FacesMessage(errorMessage);
		facesContext.addMessage(input.getClientId(facesContext), message);
	}


	private static UIViewRootEx2 getViewRoot() {
		return (UIViewRootEx2)resolveVariable("view");
	}
	public static String xmlEncode(final String text) {
		StringBuilder result = new StringBuilder();

		for(int i = 0; i < text.length(); i++) {
			char currentChar = text.charAt(i);
			if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= '0' && currentChar <= '9'))) {
				result.append("&#" + (int)currentChar + ";");
			} else {
				result.append(currentChar);
			}
		}

		return result.toString();
	}
}
