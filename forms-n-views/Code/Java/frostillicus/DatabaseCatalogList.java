package frostillicus;

import java.io.Serializable;
import java.util.*;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import lotus.domino.*;

public class DatabaseCatalogList implements Serializable {
	private static final long serialVersionUID = 8248049049375199070L;

	private String server;
	private Collection<DatabaseCatalogEntry> databaseList;

	public DatabaseCatalogList() {
		this.databaseList = new TreeSet<DatabaseCatalogEntry>(new FilePathComparitor());
	}

	public String getServer() { return this.server; }
	public void setServer(String server) throws NotesException {
		this.server = server;

		System.out.println("Server set to " + server);

		this.databaseList.clear();

		Session session = ExtLibUtil.getCurrentSession();
		DbDirectory dbdir = session.getDbDirectory(server);
		Database db = dbdir.getFirstDatabase(DbDirectory.DATABASE);
		while(db != null) {
			DatabaseCatalogEntry dbEntry = new DatabaseCatalogEntry();
			dbEntry.setTitle(db.getTitle());
			dbEntry.setFilePath(db.getFilePath());
			dbEntry.setType("database");

			this.databaseList.add(dbEntry);

			Database tempDB = db;
			db = dbdir.getNextDatabase();
			tempDB.recycle();
		}

		db = dbdir.getFirstDatabase(DbDirectory.TEMPLATE);
		while(db != null) {
			DatabaseCatalogEntry dbEntry = new DatabaseCatalogEntry();
			dbEntry.setTitle(db.getTitle());
			dbEntry.setFilePath(db.getFilePath());
			dbEntry.setType("template");

			this.databaseList.add(dbEntry);

			Database tempDB = db;
			db = dbdir.getNextDatabase();
			tempDB.recycle();
		}
	}

	public Collection<DatabaseCatalogEntry> getDatabaseList() { return this.databaseList; }


	public class FilePathComparitor implements Comparator<DatabaseCatalogEntry>, Serializable {
		private static final long serialVersionUID = 823887636162869462L;

		public int compare(DatabaseCatalogEntry arg0, DatabaseCatalogEntry arg1) {
			return arg0.getFilePath().compareToIgnoreCase(arg1.getFilePath());
		}

	}
	public class DatabaseCatalogEntry implements Serializable {
		private static final long serialVersionUID = 2449860780439826951L;

		private String title;
		private String filePath;
		private String type;

		public String getTitle() { return title; }
		public void setTitle(String title) { this.title = title; }
		public String getFilePath() { return filePath; }
		public void setFilePath(String filePath) { this.filePath = filePath; }
		public String getType() { return type; }
		public void setType(String type) { this.type = type; }
	}
}
