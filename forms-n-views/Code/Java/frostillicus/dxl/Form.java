package frostillicus.dxl;

import java.util.*;
import java.io.*;
import javax.xml.xpath.XPathExpressionException;

import lotus.domino.*;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import org.openntf.domino.utils.xml.*;

import frostillicus.FNVUtil;

public class Form extends AbstractDXLDesignNote {
	private static final long serialVersionUID = 7167094282778445465L;

	public Form(final String databaseDocumentId, final String designDocumentId) throws Exception {
		super(databaseDocumentId, designDocumentId);
	}

	public List<Field> getFields() throws XPathExpressionException {
		List<XMLNode> fieldNodes = getDxl().selectNodes("//field");
		List<Field> result = new ArrayList<Field>(fieldNodes.size());
		for(XMLNode fieldNode : fieldNodes) {
			result.add(new Field(fieldNode));
		}
		return result;
	}

	public void removeField(final int index) throws XPathExpressionException {
		List<XMLNode> fieldNodes = getDxl().selectNodes("//field");
		fieldNodes.remove(index);
	}
	public void swapFields(final int a, final int b) throws XPathExpressionException {
		XMLNodeList fieldNodes = (XMLNodeList)getDxl().selectNodes("//field");
		fieldNodes.swap(a, b);
	}

	// Add new fields to a hidden paragraph
	public void addField() throws XPathExpressionException, IOException {
		XMLNode body = this.getDxl().selectSingleNode("/form/body/richtext");

		// Create an appropriate paragraph definition
		XMLNode finalPardef = this.getDxl().selectSingleNode("//pardef[last()]");
		int nextId = Integer.valueOf(finalPardef.getAttribute("id")) + 1;
		XMLNode pardef = body.addChildElement("pardef");
		pardef.setAttribute("id", String.valueOf(nextId));
		pardef.setAttribute("hide", "notes web mobile");

		// Now create the par and the field
		XMLNode par = body.addChildElement("par");
		par.setAttribute("def", pardef.getAttribute("id"));

		// Now add the field
		XMLNode field = par.addChildElement("field");
		field.setAttribute("kind", "editable");
		field.setAttribute("name", "");
		field.setAttribute("type", "text");
	}

	public class Field implements Serializable {
		private static final long serialVersionUID = 7257423276547953704L;

		private final XMLNode node;

		public Field(final XMLNode node) {
			this.node = node;
		}

		public String getName() { return node.getAttribute("name"); }
		public void setName(final String name) { node.setAttribute("name", name); }

		public String getType() { return node.getAttribute("type"); }
		public void setType(final String type) { node.setAttribute("type", type); }

		public String getKind() { return node.getAttribute("kind"); }
		public void setKind(final String kind) { node.setAttribute("kind", kind); }

		public boolean isAllowMultiValues() { return this.getAllowMultiValuesString().equals("true"); }
		public void setAllowMultiValues(final boolean allowMultiValues) { this.setAllowMultiValuesString(String.valueOf(allowMultiValues)); }
		public String getAllowMultiValuesString() { return node.getAttribute("allowmultivalues"); }
		public void setAllowMultiValuesString(final String allowMultiValues) { node.setAttribute("allowmultivalues", allowMultiValues); }

		public boolean isProtected() { return this.getProtectedString().equals("true"); }
		public void setProtected(final boolean _protected) { this.setProtectedString(String.valueOf(_protected)); }
		public String getProtectedString() { return node.getAttribute("protected"); }
		public void setProtectedString(final String _protected) { node.setAttribute("protected", _protected); }

		public boolean isSign() { return this.getSignString().equals("true"); }
		public void setSign(final boolean sign) { this.setSignString(String.valueOf(sign)); }
		public String getSignString() { return node.getAttribute("sign"); }
		public void setSignString(final String sign) { node.setAttribute("sign", sign); }

		public boolean getSeal() { return this.getSealString().equals("true"); }
		public void setSeal(final boolean seal) { this.setSealString(String.valueOf(seal)); }
		public String getSealString() { return node.getAttribute("seal"); }
		public void setSealString(final String seal) { node.setAttribute("seal", seal); }

		public boolean isLookUpAddressOnRefresh() { return this.getLookUpAddressOnRefreshString().equals("true"); }
		public void setLookUpAddressOnRefresh(final boolean lookUpAddressOnRefresh) { this.setLookUpAddressOnRefreshString(String.valueOf(lookUpAddressOnRefresh)); }
		public String getLookUpAddressOnRefreshString() { return this.node.getAttribute("lookupaddressonrefresh"); }
		public void setLookUpAddressOnRefreshString(final String lookUpAddressOnRefresh) { this.node.setAttribute("lookupaddressonrefresh", lookUpAddressOnRefresh); }

