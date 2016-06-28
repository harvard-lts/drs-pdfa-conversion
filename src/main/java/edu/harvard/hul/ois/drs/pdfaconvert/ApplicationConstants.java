/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert;

/**
 * @author dan179
 */
public class ApplicationConstants {

	/** Name of version properties file for this application. */
	public static final String VERSION_PROPS = "version.properties";
	
	/** Key into version properties file for the version. */
	public static final String VERSION_KEY = "build.version";

	/** Name of default properties file for this application. */
	public static final String PROJECT_PROPS = "project.properties";

	/** Key into the project properties of environment variable to point to external project properties file. */
	public static final String ENV_PROJECT_PROPS = "PDFA_CONVERTER_PROPS";

	/** Key into the project properties of the path to the Unoconv application home. */
	public static final String UNOCONV_HOME_PROP = "unoconv_home";

	/** PKey into the project properties of the path to the PdfaPilot application home. */
	public static final String PDFA_PILOT_HOME_PROP = "pdfaPilot_home";

	/** Key into the project properties of the path to the Calibre application home. */
	public static final String CALIBRE_HOME_PROP = "calibre_home";

	/** Key into the project properties of the output directory for converted files. */
	public static final String OUTPUT_DIR_PROP = "output_dir";
}
