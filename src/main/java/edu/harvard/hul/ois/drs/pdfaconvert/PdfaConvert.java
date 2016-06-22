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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.tools.PdfaConvertable;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.calibre.CalibreTool;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.pdfapilot.PdfaPilotTool;
import edu.harvard.hul.ois.drs.pdfaconvert.tools.unoconv.UnoconvTool;

/**
 * Hello world!
 */
public class PdfaConvert {

	public static Properties applicationProps; // consider creating with Spring and injecting where needed

	private String unoconvHome;
	private String pdfaPilotHome;
	private String calibreHome;
	
	private static final String PARAM_I = "i";

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
	
	private static final Logger logger;

	static {
		System.out.println("About to initialize Log4j");
		logger = LogManager.getLogger();
		System.out.println("Finished initializing Log4j");
		VALID_FILE_TYPES = Arrays.asList(DOC_TYPE, DOCM_TYPE, DOCX_TYPE, EPUB_TYPE, ODT_TYPE, PDF_TYPE, RTF_TYPE, WP_TYPE, WPD_TYPE );
	}
	
	public static void main(String[] args) throws IOException {
		
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
		if (cmd.hasOption('v')) {
			// TODO: expand this section
			System.out.println("Here's the version info... TODO...");
			System.out.println("Exiting...");
			System.exit(0);
		}
		
		// print help info
		if (cmd.hasOption('h')) {
			// TODO: expand this section
			System.out.println("Here's the help info... TODO...");
			System.out.println("Exiting...");
			System.exit(0);
		}

		// input parameter
		if (cmd.hasOption(PARAM_I)) {
			String input = cmd.getOptionValue(PARAM_I);
			boolean hasValue = cmd.hasOption(PARAM_I);
			logger.debug("*** Has option " + PARAM_I + " value: [" + hasValue + "]****");
			String paramVal = cmd.getOptionValue(PARAM_I);
			logger.debug("*** value of option: [" + paramVal + "] ****");

			File inputFile = new File(input);
			if (!inputFile.exists()) {
				logger.warn(input + " does not exist or is not readable.");
				System.exit(1);
			}
			
			PdfaConvert convert = new PdfaConvert();
			if (inputFile.isDirectory()) {
				if (inputFile.listFiles() == null || inputFile.listFiles().length < 1) {
					logger.warn("Input directory is empty, nothing to process.");
					System.exit(1);
				} else {
					logger.debug("****Have directory: [" + inputFile.getAbsolutePath() + "] with file count: " + inputFile.listFiles().length);
					DirectoryStream<Path> dirStream = null;
					try {
						dirStream = Files.newDirectoryStream(inputFile.toPath());
						for (Path filePath : dirStream) {
							logger.debug("****Have file name: " + filePath.toString());
							// Note: only handling files, not recursively going into sub-directories
							if (filePath.toFile().isFile()) {
								convert.examine(filePath.toFile());
							} else {
								logger.warn("Not a file so not processing: " + filePath.toString());
							}
						}
					} catch (Exception e) {
						logger.error("Caught exception: " + e);
					} finally {
						if (dirStream != null) {
							dirStream.close();
						}
					}
				}
			} else {
				logger.debug("About to process file: " + inputFile.getPath());
				convert.examine(inputFile);
			}
		} else {
			System.err.println("Missing required option: " + PARAM_I);
			// printHelp(options);
			System.exit(-1);
		}

		System.exit(0);
	}
	
	public PdfaConvert() throws IOException {
		super();

	    // First look for a system property pointing to a project properties file.
	    // This value can be either a file path, file protocol (e.g. - file:/path/to/file), or a URL (http://some/server/file).
	    // If this value either is does not exist or is not valid, the default file that comes with this application will be used for initialization.
		String environmentProjectPropsFile = System.getProperty(ApplicationConstants.ENV_PROJECT_PROPS);
	    URI projectPropsUri = null;
	    if (environmentProjectPropsFile != null) {
	        try {
	            projectPropsUri = new URI(environmentProjectPropsFile);
	            // log4j system needs a scheme in the URI so convert to file if necessary.
	            if (null == projectPropsUri.getScheme()) {
	                File log4jProperties = new File(environmentProjectPropsFile);
	                if (log4jProperties.exists() && log4jProperties.isFile()) {
	                    projectPropsUri = log4jProperties.toURI();
	                } else {
	                    // No scheme and not a file - yikes!!! Let's bail and use fall-back file.
	                    projectPropsUri = null;
	                    throw new URISyntaxException(environmentProjectPropsFile, "Not a valid file");
	                }
	            }
	        } catch (URISyntaxException e) {
	            // fall back to FITS-supplied file
	            System.err.println("Unable to load log4j.properties file: " + environmentProjectPropsFile + " -- reason: " + e.getReason());
	            System.err.println("Falling back to default project.properties file: " + ApplicationConstants.PROJECT_PROPS);
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
					logger.info("About to load project.properties from environment: " + envPropFile.getAbsolutePath());
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
				Enumeration<Object> keys = applicationProps.keys();
				logger.info("*** applicationProps: ");
				while (keys.hasMoreElements()) {
					Object key = keys.nextElement();
					logger.info("Key: {} -- value: {}", key, applicationProps.get(key));
				}
			} catch (IOException e) {
				logger.error("Could not load properties file: " + ApplicationConstants.PROJECT_PROPS, e);
				// couldn't load default properties so bail...
				throw e;
			}
		}
		
		// load converter application locations
		unoconvHome = applicationProps.getProperty(ApplicationConstants.UNOCONV_HOME_PROP);
		pdfaPilotHome = applicationProps.getProperty(ApplicationConstants.PDFA_PILOT_HOME_PROP);
		calibreHome = applicationProps.getProperty(ApplicationConstants.CALIBRE_HOME_PROP);
		logger.debug("Converter homes:\n unconvHome: {}, pdfaPilotHome: {}, calibreHome: {}", unoconvHome, pdfaPilotHome, calibreHome);
	}
	
	public void examine(File inputFile) {
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
				throw new RuntimeException("File type unknown. Cannot process: " + inputFile.getName());
		}
		converter.convert(inputFile);
		
	}
}