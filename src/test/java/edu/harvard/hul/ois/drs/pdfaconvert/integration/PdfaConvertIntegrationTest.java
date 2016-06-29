package edu.harvard.hul.ois.drs.pdfaconvert.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConvert;
import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConverterOutput;

/**
 * These are integration tests needing configuration in src/test/resources/project.properties of
 * external applications so should only have @Ignore commented-out for local development testing.
 * 
 * @author dan179
 */
@Ignore // ALWAYS comment out before saving this class -- This is an integration test!
public class PdfaConvertIntegrationTest {

	// Directory where all test input files are stored
	private static final String TEST_FILE_DIR =
			"src" + File.separator + "test" + File.separator +
			"resources" + File.separator + "test-files" + File.separator;
	
	private static final Logger logger = LogManager.getLogger();
	
	private static PdfaConvert converter = null;
	
	/**
	 * Only need one main class to perform all tests
	 * @throws IOException
	 */
	@BeforeClass
	public static void initClass() throws IOException {
		converter = new PdfaConvert();
	}
	
	/**
	 * This test needs an environment-specific setting pointing to the home location for Unoconv.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testExamineDocx() throws IOException, URISyntaxException {
		String inputFilename = "TrivialDocument.docx";
		File inputFile = new File(TEST_FILE_DIR + inputFilename);
		if (!inputFile.exists() || !inputFile.isFile()) {
			fail(inputFile.getAbsolutePath() + " - either does not exist or is not a file.");
		}
		PdfaConverterOutput output = converter.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		String expectedFilename = basicFilename + ".pdf";
		File outputFile = new File("target" + File.separator + expectedFilename);
		assertNotNull(outputFile);
		assertTrue(outputFile.exists());
		assertTrue(outputFile.isFile());
		File returnedFile = output.getPdfaConvertedFile();
		assertNotNull(returnedFile);
		assertEquals("Returned filename not same as expected", expectedFilename, returnedFile.getName());
	}
	
	/**
	 * This test needs an environment-specific setting pointing to the home location for Calibre.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testExamineEpub() throws IOException, URISyntaxException {
		String inputFilename = "Calibre_has_tables.epub";
		File inputFile = new File(TEST_FILE_DIR + inputFilename);
		if (!inputFile.exists() || !inputFile.isFile()) {
			fail(inputFile.getAbsolutePath() + " - either does not exist or is not a file.");
		}
		PdfaConverterOutput output = converter.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		String expectedFilename = basicFilename + ".pdf";
		File outputFile = new File("target" + File.separator + expectedFilename);
		assertNotNull(outputFile);
		assertTrue(outputFile.exists());
		assertTrue(outputFile.isFile());
		File returnedFile = output.getPdfaConvertedFile();
		assertNotNull(returnedFile);
		assertEquals("Returned filename not same as expected", expectedFilename, returnedFile.getName());
	}
	
	/**
	 * This test needs an environment-specific setting pointing to the home location for pdfaPilot.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test(expected=UnsupportedOperationException.class) // cannot run until there is an available pdfaPilot application available
	public void testExaminePdf() throws IOException, URISyntaxException {
		String inputFilename = "Has_document_properties.pdf";
		File inputFile = new File(TEST_FILE_DIR + inputFilename);
		if (!inputFile.exists() || !inputFile.isFile()) {
			fail(inputFile.getAbsolutePath() + " - either does not exist or is not a file.");
		}
		converter.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		File outputFile = new File("target" + File.separator + basicFilename + ".pdf");
		assertNotNull(outputFile);
		assertTrue(outputFile.exists());
		assertTrue(outputFile.isFile());
	}
	
}
