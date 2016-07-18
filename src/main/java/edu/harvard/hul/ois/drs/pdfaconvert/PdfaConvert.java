/*
Copyright (c) 2016 by The President and Fellows of Harvard College
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permission and limitations under the License.
*/
package edu.harvard.hul.ois.drs.pdfaconvert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.tools.PdfaConvertable;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.calibre.CalibreTool;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.pdfapilot.PdfaPilotTool;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.unoconv.UnoconvTool;

/**
 * Converts an word processing input document to a PDF/A document.
 */
public class PdfaConvert {

	public static Properties applicationProps; // consider creating with Spring and injecting where needed

	private String unoconvHome;
	private String pdfaPilotHome;
	private String calibreHome;

	private static String applicationVersion;

	// command line parameters
	private static final String PARAM_I = "i";
	private static final String PARAM_H = "h";
	private static final String PARAM_V = "v";

	private static final String DOC_TYPE = "doc";
	private static final String DOCM_TYPE = "docm";
	private static final String DOCX_TYPE = "docx";
	private static final String EPUB_TYPE = "epub";
	private static final String ODT_TYPE = "odt";
	private static final String PDF_TYPE = "pdf";
	private static final String RTF_TYPE = "rtf";
	private static final String WP_TYPE = "wp";
	private static final String WPD_TYPE = "wpd";

	private static List<String> VALID_FILE_TYPES;

	private static Logger logger;

	static {
		System.out.println("About to initialize Log4j");
        String log4jSystemProp = System.getProperty("log4j.configurationFile");
        if (log4jSystemProp != null) {
        	System.out.println("Attempting to load external log4j.configurationFile: " + log4jSystemProp);
        }
		logger = LogManager.getLogger();
		System.out.println("Finished initializing Log4j");
		VALID_FILE_TYPES = Arrays.asList(DOC_TYPE, DOCM_TYPE, DOCX_TYPE, EPUB_TYPE, ODT_TYPE, PDF_TYPE, RTF_TYPE,
				WP_TYPE, WPD_TYPE);
	}

	public static void main(String[] args) throws IOException {
		if (logger == null) {
			System.out.println("About to initialize Log4j");
			logger = LogManager.getLogger();
			System.out.println("Finished initializing Log4j");
		}

		logger.debug("Entering main()");

		// WIP: the following command line code was pulled from FITS
		Options options = new Options();
		Option inputFileOption = new Option(PARAM_I, true, "input file");
		options.addOption(inputFileOption);
		options.addOption("v", false, "print version information");
		options.addOption("h", false, "help information");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args, true);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// print version info
		if (cmd.hasOption(PARAM_V)) {
			setVersionFromFile();
			if (StringUtils.isEmpty(applicationVersion)) {
				applicationVersion = "<not set>";
				System.exit(1);
			}
			System.out.println("Version: " + applicationVersion);
			System.exit(0);
		}

		// print help info
		if (cmd.hasOption(PARAM_H)) {
			displayHelp();
			System.exit(0);
		}

