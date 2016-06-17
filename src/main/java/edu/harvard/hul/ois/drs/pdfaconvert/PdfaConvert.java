package edu.harvard.hul.ois.drs.pdfaconvert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
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
	
	private static final Logger logger = LogManager.getLogger();

	static {
		VALID_FILE_TYPES = Arrays.asList(DOC_TYPE, DOCM_TYPE, DOCX_TYPE, EPUB_TYPE, ODT_TYPE, PDF_TYPE, RTF_TYPE, WP_TYPE, WPD_TYPE );
	}
	
	public static void main(String[] args) throws IOException {
		
		logger.info("Entering main()");

		// WIP: the following command line code was pulled from FITS
		Options options = new Options();
		options.addOption(PARAM_I, true, "input file");
		options.addOption("v", false, "print version information");
//		OptionGroup outputOptions = new OptionGroup();
//		Option stdxml = new Option("x", false, "convert FITS output to a standard metadata schema");
//		Option combinedStd = new Option("xc", false, "output using a standard metadata schema and include FITS xml");
//		outputOptions.addOption(stdxml);
//		outputOptions.addOption(combinedStd);
//		options.addOptionGroup(outputOptions);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("Uh oh...");
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// input parameter
		if (cmd.hasOption(PARAM_I)) {
			String input = cmd.getOptionValue(PARAM_I);
			boolean hasValue = cmd.hasOption(PARAM_I);
			System.out.println("*** Has option " + PARAM_I + " value: [" + hasValue + "]****");
			String paramVal = cmd.getOptionValue(PARAM_I);
			System.out.println("*** value of option: [" + paramVal + "] ****");

			File inputFile = new File(input);
			if (!inputFile.exists()) {
				System.err.println(input + " does not exist or is not readable.");
				System.exit(1);
			}

		    String path = inputFile.getPath().toLowerCase();
			String ext = path.substring(path.lastIndexOf(".") + 1);

			PdfaConvert convert = new PdfaConvert();
			convert.examine(inputFile);

		} else {
			System.err.println("Invalid CLI options");
			// printHelp(options);
			System.exit(-1);
		}

		System.exit(0);
	}
	
	public PdfaConvert() throws IOException {
		super();

		// load properties
		applicationProps = new Properties();
		File props = new File(ApplicationConstants.PROJECT_PROPS);
		Reader reader = new FileReader(props);
		applicationProps.load(reader);
		// load converter application locations
		unoconvHome = applicationProps.getProperty(ApplicationConstants.UNOCONV_HOME_PROP);
		pdfaPilotHome = applicationProps.getProperty(ApplicationConstants.PDFA_PILOT_HOME_PROP);
		calibreHome = applicationProps.getProperty(ApplicationConstants.CALIBRE_HOME_PROP);
		logger.debug("Converter homes:\n unconvHome: {}, pdfaPilotHome: {}, calibreHome: {}", unoconvHome, pdfaPilotHome, calibreHome);
	}
	
	public void examine(File inputFile) {
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
