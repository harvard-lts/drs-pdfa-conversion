/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert.tools.pdfapilot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.PdfaConvertable;

/**
 * @author dan179
 */
public class PdfaPilotTool extends AbstractPdfaConverterTool implements PdfaConvertable {

	private static final String TOOL_NAME = "PdfaPilotTool";
	private static final Logger logger = LogManager.getLogger();

	public PdfaPilotTool(String pdfaPilotHome) {
		super();
		logger.debug("Entering C-tor for: " + PdfaPilotTool.class.getSimpleName());
	}

}