		// input parameter
		if (cmd.hasOption(PARAM_I)) {
			String input = cmd.getOptionValue(PARAM_I);
			boolean hasValue = cmd.hasOption(PARAM_I);
			logger.debug("Has option {} value: [{}]", PARAM_I, hasValue);
			String paramVal = cmd.getOptionValue(PARAM_I);
			logger.debug("value of option: [{}] ****", paramVal);

			File inputFile = new File(input);
			if (!inputFile.exists()) {
				logger.warn("{} does not exist or is not readable.", input);
				System.exit(1);
			}

			PdfaConvert convert = new PdfaConvert();
			if (inputFile.isDirectory()) {
				if (inputFile.listFiles() == null || inputFile.listFiles().length < 1) {
					logger.warn("Input directory is empty, nothing to process.");
					System.exit(1);
				} else {
					logger.debug("Have directory: [{}] with file count: {}", inputFile.getAbsolutePath(), inputFile.listFiles().length);
					DirectoryStream<Path> dirStream = null;
						dirStream = Files.newDirectoryStream(inputFile.toPath());
						for (Path filePath : dirStream) {
							logger.debug("Have file name: {}", filePath.toString());
							// Note: only handling files, not recursively going into sub-directories
							if (filePath.toFile().isFile()) {
								// Catch possible exception for each file so can handle other files in directory.
								try {
									convert.examine(filePath.toFile());
								} catch (Exception e) {
									logger.error("Problem processing file: {} -- Error message: {}", filePath.getFileName(), e.getMessage());
								}
							} else {
								logger.warn("Not a file so not processing: {}", filePath.toString()); // could be a directory but not recursing
							}
						}
						dirStream.close();
				}
			} else {
				logger.debug("About to process file: {}", inputFile.getPath());
				try {
					convert.examine(inputFile);
				} catch (Exception e) {
					logger.error("Problem processing file: {} -- Error message: {}", inputFile.getName(), e.getMessage());
					logger.debug("Problem processing file: {} -- Error message: {}", inputFile.getName(), e.getMessage(), e);
				}
			}
		} else {
			System.err.println("Missing required option: " + PARAM_I);
			displayHelp();
			System.exit(-1);
		}

