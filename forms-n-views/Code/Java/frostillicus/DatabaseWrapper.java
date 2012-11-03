package frostillicus;

import java.util.*;

public class DatabaseWrapper {
	@SuppressWarnings("unused")
	private final String documentId;

	public DatabaseWrapper(String documentId) {
		this.documentId = documentId;
	}

	public SortedSet<Map<String, Object>> getViews() {
		return null;
	}
}
