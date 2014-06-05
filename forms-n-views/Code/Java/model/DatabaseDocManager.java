package model;

import frostillicus.FNVUtil;
import frostillicus.xsp.bean.*;
import frostillicus.xsp.model.domino.AbstractDominoManager;
import frostillicus.xsp.model.domino.DominoModelList;

@ManagedBean(name="Databases")
@ApplicationScoped
public class DatabaseDocManager extends AbstractDominoManager<DatabaseDoc> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<DatabaseDoc> getModelClass() {
		return DatabaseDoc.class;
	}

	@Override
	protected String getViewPrefix() {
		return "Databases\\";
	}

	@Override
	public DominoModelList<DatabaseDoc> getNamedCollection(final String name, final String category) {
		if("mine".equals(name)) {
			return super.getNamedCollection("By Username", FNVUtil.getSession().getEffectiveUserName());
		}
		return super.getNamedCollection(name, category);
	}

}
