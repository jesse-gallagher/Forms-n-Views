package frostillicus;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.*;

import java.io.*;
import java.util.*;
import javax.faces.context.FacesContext;

import lotus.domino.NotesException;

@SuppressWarnings("unused")
public class DatabaseList extends TabularDataModel implements Serializable, TabularDataSource {
	private static final long serialVersionUID = -4878641986912884110L;



	public DatabaseList() throws NotesException {

	}

	@Override public int getRowCount() { return 0; }

	@Override
	public Object getRowData() {
		// TODO Auto-generated method stub
		return null;
	}

	private class DatabaseListEntry implements ViewRowData {

		public ColumnInfo getColumnInfo(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getColumnValue(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getOpenPageURL(String arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getValue(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isReadOnly(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public void setColumnValue(String arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

	}

	@SuppressWarnings("unchecked")
	private class FakeEntryData extends HashMap<String, Comparable> implements ViewRowData {
		private static final long serialVersionUID = 5946100397649532083L;

		public Object getColumnValue(String arg0) { return this.get(arg0); }
		public Object getValue(String arg0) { return this.get(arg0); }
		public ColumnInfo getColumnInfo(String arg0) { return null; }
		public String getOpenPageURL(String arg0, boolean arg1) { return null; }
		public boolean isReadOnly(String arg0) { return false; }

		public void setColumnValue(String arg0, Object arg1) {
			if(!(arg1 instanceof Comparable)) {
				this.put(arg0, arg1.toString());
			} else {
				this.put(arg0, (Comparable<?>)arg1);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private class MapComparator implements Comparator<Map<String, Comparable>> {
		private String key;
		private boolean ascending;

		public MapComparator(String key, boolean ascending) {
			this.key = key;
			this.ascending = ascending;
		}

		public int compare(Map<String, Comparable> o1, Map<String, Comparable> o2) {
			return (ascending ? 1 : -1) * o1.get(key).compareTo(o2.get(key));
		}
	}
}
