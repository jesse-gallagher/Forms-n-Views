package frostillicus.dxl;

import java.io.*;

import lotus.domino.*;

import com.ibm.xsp.extlib.util.ExtLibUtil;

import frostillicus.FNVUtil;

public class Folder extends AbstractFolder {
	private static final long serialVersionUID = -8774232556021141733L;

	public Folder(final String databaseDocumentId, final String designDocumentId) throws Exception {
		super(databaseDocumentId, designDocumentId);
	}

	public static String create(final String databaseDocumentId, final String name) throws Exception {
		DxlImporter importer = null;
		try {
			InputStream is = Stylesheet.class.getResourceAsStream("/frostillicus/dxl/folder.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder xmlBuilder = new StringBuilder();
			while(reader.ready()) {
				xmlBuilder.append(reader.readLine());
				xmlBuilder.append("\n");
			}
			is.close();
			String xml = xmlBuilder.toString().replace("name=\"\"", "name=\"" + FNVUtil.xmlEncode(name) + "\"");

			importer = ExtLibUtil.getCurrentSession().createDxlImporter();
			importer.setDesignImportOption(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE);
			importer.setReplicaRequiredForReplaceOrUpdate(false);
			Document databaseDoc = ExtLibUtil.getCurrentDatabase().getDocumentByUNID(databaseDocumentId);
			Database foreignDB = ExtLibUtil.getCurrentSessionAsSignerWithFullAccess().getDatabase(databaseDoc.getItemValueString("Server"), databaseDoc.getItemValueString("FilePath"));
			importer.importDxl(xml, foreignDB);

			Document importedDoc = foreignDB.getDocumentByID(importer.getFirstImportedNoteID());
			return importedDoc.getUniversalID();
		} catch(Exception e) {
			e.printStackTrace();
			if(importer != null) {
				System.out.println(importer.getLog());
			}
		}
		return null;
	}
}
