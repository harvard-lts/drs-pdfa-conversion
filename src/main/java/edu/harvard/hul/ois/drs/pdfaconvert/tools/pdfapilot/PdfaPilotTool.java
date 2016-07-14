/*
Copyright (c) 2016 by The President and Fellows of Harvard College
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permission and limitations under the License.
*/
package edu.harvard.hul.ois.drs.pdfaconvert.tools.pdfapilot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool;

/**
 * Java wrapper for Callas Software pdfaPilot CLI tool for converting PDF documents into PDF/A.
 * 
 * @author dan179
 */
public class PdfaPilotTool extends AbstractPdfaConverterTool {

	private static final String TOOL_NAME = "PdfaPilotTool";
	private static final Logger logger = LogManager.getLogger();

	public PdfaPilotTool(String pdfaPilotHome) {
		super();
		logger.debug("Entering C-tor for: " + PdfaPilotTool.class.getSimpleName());
	}

	@Override
	protected String getToolName() {
		return TOOL_NAME;
	}

}
