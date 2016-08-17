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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.ApplicationConstants;
import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConvert;
import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConverterOutput;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool;

/**
 * Java wrapper for Callas Software pdfaPilot CLI tool for converting PDF documents into PDF/A.
 * 
 * @author dan179
 */
public class PdfaPilotTool extends AbstractPdfaConverterTool {

	private static final String TOOL_NAME = "PdfaPilotTool";
	private static final String TOOL_LOG_FILE_NAME = "pdfaPilot-output.txt";
	private static final String PDFA_PILOT_COMMAND = "pdfaPilot";
	
	private static List<String> basicPdfaPilotCommand;
	
	private List<String> fullPdfaPilotCommand;
	private boolean useRemoteInputDirSetting = false;
	
	private static final Logger logger = LogManager.getLogger();
	
	static {		
		// Basic command to invoke pdfaPilot -- It is lacking the output location and filename, and input filename.
		basicPdfaPilotCommand = new ArrayList<String>();
		basicPdfaPilotCommand.add(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_HOME_PROP) + PDFA_PILOT_COMMAND); // executable
		basicPdfaPilotCommand.add("--onlypdfa"); // convert to PDF/A format
		basicPdfaPilotCommand.add("--overwrite"); // overwrite existing file rather than add index to file name.
		// As an alternative the following is to set the output folder only, instead of setting the output file with "--outputfile="
		// basicPdfaPilotCommand.add("--outputfolder=" + PdfaConvert.applicationProps.getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_OUTPUT_DIR_PROP));
	}

	/**
	 * Constructor for use with local execution of pdfaPilot.
	 * 
	 * @param pdfaPilotHome Location on system of pdfaPilot home directory.
	 * @param outputDir Output directory for transformed files.
	 */
	public PdfaPilotTool(String pdfaPilotHome, File outputDir) {
		super(outputDir);
		logger.debug("Entering C-tor for: {}", PdfaPilotTool.class.getSimpleName());
		fullPdfaPilotCommand = new ArrayList<String>();
		fullPdfaPilotCommand.addAll(basicPdfaPilotCommand);
	}
	
	/**
	 * Constructor for use with remote execution of pdfaPilot.
	 * 
	 * @param pdfaPilotHome Location on remote system of pdfaPilot home directory.
	 * @param outputDir Output directory for transformed files on local system.
	 * @param tunnelingPrefix String for adding to 'ssh' command for tunneling.
	 */
	protected PdfaPilotTool(String pdfaPilotHome, File outputDir, List<String> tunnelingPrefix) {
		super(outputDir);
		fullPdfaPilotCommand = new ArrayList<String>();
		fullPdfaPilotCommand.addAll(tunnelingPrefix);
		fullPdfaPilotCommand.addAll(basicPdfaPilotCommand);
		useRemoteInputDirSetting = true;
	}

	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool#getToolName()
	 */
	@Override
	protected String getToolName() {
		return TOOL_NAME;
	}

	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.PdfaConvertable#convert(java.io.File)
	 */
	@Override
	public PdfaConverterOutput convert(File inputFile) {
		return convert(inputFile, false);
	}

	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.PdfaConvertable#convert(java.io.File, boolean)
	 */
	@Override
	public PdfaConverterOutput convert(File inputFile, boolean deleteConvertedFile) {
        logger.debug("{}.convert() starting on file: [{}]", TOOL_NAME, inputFile);
        logger.debug("file exists: {}", inputFile.exists());
        logger.debug("file absolute path: {}", inputFile.getAbsolutePath());

        // Process input file with pdfaPilot
        String generatedPdfFilename = getGeneratedPdfFilename(inputFile);
        List<String> execCommand = new ArrayList<String>();
		execCommand.addAll(fullPdfaPilotCommand);
		if (useRemoteInputDirSetting) {
			// set set output file
			execCommand.add("--outputfile=" + PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_OUTPUT_DIR_PROP) + File.separatorChar + generatedPdfFilename);
			// set input file
			execCommand.add(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_INPUT_DIR_PROP) + File.separatorChar + inputFile.getName());
		} else {
			// set output file
			execCommand.add("--outputfile=" + getOutputDirectory() + File.separatorChar + generatedPdfFilename);
			// set input file
			execCommand.add(inputFile.getAbsolutePath());
		}

		logger.debug("About to launch {}, command: {}", TOOL_NAME, execCommand);
		ByteArrayOutputStream baos = processCommand(execCommand, null);

		String logFilename = getOutputDirectory() + File.separator + TOOL_LOG_FILE_NAME;
		logApplicationOutput(logFilename, baos);
		PdfaConverterOutput converterOutput = retrieveGeneratedOutput(generatedPdfFilename, baos, deleteConvertedFile);
		logger.debug("Finished running {}", TOOL_NAME);
		return converterOutput;
	}
	
	/**
	 * Retrieve the generated converted file from output directory.
	 * 
	 * @param filename Name of file to retrieve.
	 * @param baos Output from pdfaPilot executable.
	 * @param deleteConvertedFile Delete the converted file.
	 * @return PdfaConverterOutput which wraps both the converted file and text output of pdfaPilot.
	 */
	protected PdfaConverterOutput retrieveGeneratedOutput(String filename, ByteArrayOutputStream baos, boolean deleteConvertedFile) {
		File pdfaOutputFile = retrieveGeneratedFile( filename, deleteConvertedFile);
		String toolOutput = getToolLoggingOutput(baos);
		PdfaConverterOutput converterOutput = new PdfaConverterOutput(pdfaOutputFile, toolOutput);
		return converterOutput;
	}
}
