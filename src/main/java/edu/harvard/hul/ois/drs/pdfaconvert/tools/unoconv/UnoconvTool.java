/*
Copyright (c) 2016 by The President and Fellows of Harvard College
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permission and limitations under the License.
*/
package edu.harvard.hul.ois.drs.pdfaconvert.tools.unoconv;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConverterOutput;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool;

/**
 * Java wrapper for Unoconv tool (which calls LibreOffice) for converting .doc, .docx, .odt, .rtf and .wpd documents into PDF/A.
 * 
 * @author dan179
 */
public class UnoconvTool extends AbstractPdfaConverterTool {

	private List<String> unixCommand = new ArrayList<String>();

	private static final String TOOL_NAME = "UnoconvTool";
	private static final String TOOL_LOG_FILE_NAME = "unoconv-output.txt";
	private static final String UNOCONV_COMMAND = "unoconv";

	private static final Logger logger = LogManager.getLogger();

	public UnoconvTool(String unoconvHome, File outputDir) {
		super(outputDir);
		logger.debug("Entering C-tor for: {}", UnoconvTool.class.getSimpleName());
        
        File unoconvDir = new File(unoconvHome);
        logger.info("Unoconv home: {} -- isDirectory: ", unoconvHome, (unoconvHome == null ? "false" : unoconvDir.isDirectory()) );

		String command = unoconvHome + File.separatorChar + UNOCONV_COMMAND;
		logger.info("Have command: {}", command);
		unixCommand.add(command);
	}

	@Override
	protected String getToolName() {
		return TOOL_NAME;
	}


	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.unoconv.PdfaConvertable#extractInfo(java.io.File)
	 */
	public PdfaConverterOutput convert(File inputFile) {
        logger.debug("{}.extractInfo() starting on file: [{}]", TOOL_NAME, inputFile);
        logger.debug("file exists: {}", inputFile.exists());
        logger.debug("file absolute path: {}", inputFile.getAbsolutePath());

		List<String> execCommand = new ArrayList<String>();
		execCommand.addAll(unixCommand);
		execCommand.add("-vv"); // for verbosity
		execCommand.add("-f"); // PDF output format
		execCommand.add("pdf");
		execCommand.add("-eSelectPdfVersion=1"); // PDF/A output

		String outputFilenameBase = inputFile.getName().substring(0, inputFile.getName().indexOf('.'));
		String generatedPdfFilename = outputFilenameBase + ".pdf";
		logger.debug("outputFilename: {}", generatedPdfFilename);
		execCommand.add("-o"); // output location - directory or filename
		execCommand.add( getOutputDirectory() + File.separator + generatedPdfFilename);
		execCommand.add(inputFile.getAbsolutePath());

		logger.debug("About to launch {}, command: {}", TOOL_NAME, execCommand);
		ByteArrayOutputStream baos = processCommand(execCommand, null);
		String logFilename = getOutputDirectory() + File.separator + TOOL_LOG_FILE_NAME;
		logApplicationOutput(logFilename, baos);
		
		File pdfaOutputFile = retrieveGeneratedFile( getOutputDirectory(), generatedPdfFilename);
		String toolOutput = getToolLoggingOutput(baos);
		PdfaConverterOutput converterOutput = new PdfaConverterOutput(pdfaOutputFile, toolOutput);		
		logger.debug("Finished running {}", TOOL_NAME);
		return converterOutput;
	}

}
