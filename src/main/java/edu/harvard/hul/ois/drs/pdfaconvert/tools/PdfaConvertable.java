package edu.harvard.hul.ois.drs.pdfaconvert.tools;

import java.io.File;

public interface PdfaConvertable {

	/**
	 * Convert the input file to a PDF/A.
	 * If method not implemented by class implementing this interface
	 * then this default implementation will be used.
	 * 
	 * @param inputFile The file to convert.
	 */
	default void convert(File inputFile) {
		throw new UnsupportedOperationException();
	};

}