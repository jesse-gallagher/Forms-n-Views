package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;

public class EnumBindingConverter implements Converter {

	@SuppressWarnings("unchecked")
	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String value) {
		ValueBinding binding = component.getValueBinding("value");
		Class<? extends Enum> enumType = binding.getType(facesContext);

		return Enum.valueOf(enumType, value);
	}

	public String getAsString(final FacesContext facesContext, final UIComponent component, final Object value) {
		return String.valueOf(value);
	}

}
