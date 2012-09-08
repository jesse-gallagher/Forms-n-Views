package frostillicus.dxl;

import java.util.*;
import java.io.Serializable;
import javax.xml.xpath.XPathExpressionException;
import com.raidomatic.xml.*;

public class Form extends AbstractDXLDesignNote {
	private static final long serialVersionUID = 7167094282778445465L;

	public Form(String databaseDocumentId, String designDocumentId) throws Exception {
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
	//	public void addField() throws XPathExpressionException {
	//		// Just add it to the first paragraph for now
	//		XMLNode fieldNode = this.getDxl().selectSingleNode("/form/body/richtext/par");
	//		Field field = new Field(fieldNode);
	//		field.setFieldType("text");
	//	}
	public void swapFields(int a, int b) throws XPathExpressionException {
		XMLNodeList fieldNodes = (XMLNodeList)getDxl().selectNodes("//field");
		fieldNodes.swap(a, b);
	}

	public class Field implements Serializable {
		private static final long serialVersionUID = 7257423276547953704L;

		private final XMLNode node;

		public Field(XMLNode node) {
			this.node = node;
		}

		public String getName() { return node.getAttribute("name"); }
		public void setName(String name) { node.setAttribute("name", name); }

		public String getType() { return node.getAttribute("type"); }
		public void setType(String type) { node.setAttribute("type", type); }

		public String getKind() { return node.getAttribute("kind"); }
		public void setKind(String kind) { node.setAttribute("kind", kind); }

		public boolean isAllowMultiValues() { return this.getAllowMultiValuesString().equals("true"); }
		public void setAllowMultiValues(boolean allowMultiValues) { this.setAllowMultiValuesString(String.valueOf(allowMultiValues)); }
		public String getAllowMultiValuesString() { return node.getAttribute("allowmultivalues"); }
		public void setAllowMultiValuesString(String allowMultiValues) { node.setAttribute("allowmultivalues", allowMultiValues); }

		public boolean isProtected() { return this.getProtectedString().equals("true"); }
		public void setProtected(boolean _protected) { this.setProtectedString(String.valueOf(_protected)); }
		public String getProtectedString() { return node.getAttribute("protected"); }
		public void setProtectedString(String _protected) { node.setAttribute("protected", _protected); }

		public boolean isSign() { return this.getSignString().equals("true"); }
		public void setSign(boolean sign) { this.setSignString(String.valueOf(sign)); }
		public String getSignString() { return node.getAttribute("sign"); }
		public void setSignString(String sign) { node.setAttribute("sign", sign); }

		public boolean getSeal() { return this.getSealString().equals("true"); }
		public void setSeal(boolean seal) { this.setSealString(String.valueOf(seal)); }
		public String getSealString() { return node.getAttribute("seal"); }
		public void setSealString(String seal) { node.setAttribute("seal", seal); }

		public boolean isLookUpAddressOnRefresh() { return this.getLookUpAddressOnRefreshString().equals("true"); }
		public void setLookUpAddressOnRefresh(boolean lookUpAddressOnRefresh) { this.setLookUpAddressOnRefreshString(String.valueOf(lookUpAddressOnRefresh)); }
		public String getLookUpAddressOnRefreshString() { return this.node.getAttribute("lookupaddressonrefresh"); }
		public void setLookUpAddressOnRefreshString(String lookUpAddressOnRefresh) { this.node.setAttribute("lookupaddressonrefresh", lookUpAddressOnRefresh); }

		public boolean isLookUpEachChar() { return this.getLookUpEachCharString().equals("true"); }
		public void setLookUpEachChar(boolean lookUpEachChar) { this.setLookUpEachCharString(String.valueOf(lookUpEachChar)); }
		public String getLookUpEachCharString() { return this.node.getAttribute("lookupeachchar"); }
		public void setLookUpEachCharString(String lookUpEachChar) { this.node.setAttribute("lookupeachchar", lookUpEachChar); }


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
		public void setFieldType(String fieldType) {
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
			try {
				node = this.node.selectSingleNode("keywords");
			} catch(XPathExpressionException xee) { }

			if(node == null) {
				node = this.node.addChildElement("keywords");
			}
			return node;
		}
	}
}
