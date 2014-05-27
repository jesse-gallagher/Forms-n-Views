package frostillicus.dxl;

import java.io.*;
import java.util.List;

import lotus.domino.*;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;

@SuppressWarnings("serial")
public abstract class AbstractDXLDesignNote implements Serializable {

	private String databaseDocumentId;
	private String designDocumentId;
	private XMLDocument dxl;

	protected String getTemplateName() { return "abstract"; }


	public AbstractDXLDesignNote(final String databaseDocumentId, final String designDocumentId) throws Exception {
		this.databaseDocumentId = databaseDocumentId;
		this.designDocumentId = designDocumentId;

		Document databaseDoc = ExtLibUtil.getCurrentDatabase().getDocumentByUNID(databaseDocumentId);
		Database foreignDB = ExtLibUtil.getCurrentSession().getDatabase(databaseDoc.getItemValueString("Server"), databaseDoc.getItemValueString("FilePath"));
		Document foreignDesignDoc = foreignDB.getDocumentByUNID(designDocumentId);

		dxl = new XMLDocument();
		dxl.loadString(foreignDesignDoc.generateXML());

		foreignDesignDoc.recycle();
		foreignDB.recycle();
	}

	public String getUniqueKey() {
		return this.getDatabaseDocumentId() + this.getDesignDocumentId();
	}

	public String getName() { return this.getRootNode().getAttribute("name"); }
	public void setName(final String name) { this.getRootNode().setAttribute("name", name); }
	public void setName(final List<String> name) {
		// Sometimes the page provides a list for no reason
		this.setName(name.get(0));
	}

	public String getAlias() {
		return this.getRootNode().getAttribute("alias");
	}
	public void setAlias(final String alias) {
		this.getRootNode().setAttribute("alias", alias);
	}

	public String save() throws Exception {
		DxlImporter importer = ExtLibUtil.getCurrentSessionAsSignerWithFullAccess().createDxlImporter();
		try {

			Document databaseDoc = ExtLibUtil.getCurrentDatabase().getDocumentByUNID(getDatabaseDocumentId());
			Database foreignDB = ExtLibUtil.getCurrentSessionAsSignerWithFullAccess().getDatabase(databaseDoc.getItemValueString("Server"), databaseDoc.getItemValueString("FilePath"));

			importer.setDesignImportOption(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE);
			importer.setReplicaRequiredForReplaceOrUpdate(false);
			importer.importDxl(this.getDxl().getXml(), foreignDB);

		} catch(Exception e) {
			throw new Exception(importer.getLog());
		}
		return "xsp-success";
	}

	public String getXml() throws IOException { return this.getDxl().getXml(); }

	protected XMLDocument getDxl() { return this.dxl; }
	protected XMLNode getRootNode() {
		return getDxl().getFirstChild();
	}

	public String getDatabaseDocumentId() { return this.databaseDocumentId; }
	public String getDesignDocumentId() { return this.designDocumentId; }
}
