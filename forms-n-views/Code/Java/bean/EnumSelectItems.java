package bean;

import java.io.Serializable;
import java.util.*;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.openntf.domino.utils.Strings;

import com.ibm.xsp.model.DataObject;

import frostillicus.xsp.bean.*;

@ManagedBean(name="enumItems")
@ApplicationScoped
public class EnumSelectItems implements Serializable, DataObject {
	private static final long serialVersionUID = 1L;

	public Class<?> getType(final Object key) {
		return List.class;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getValue(final Object key) {
		if(key == null) {
			throw new NullPointerException("key cannot be null.");
		}

		String className = String.valueOf(key);

		try {
			Class<? extends Enum> enumClass = (Class<? extends Enum>)FacesContext.getCurrentInstance().getContextClassLoader().loadClass(className);

			Enum[] vals = enumClass.getEnumConstants();
			List<SelectItem> result = new ArrayList<SelectItem>(vals.length);
			for(Enum val : vals) {
				SelectItem item = new SelectItem();
				item.setLabel(Strings.toProperCase(val.name()));
				item.setValue(val);
				result.add(item);
			}

			return result;
		} catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public boolean isReadOnly(final Object key) {
		return true;
	}

	public void setValue(final Object key, final Object value) { }
}