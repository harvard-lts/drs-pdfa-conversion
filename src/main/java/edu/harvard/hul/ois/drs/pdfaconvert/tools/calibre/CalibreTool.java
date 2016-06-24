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
import edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.PdfaConvertable;

/**
 * @author dan179
 *
 */
public class CalibreTool extends AbstractPdfaConverterTool implements PdfaConvertable {

	private static final String TOOL_NAME = "CalibreTool";
	private static final String CALIBRE_COMMAND = "/ebook-convert";
	public static final Logger logger = LogManager.getLogger();
	
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
	
	/**
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.unoconv.PdfaConvertable#extractInfo(java.io.File)
	 */
	public void convert(File inputFile) {
        logger.debug(TOOL_NAME + ".extractInfo() starting on file: [" + inputFile.getName() + "]");
        logger.debug("file exists: " + inputFile.exists());
        logger.debug("file absolute path: " + inputFile.getAbsolutePath());
		List<String> execCommand = new ArrayList<String>();
		execCommand.addAll(unixCommand);
		execCommand.add(inputFile.getAbsolutePath()); // input file first
		String outputDir = PdfaConvert.applicationProps.getProperty(ApplicationConstants.OUTPUT_DIR_PROP);
		String outputFilename = inputFile.getName().substring(0, inputFile.getName().indexOf('.')); // '.pdf' suffix automatically added
		execCommand.add(outputDir + "/" + outputFilename + ".pdf");
		logger.debug("Launching CalibreTool, command = " + execCommand);

		
		ByteArrayOutputStream baos = processCommand(execCommand, null);
		String logFilename = outputDir + "/calibre-output.txt";
		logApplicationOutput(logFilename, baos);
		logger.debug("Finished running " + TOOL_NAME);
	}
}
