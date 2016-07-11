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
