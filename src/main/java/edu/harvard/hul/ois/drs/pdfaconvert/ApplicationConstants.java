/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert;

/**
 * @author dan179
 */
public class ApplicationConstants {

	/** Name of default properties file for this application. */
	public static final String PROJECT_PROPS = "project.properties";

	/** Name of environment variable to point to external project properties file. */
	public static final String ENV_PROJECT_PROPS = "PDFA_CONVERTER_PROPS";

	/** Path in project properties file to the Unoconv application home. */
	public static final String UNOCONV_HOME_PROP = "unoconv_home";

	/** Path in project properties file to the PdfaPilot application home. */
	public static final String PDFA_PILOT_HOME_PROP = "pdfaPilot_home";

	/** Path in project properties file to the Calibre application home. */
	public static final String CALIBRE_HOME_PROP = "calibre_home";

	/** Output directory for converted files. */
	public static final String OUTPUT_DIR_PROP = "output_dir";
}
