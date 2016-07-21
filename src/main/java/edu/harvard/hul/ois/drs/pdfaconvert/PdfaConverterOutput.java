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

import java.io.File;
import java.io.Serializable;

/**
 * Wrapper class for a file converted to PDF/A format as well as any other relevant data.
 * 
 * @author dan179
 */
public class PdfaConverterOutput implements Serializable {
	private static final long serialVersionUID = 8657370419144639361L;

	private File pdfaConvertedFile;
	private String toolLoggingOutput;

	public PdfaConverterOutput(File pdfaConvertedFile, String toolLoggingOutput) {
		super();
		this.pdfaConvertedFile = pdfaConvertedFile;
		this.toolLoggingOutput = toolLoggingOutput;
	}

	public File getPdfaConvertedFile() {
		return pdfaConvertedFile;
	}
	
	public String getToolLoggingOutput() {
		return toolLoggingOutput;
	}

}