		public boolean isLookUpEachChar() { return this.getLookUpEachCharString().equals("true"); }
		public void setLookUpEachChar(final boolean lookUpEachChar) { this.setLookUpEachCharString(String.valueOf(lookUpEachChar)); }
		public String getLookUpEachCharString() { return this.node.getAttribute("lookupeachchar"); }
		public void setLookUpEachCharString(final String lookUpEachChar) { this.node.setAttribute("lookupeachchar", lookUpEachChar); }

		public String getDefaultValueFormula() {
			XMLNode node = this.getDefaultValueFormulaNode();
			if(node != null) {
				return node.getText();
			}
			return "";
		}
		public void setDefaultValueFormula(final String defaultValueFormula) {
			// DXL is not happy with empty default value nodes, so delete when empty
			XMLNode node = this.getDefaultValueFormulaNode();
			if(defaultValueFormula == null || defaultValueFormula.length() == 0) {
				if(node != null) {
					this.node.removeChild(node.getParentNode());
				}
			} else {
				if(node == null) {
					if(defaultValueFormula == null || defaultValueFormula.length() == 0) { return; }
					node = this.createDefaultValueFormulaNode();
				}
				node.setText(defaultValueFormula);
			}
		}

		// DXL uses the "keyword" field type for several field types, so it's more convenient to make a new faux
		//	attribute to handle referring to the field type like a human might
		public String getFieldType() {
			String type = this.getType();
			if(type.equals("keyword")) {
				XMLNode keywords = this.getKeywordsNode();
				String ui = keywords.getAttribute("ui");
				if(ui.equals("checkbox")) {
					return "checkbox";
				} else if(ui.equals("radiobutton")) {
					return "radiobutton";
				} else if(ui.equals("combobox")) {
					return "combobox";
				} else {
					return "dialoglist";
				}
			} else {
				return type;
			}
		}
		public void setFieldType(final String fieldType) {
			try {
				if(fieldType.equals("dialoglist") || fieldType.equals("checkbox") || fieldType.equals("radiobutton") || fieldType.equals("combobox")) {
					this.node.setAttribute("type", "keyword");
					XMLNode keywords = this.getKeywordsNode();
					keywords.setAttribute("ui", fieldType);
					keywords.setAttribute("helperbutton", String.valueOf(fieldType.equals("dialoglist")));
					if(keywords.getAttribute("columns").length() == 0) {
						keywords.setAttribute("columns", "1");
					}
				} else {
					this.node.setAttribute("type", fieldType);
					if(fieldType.equals("password")) {
						this.node.setAttribute("type", fieldType);
						this.node.setAttribute("seal", "true");
					} else if(fieldType.equals("richtextlite")) {
						this.node.setAttribute("type", fieldType);
						if(this.getKind().equals("computedfordisplay") || this.getKind().equals("computedwhencomposed")) {
							this.setKind("computed");
						}
						if(this.node.getAttribute("onlyallow").length() == 0) {
							this.node.setAttribute("onlyallow", "picture sharedimage attachment view datepicker sharedapplet text object calendar inbox help clear graphic link");
						}
						if(this.node.getAttribute("firstdisplay").length() == 0) {
							this.node.setAttribute("firstdisplay", "text");
						}
					} else if(fieldType.equals("richtext") && (this.getKind().equals("computedfordisplay") || this.getKind().equals("computedwhencomposed"))) {
						this.setKind("computed");
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		private XMLNode getKeywordsNode() {
			XMLNode node = null;
			node = this.node.selectSingleNode("keywords");

			if(node == null) {
				node = this.node.addChildElement("keywords");
			}
			return node;
		}
		private XMLNode getDefaultValueFormulaNode() {
			XMLNode node = null;
			node = this.node.selectSingleNode("code[@event='defaultvalue']");

			if(node == null) {
				return null;
			} else {
				node = node.selectSingleNode("formula");
			}
			return node;
		}
		private XMLNode createDefaultValueFormulaNode() {
			XMLNode node = this.node.addChildElement("code");
			node.setAttribute("event", "defaultvalue");
			node = node.addChildElement("formula");
			return node;
		}
	}

	public static String create(final String databaseDocumentId, final String name) throws Exception {
		DxlImporter importer = null;
		try {
			InputStream is = Stylesheet.class.getResourceAsStream("/frostillicus/dxl/form.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder xmlBuilder = new StringBuilder();
			while(reader.ready()) {
				xmlBuilder.append(reader.readLine());
				xmlBuilder.append("\n");
			}
			is.close();
			String xml = xmlBuilder.toString().replace("name=\"\"", "name=\"" + FNVUtil.xmlEncode(name) + "\"");
			xml = xml.replace("<item name='$$ScriptName' summary='false' sign='true'><text/></item>", "<item name='$$ScriptName' summary='false' sign='true'><text>" + FNVUtil.xmlEncode(name) + "</text></item>");

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
