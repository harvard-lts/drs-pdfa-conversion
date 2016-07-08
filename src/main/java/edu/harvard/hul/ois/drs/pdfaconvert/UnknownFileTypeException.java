/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert;

/**
 * Thrown to indicate that the input file type cannot be converted to a PDF/A document.
 * 
 * @author dan179
 */
public class UnknownFileTypeException extends PdfaConversionException {
	private static final long serialVersionUID = -4002587758357738734L;

	public UnknownFileTypeException() {
		super();
	}

	public UnknownFileTypeException(String message) {
		super(message);
	}

	public UnknownFileTypeException(Throwable cause) {
		super(cause);
	}

	public UnknownFileTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownFileTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
