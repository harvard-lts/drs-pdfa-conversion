/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert;

import java.io.File;
import java.io.Serializable;

/**
 * Wrapper class for a file converted to PDF/A format as well as any other relevant data.
 * 
 * @author dan179
 */
public class PdfaConverterOutput implements Serializable {
	private static final long serialVersionUID = 6166711611392895982L;
	
	private File pdfaConvertedFile;

	public PdfaConverterOutput(File pdfaConvertedFile) {
		super();
		this.pdfaConvertedFile = pdfaConvertedFile;
	}

	public File getPdfaConvertedFile() {
		return pdfaConvertedFile;
	}

}
