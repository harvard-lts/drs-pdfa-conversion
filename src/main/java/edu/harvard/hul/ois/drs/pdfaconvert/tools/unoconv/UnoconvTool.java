/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert.tools.unoconv;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
 */
public class UnoconvTool extends AbstractPdfaConverterTool implements PdfaConvertable {

	private List<String> unixCommand = new ArrayList<String>();

	private static final String TOOL_NAME = "UnoconvTool";
	private boolean enabled = true;

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

	/* (non-Javadoc)
	 * @see edu.harvard.hul.ois.drs.pdfaconvert.tools.unoconv.PdfaConvertable#extractInfo(java.io.File)
	 */
	public void convert(File inputFile) {
        logger.debug(TOOL_NAME + ".extractInfo() starting on file: [" + inputFile.getName() + "]");
        logger.debug("file exists: " + inputFile.exists());
        logger.debug("file absolute path: " + inputFile.getAbsolutePath());

		List<String> execCommand = new ArrayList<String>();
		execCommand.addAll(unixCommand);

//		execCommand.add("-h");
		execCommand.add("-vv"); // for verbosity
		execCommand.add("-f"); // PDF output format
		execCommand.add("pdf");
		execCommand.add("-eSelectPdfVersion=1"); // PDF/A output
		String outputDir = PdfaConvert.applicationProps.getProperty(ApplicationConstants.OUTPUT_DIR_PROP);
		logger.debug("Have output directory: " + outputDir);
		String outputFilename = inputFile.getName().substring(0, inputFile.getName().indexOf('.')); // '.pdf' suffix automatically added
		logger.debug("outputFilename: " + outputFilename);
		execCommand.add("-o"); // output location - directory or filename
		execCommand.add(outputDir + outputFilename);
		execCommand.add(inputFile.getAbsolutePath());

		logger.debug("Launching UnoconvTool, command = " + execCommand);
        
//		String execOut = exec(execCommand, null);
//		logger.info("*** unoconv output:\n" + execOut);
//		String[] outParts = execOut.split("\n");
//		logger.debug("outParts size: {}", outParts.length);
//		String format = null;
//		for(String s : outParts) {
//			s = s.toLowerCase();
//			String[] lineParts = s.split("\t");
//			if(lineParts[0].equalsIgnoreCase("filetype")) {
//				format = lineParts[1].trim();
//				logger.debug("format: {}", format);
//				break;
//			}
//		}
		
		ByteArrayOutputStream baos = processCommand(execCommand, null);
		try {
//			FileWriter fw = new FileWriter(inputFile.getName() + ".txt");
//			fw.write(execOut);
//			fw.close();
			FileOutputStream outFile = new FileOutputStream("unoconv-output.txt");
			outFile.write(baos.toByteArray());
			outFile.flush();
			outFile.close();
		} catch(IOException ioe) {
			logger.error("Uh oh...", ioe);
		}
		logger.debug("Finished running " + TOOL_NAME);
		
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean value) {
		enabled = value;
	}


}
