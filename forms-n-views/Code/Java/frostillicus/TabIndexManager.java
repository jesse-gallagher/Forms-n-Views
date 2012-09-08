package frostillicus;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("unused")
public class TabIndexManager implements Serializable {
	private static final long serialVersionUID = -5660266795488183762L;

	private int currentIndex = 1;
	private Map<String, Integer> indexMap = new HashMap<String, Integer>();

	public String getIndex(String databaseDocumentId, String designDocumentId) {
		String key = databaseDocumentId + designDocumentId;
		return key;
		//		if(!indexMap.containsKey(key)) {
		//			indexMap.put(key, this.currentIndex++);
		//		}
		//		return indexMap.get(key);
	}
}
