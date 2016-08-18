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

/**
 * Java wrapper for Callas Software pdfaPilot CLI tool for converting PDF documents into PDF/A to be
 * executed on a remote system via SSH tunneling.
 * 
 * @author dan179
 */
public class PdfaPilotRemoteTool extends PdfaPilotTool {
	
	private static final String TOOL_NAME = "PdfaPilotRemoteTool";

	private File inputFile;
	private static List<String> tunnelingPrefixCommand;
	
	private static final Logger logger = LogManager.getLogger();

	static {
		// Part of command that does tunneling across network
		tunnelingPrefixCommand = new ArrayList<String>();
		tunnelingPrefixCommand.add("ssh");
		tunnelingPrefixCommand.add(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_USER_PROP) + "@" + PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_URL_PROP));
		tunnelingPrefixCommand.add("-L");
		tunnelingPrefixCommand.add(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_TUNNELLING_STRING_PROP));
	}
	/**
	 * @param pdfaPilotHome
	 */
	public PdfaPilotRemoteTool(String pdfaPilotHome, File outputDir) {
		super(pdfaPilotHome, outputDir, tunnelingPrefixCommand);
	}
	
	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.pdfapilot.PdfaPilotTool#convert(java.io.File)
	 */
	@Override
	public PdfaConverterOutput convert(File inputFile) {
		return convert(inputFile, false);
	}

	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.pdfapilot.PdfaPilotTool#convert(java.io.File, boolean)
	 */
	@Override
	public PdfaConverterOutput convert(File inputFile, boolean deleteConvertedFile) {
		this.inputFile = inputFile;
		
		copyFileToRemote();
		PdfaConverterOutput output = null;
		try {
			output = super.convert(inputFile, deleteConvertedFile);
		} finally {
			// attempt to clean up on remote server even if a problem
			removeRemoteInputFile();
			removeRemoteDerivativeFile();
		}
		logger.debug("Finished running {}", TOOL_NAME);
		return output;
	}
	
	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.pdfapilot.PdfaPilotTool#retrieveGeneratedOutput(java.lang.String, java.io.ByteArrayOutputStream, boolean)
	 */
	protected PdfaConverterOutput retrieveGeneratedOutput(String filename, ByteArrayOutputStream baos, boolean deleteConvertedFile) {
		copyDerivativeFileFromRemote(); // Must copy file from remote server before able to return it.
		return super.retrieveGeneratedOutput(filename, baos, deleteConvertedFile);
	}

	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.pdfapilot.PdfaPilotTool#getToolName()
	 */
	@Override
	protected String getToolName() {
		return TOOL_NAME;
	}

	/*
	 * Copies input file from local location to configured location on remote server.
	 */
	private void copyFileToRemote() {
        List<String> scpExecCommand = new ArrayList<String>();
        scpExecCommand.add("scp");
        scpExecCommand.add(inputFile.getAbsolutePath());
        scpExecCommand.add(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_USER_PROP) +
        		"@" +
        		PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_URL_PROP) +
        		":~/" +
        		PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_INPUT_DIR_PROP));
		logger.debug("About to launch {}, with command: {}", getToolName(), scpExecCommand);
        processCommand(scpExecCommand, null);
	}

	/*
	 * Copied converted file from remote server to locally configured output directory.
	 */
	private void copyDerivativeFileFromRemote() {
        String generatedPdfFilename = getGeneratedPdfFilename(inputFile);
        List<String> scpExecCommand = new ArrayList<String>();
		scpExecCommand.clear();
		scpExecCommand.add("scp");
        scpExecCommand.add(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_USER_PROP) +
        		"@" +
        		PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_URL_PROP) +
        		":~/" +
        		PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_OUTPUT_DIR_PROP) +
        		File.separatorChar +
        		generatedPdfFilename);
        scpExecCommand.add( getOutputDirectory() );
		logger.debug("About to launch {}, command: {}", getToolName(), scpExecCommand);
		processCommand(scpExecCommand, null);
	}
	
	/*
	 * Removes input file from remote server once the conversion is complete and it is no longer needed.
	 */
	private void removeRemoteInputFile() {
		List<String> scpRmInputFile = new ArrayList<String>();
		scpRmInputFile.addAll(tunnelingPrefixCommand);
		scpRmInputFile.add("rm " +
				PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_INPUT_DIR_PROP) + File.separatorChar + inputFile.getName());
		logger.debug("About to launch {}, command: {}", getToolName(), scpRmInputFile);
		processCommand(scpRmInputFile, null);
	}

	/*
	 * Removes converted file from remote server after it is retrieved and no longer needed remotely.
	 */
	private void removeRemoteDerivativeFile() {
        String generatedPdfFilename = getGeneratedPdfFilename(inputFile);
		List<String> scpRmOutputFile = new ArrayList<String>();
		scpRmOutputFile.addAll(tunnelingPrefixCommand);
		scpRmOutputFile.add("rm " +
				PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.PDFA_PILOT_REMOTE_OUTPUT_DIR_PROP) + File.separatorChar + generatedPdfFilename);
		logger.debug("About to launch {}, command: {}", getToolName(), scpRmOutputFile);
		processCommand(scpRmOutputFile, null);
	}
}
