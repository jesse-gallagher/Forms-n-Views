package frostillicus;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class FNVUtil {
	private FNVUtil() { }

	public static void dojoPublish(String channel, String type, String message) {
		getViewRoot().postScript("dojo.publish(\"" + channel.replace("\"", "\\\"") + "\", [{ type: \"" + type.replace("\"", "\\\"") + "\", message: \"" + message.replace("\"", "\\\"") + "\" }])");
	}
	public static void toaster(String message) {
		dojoPublish("/toaster", "message", message);
	}

	public static void alert(String message) {
		getViewRoot().postScript("XSP.alert(\"" + message.replace("\"", "\\\"").replace("\r\n", "\n").replace("\n", "\\n") + "\")");
	}

	public static void invalidateField(FacesContext facesContext, UIInput input, String errorMessage) {
		input.setValid(false);

		FacesMessage message = new FacesMessage(errorMessage);
		facesContext.addMessage(input.getClientId(facesContext), message);
	}


	private static UIViewRootEx2 getViewRoot() {
		return (UIViewRootEx2)ExtLibUtil.resolveVariable(ExtLibUtil.getXspContext().getFacesContext(), "view");
	}
}
