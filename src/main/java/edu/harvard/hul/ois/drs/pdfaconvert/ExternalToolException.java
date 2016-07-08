/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert;

/**
 * Thrown when there is a problem with an external tool converting an input file to a PDF/A.
 * 
 * @author dan179
 */
public class ExternalToolException extends PdfaConversionException {
	private static final long serialVersionUID = -4405679438024293956L;

	public ExternalToolException() {
		super();
	}

	public ExternalToolException(String message) {
		super(message);
	}

	public ExternalToolException(Throwable cause) {
		super(cause);
	}

	public ExternalToolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExternalToolException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
