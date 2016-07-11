/*
Copyright (c) 2016 by The President and Fellows of Harvard College
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permission and limitations under the License.
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
