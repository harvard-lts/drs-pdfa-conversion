/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert;

/**
 * If the generated file is either unavailable or unreadable.
 * 
 * @author dan179
 */
public class GeneratedFileUnavailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

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
