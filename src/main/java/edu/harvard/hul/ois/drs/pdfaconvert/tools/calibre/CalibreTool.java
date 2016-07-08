/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert.tools.calibre;

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

	public CalibreTool(String calibreHome) {
		super();
		logger.debug("Entering C-tor for: " + CalibreTool.class.getSimpleName());
        
        File calibreHomeDir = new File(calibreHome);
        logger.info(calibreHome + " isDirectory: " + calibreHomeDir.isDirectory());

        String command = calibreHome + CALIBRE_COMMAND;
		logger.debug("Have command: " + command);
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
        logger.debug(TOOL_NAME + ".extractInfo() starting on file: [" + inputFile.getName() + "]");
        logger.debug("file exists: " + inputFile.exists());
        logger.debug("file absolute path: " + inputFile.getAbsolutePath());
		List<String> execCommand = new ArrayList<String>();
		execCommand.addAll(unixCommand);
		execCommand.add(inputFile.getAbsolutePath()); // input file first
		String outputDir = PdfaConvert.applicationProps.getProperty(ApplicationConstants.OUTPUT_DIR_PROP);
		String outputFilenameBase = inputFile.getName().substring(0, inputFile.getName().indexOf('.'));
		String generatedPdfFilename = outputFilenameBase + ".pdf";
		execCommand.add(outputDir + File.separator + generatedPdfFilename);
		
		logger.debug("Launching CalibreTool, command = " + execCommand);
		ByteArrayOutputStream baos = processCommand(execCommand, null);
		String logFilename = outputDir + File.separator + TOOL_LOG_FILE_NAME;
		logApplicationOutput(logFilename, baos);

		File pdfaOutputFile = retrieveGeneratedFile(outputDir, generatedPdfFilename);
		PdfaConverterOutput converterOutput = new PdfaConverterOutput(pdfaOutputFile);		
		logger.debug("Finished running " + TOOL_NAME);
		return converterOutput;
	}
}
