package frostillicus;

import java.util.*;

public class DatabaseManager implements Map<String, DatabaseWrapper> {

	public DatabaseWrapper get(Object key) {
		if(!(key instanceof String)) { throw new IllegalArgumentException(); }
		String databaseDocumentId = (String)key;

		return new DatabaseWrapper(databaseDocumentId);
	}

	public void clear() { }
	public boolean containsKey(Object key) { return false; }
	public boolean containsValue(Object value) {  return false; }
	public Set<java.util.Map.Entry<String, DatabaseWrapper>> entrySet() { return null; }
	public boolean isEmpty() {  return false; }
	public Set<String> keySet() { return null; }
	public DatabaseWrapper put(String key, DatabaseWrapper value) { return null; }
	public void putAll(Map<? extends String, ? extends DatabaseWrapper> m) { }
	public DatabaseWrapper remove(Object key) { return null; }
	public int size() { return 0; }
	public Collection<DatabaseWrapper> values() { return null; }
}
