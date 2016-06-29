/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert.tools.unoconv;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.ApplicationConstants;
import edu.harvard.hul.ois.drs.pdfaconvert.GeneratedFileUnavailableException;
import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConvert;
import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConverterOutput;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.PdfaConvertable;

/**
 * @author dan179
 */
public class UnoconvTool extends AbstractPdfaConverterTool implements PdfaConvertable {

	private List<String> unixCommand = new ArrayList<String>();

	private static final String TOOL_NAME = "UnoconvTool";
	private static final String TOOL_LOG_FILE_NAME = "unoconv-output.txt";

	private static final Logger logger = LogManager.getLogger();

	public UnoconvTool(String unoconvHome) {
		super();
        logger.debug ("Initializing " + TOOL_NAME);
        
        File unoconvDir = new File(unoconvHome);
        logger.info(unoconvHome + " isDirectory: " + unoconvDir.isDirectory());

		String command = unoconvHome  + "/unoconv";
		logger.info("Have command: " + command);
		unixCommand.add(command);
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
		execCommand.add("-vv"); // for verbosity
		execCommand.add("-f"); // PDF output format
		execCommand.add("pdf");
		execCommand.add("-eSelectPdfVersion=1"); // PDF/A output
		String outputDir = PdfaConvert.applicationProps.getProperty(ApplicationConstants.OUTPUT_DIR_PROP);
		File outputDirectory = new File(outputDir);
		logger.debug("Have output directory: " + outputDirectory.getAbsolutePath());
		logger.debug("isDirectory: " + outputDirectory.isDirectory());
		String outputFilenameBase = inputFile.getName().substring(0, inputFile.getName().indexOf('.'));
		String generatedPdfFilename = outputFilenameBase + ".pdf";
		logger.debug("outputFilename: " + generatedPdfFilename);
		execCommand.add("-o"); // output location - directory or filename
		execCommand.add(outputDir + File.separator + generatedPdfFilename);
		execCommand.add(inputFile.getAbsolutePath());

		logger.debug("About to launch UnoconvTool, command = " + execCommand);
		ByteArrayOutputStream baos = processCommand(execCommand, null);
		String logFilename = outputDir + File.separator + TOOL_LOG_FILE_NAME;
		logApplicationOutput(logFilename, baos);
		
		File pdfaOutputFile = retrieveGeneratedFile(outputDir, generatedPdfFilename);
		PdfaConverterOutput converterOutput = new PdfaConverterOutput(pdfaOutputFile);		
		logger.debug("Finished running " + TOOL_NAME);
		return converterOutput;
	}

}
