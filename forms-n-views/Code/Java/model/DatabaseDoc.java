package model;

import java.util.Collection;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.ViewEntry;

import frostillicus.FNVUtil;
import frostillicus.xsp.model.domino.AbstractDominoModel;
import frostillicus.xsp.model.domino.DominoColumnInfo;

public class DatabaseDoc extends AbstractDominoModel {
	private static final long serialVersionUID = 1L;

	public DatabaseDoc(final Database database) {
		super(database);
		setValue("Form", "Database");
	}

	public DatabaseDoc(final Document doc) {
		super(doc);
	}

	public DatabaseDoc(final ViewEntry entry, final List<DominoColumnInfo> columnInfo) {
		super(entry, columnInfo);
	}

	@Override
	protected Collection<String> nonSummaryFields() {
		return null;
	}

	public Database getDatabase() {
		return FNVUtil.getDatabaseForDocumentId(this.getId());
	}
}
