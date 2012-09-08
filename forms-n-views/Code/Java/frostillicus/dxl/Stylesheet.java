package frostillicus.dxl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.xpath.XPathExpressionException;
import com.raidomatic.xml.XMLNode;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
//import org.apache.commons.codec.binary.Base64;

public class Stylesheet extends AbstractDXLDesignNote {
	private static final long serialVersionUID = -3543549758559295423L;
	public Stylesheet(String databaseDocumentId, String designDocumentId) throws Exception {
		super(databaseDocumentId, designDocumentId);
	}

	public String getContent() throws XPathExpressionException, UnsupportedEncodingException, IOException {
		String fileData = this.getRootNode().selectSingleNode("/stylesheetresource/filedata").getTextContent();

		//return Base64Coder.decodeString(fileData);
		return new String(new BASE64Decoder().decodeBuffer(fileData), "UTF-8");
		//return new String(Base64.decodeBase64(fileData), "UTF-8");
	}
	public void setContent(String content) throws XPathExpressionException {
		XMLNode dataNode = this.getRootNode().selectSingleNode("/stylesheetresource/filedata");
		dataNode.setTextContent(new BASE64Encoder().encodeBuffer(content.getBytes()).trim());
		//dataNode.setTextContent(Base64.encodeBase64String(content.getBytes()));
	}
}
