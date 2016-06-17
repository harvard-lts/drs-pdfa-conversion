/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert.tools.calibre;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.tools.AbstractPdfaConverterTool;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.PdfaConvertable;

/**
 * @author dan179
 *
 */
public class CalibreTool extends AbstractPdfaConverterTool implements PdfaConvertable {

	private static final String TOOL_NAME = "CalibreTool";
	private static final Logger logger = LogManager.getLogger();

	public CalibreTool(String calibreHome) {
		super();
		logger.debug("Entering C-tor for: " + CalibreTool.class.getSimpleName());
	}
}