		System.exit(0);
	}
	
	private static void displayHelp() {
		System.out.println("PDF/A Utility help");
		System.out.println("-i follow by path to input file to process");
		System.out.println("-v for version of this application");
		System.out.println("-h to display this help");
	}

	  /*
	   * Called from either main() for stand-alone application usage or constructor
	   * when used by another program, this reads the properties file containing the
	   * current version of FITS.
	   *
	   * Precondition of this method is that the static FITS_HOME has been set via
	   * an environment variable, being passed into a constructor, or is just the current directory.
	   */
	  private static void setVersionFromFile() {

			// get version properties file
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try {
				InputStream resourceStream = loader.getResourceAsStream(ApplicationConstants.VERSION_PROPS);
				Properties versionProps = new Properties();
				versionProps.load(resourceStream);
				String version = versionProps.getProperty(ApplicationConstants.VERSION_KEY);
				if (StringUtils.isEmpty(version)) {
					version = "<not available>";
				}
				applicationVersion = version;
				logger.debug("{} version: {}", PdfaConvert.class.getSimpleName(), applicationVersion);
			} catch (IOException e) {
				logger.error("Could not load properties file: {}", ApplicationConstants.PROJECT_PROPS, e);
			}
	  }

	  /**
	   * 
	   * @throws IOException - If there is a problem reading in the application's properties file.
	   */
	public PdfaConvert() throws IOException {
		super();

		// Set the projects properties.
		// First look for a system property pointing to a project properties file.
		// This value can be either a file path, file protocol (e.g. - file:/path/to/file),
		// or a URL (http://some/server/file).
		// If this value either does not exist or is not valid, the default
		// file that comes with this application will be used for initialization.
		String environmentProjectPropsFile = System.getProperty(ApplicationConstants.ENV_PROJECT_PROPS);
		logger.info("Have project.properties from environment: {}", environmentProjectPropsFile);
		URI projectPropsUri = null;
		if (environmentProjectPropsFile != null) {
			try {
				projectPropsUri = new URI(environmentProjectPropsFile);
				// properties file needs a scheme in the URI so convert to file if necessary.
				if (null == projectPropsUri.getScheme()) {
					File projectProperties = new File(environmentProjectPropsFile);
					if (projectProperties.exists() && projectProperties.isFile()) {
						projectPropsUri = projectProperties.toURI();
					} else {
						// No scheme and not a file - yikes!!! Let's bail and
						// use fall-back file.
						projectPropsUri = null;
						throw new URISyntaxException(environmentProjectPropsFile, "Not a valid file");
					}
				}
			} catch (URISyntaxException e) {
				// fall back to default file
				logger.error("Unable to load properties file: {} -- reason: {}", environmentProjectPropsFile, e.getReason());
				logger.error("Falling back to default project.properties file: {}", ApplicationConstants.PROJECT_PROPS);
			}
		}

		applicationProps = new Properties();

		// load properties if environment value set
		if (projectPropsUri != null) {
			File envPropFile = new File(projectPropsUri);
			if (envPropFile.exists() && envPropFile.isFile() && envPropFile.canRead()) {
				Reader reader;
				try {
					reader = new FileReader(envPropFile);
					logger.info("About to load project.properties from environment: {}", envPropFile.getAbsolutePath());
					applicationProps.load(reader);
					logger.info("Success -- loaded properties file.");
				} catch (IOException e) {
					logger.error("Could not load environment properties file: {}", projectPropsUri, e);
					// set URI back to null so default prop file loaded
					projectPropsUri = null;
				}
			}
		}

		if (projectPropsUri == null) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try {
				InputStream resourceStream = loader.getResourceAsStream(ApplicationConstants.PROJECT_PROPS);
				applicationProps.load(resourceStream);
				logger.info("loaded default applicationProps: ");
			} catch (IOException e) {
				logger.error("Could not load properties file: {}", ApplicationConstants.PROJECT_PROPS, e);
				// couldn't load default properties so bail...
				throw e;
			}
		}
		
		logger.info("Have the following application properties:");
		Enumeration<Object> keys = applicationProps.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			logger.info("Key: {} -- value: {}", key, applicationProps.get(key));
		}

		// load converter application locations
		unoconvHome = applicationProps.getProperty(ApplicationConstants.UNOCONV_HOME_PROP);
		pdfaPilotHome = applicationProps.getProperty(ApplicationConstants.PDFA_PILOT_HOME_PROP);
		calibreHome = applicationProps.getProperty(ApplicationConstants.CALIBRE_HOME_PROP);
		String outputDir = PdfaConvert.applicationProps.getProperty(ApplicationConstants.OUTPUT_DIR_PROP);
		logger.debug("Converter homes:\n unconvHome: {}, pdfaPilotHome: {}, calibreHome: {}", unoconvHome,
				pdfaPilotHome, calibreHome);
		File outputDirFile = new File(outputDir);
		boolean outputDirExists = outputDirFile.exists();
		logger.debug("Output directory for PDF files and external application log files: {}  -- exists: {}", outputDir, outputDirExists);
		// ensure output directory exists
		if (!outputDirExists) {
			outputDirFile.mkdir();
			logger.debug("Created output directory: {}", outputDirFile.getAbsolutePath());
		}
		setVersionFromFile();
	}

	/**
	 * Converts the input file to PDF format using the appropriate application.
	 * 
	 * @param inputFile - the input file to convert
	 * @return PdfaConverterOutput - Contains the input converted to PDF/A and other relevant data.
	 * @throws GeneratedFileUnavailableException - If the generated file is either unavailable or unreadable.
	 * @throws UnknownFileTypeException - The input file extension cannot be processed into a PDF/A.
	 * @throws IllegalArgumentException - If the input is null.
	 * @throws ExternalToolException - When there is a problem with the external tool being executed.
	 */
	public PdfaConverterOutput examine(File inputFile) {
		if (inputFile == null) {
			logger.warn("Invalid null file -- no-op");
			throw new IllegalArgumentException("inputFile parameter is null.");
		}
		
	    String path = inputFile.getPath().toLowerCase();
		String ext = path.substring(path.lastIndexOf(".") + 1);
		
		PdfaConvertable converter;
		
		switch(ext) {
			case DOC_TYPE:
			case DOCM_TYPE:
			case DOCX_TYPE:
			case ODT_TYPE:
			case RTF_TYPE:
			case WP_TYPE:
			case WPD_TYPE:
				converter = new UnoconvTool(unoconvHome);
				break;
			case EPUB_TYPE:
				converter = new CalibreTool(calibreHome);
				break;
			case PDF_TYPE:
				converter = new PdfaPilotTool(pdfaPilotHome);
				break;
			default:
				throw new UnknownFileTypeException("File type unknown. Cannot process: " + inputFile.getName());
		}
		PdfaConverterOutput output = converter.convert(inputFile);
		return output;
	}
	
	public String getVersion() {
		return applicationVersion;
	}
}
