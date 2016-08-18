/*
Copyright (c) 2016 by The President and Fellows of Harvard College
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permission and limitations under the License.
*/
package edu.harvard.hul.ois.drs.pdfaconvert.tools.calibre;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConverterOutput;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool;

/**
 * Java wrapper for Calibre tool for converting EPUB documents into PDF/A.
 * 
 * @author dan179
 */
public class CalibreTool extends AbstractPdfaConverterTool {

	private static final String TOOL_NAME = "CalibreTool";
	private static final String TOOL_LOG_FILE_NAME = "calibre-output.txt";
	private static final String CALIBRE_COMMAND = "/ebook-convert";
	private static final Logger logger = LogManager.getLogger();
	
	private List<String> unixCommand = new ArrayList<String>();

	public CalibreTool(String calibreHome, File outputDir) {
		super(outputDir);
		logger.debug("Entering C-tor for: {}", CalibreTool.class.getSimpleName());
        
        File calibreHomeDir = new File(calibreHome);
        logger.info("Calibre home: {} -- isDirectory: ", calibreHome, (calibreHome == null ? "false" : calibreHomeDir.isDirectory()) );

        String command = calibreHome + CALIBRE_COMMAND;
		logger.debug("Have command: {}", command);
		unixCommand.add(command);
	}

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

        String generatedPdfFilename = getGeneratedPdfFilename(inputFile);
        List<String> execCommand = new ArrayList<String>();
		execCommand.addAll(unixCommand);
		execCommand.add(inputFile.getAbsolutePath()); // input file first
		execCommand.add( getOutputDirectory() + File.separator + generatedPdfFilename);
		logger.debug("Launching {}, with command: {}",  TOOL_NAME, execCommand);
		ByteArrayOutputStream baos = processCommand(execCommand, null);
		String logFilename = getOutputDirectory() + File.separator + TOOL_LOG_FILE_NAME;
		logApplicationOutput(logFilename, baos);

		File pdfaOutputFile = retrieveGeneratedFile(generatedPdfFilename, deleteConvertedFile);
		String toolOutput = getToolLoggingOutput(baos);
		PdfaConverterOutput converterOutput = new PdfaConverterOutput(pdfaOutputFile, toolOutput);		
		logger.debug("Finished running {}", TOOL_NAME);
		return converterOutput;
	}
}
