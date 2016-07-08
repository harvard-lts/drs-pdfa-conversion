/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert;

/**
 * Thrown if the generated PDF/A file is either unavailable or unreadable.
 * 
 * @author dan179
 */
public class GeneratedFileUnavailableException extends PdfaConversionException {
	private static final long serialVersionUID = -3738551771908780471L;

	public GeneratedFileUnavailableException() {
		super();
	}

	public GeneratedFileUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneratedFileUnavailableException(String message) {
		super(message);
	}

	public GeneratedFileUnavailableException(Throwable cause) {
		super(cause);
	}
	
}
