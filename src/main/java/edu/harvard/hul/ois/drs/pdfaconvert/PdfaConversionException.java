/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert;

/**
 * Parent class of any specific exception related to converting an input file to a PDF/A document.
 * 
 * @author dan179
 */
public abstract class PdfaConversionException extends RuntimeException {
	private static final long serialVersionUID = -9102904581762816500L;

	public PdfaConversionException() {
		super();
	}

	public PdfaConversionException(String message) {
		super(message);
	}

	public PdfaConversionException(Throwable cause) {
		super(cause);
	}

	public PdfaConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public PdfaConversionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
