package edu.harvard.hul.ois.drs.pdfaconvert.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConvert;

/**
 * These are integration tests needing configuration in src/test/resources/project.properties of
 * external applications so should only have @Ignore commented-out for local development testing.
 * 
 * @author dan179
 */
@Ignore
public class PdfaConvertIntegrationTest {

	private static final Logger logger = LogManager.getLogger();
	
	/**
	 * This test needs an environment-specific setting pointing to the home location for Unoconv.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testExamineDocx() throws IOException, URISyntaxException {
		String inputFilename = "TrivialDocument.docx";
		PdfaConvert convert = new PdfaConvert();
		File inputFile = new File("src/test/resources/test-files/" + inputFilename);
		if (!inputFile.exists() || !inputFile.isFile()) {
			fail(inputFile.getAbsolutePath() + " - either does not exist or is not a file.");
		}
		convert.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		File outputFile = new File("target/" + basicFilename + ".pdf");
		assertNotNull(outputFile);
		assertTrue(outputFile.exists());
		assertTrue(outputFile.isFile());
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
		PdfaConvert convert = new PdfaConvert();
		File inputFile = new File("src/test/resources/test-files/" + inputFilename);
		if (!inputFile.exists() || !inputFile.isFile()) {
			fail(inputFile.getAbsolutePath() + " - either does not exist or is not a file.");
		}
		convert.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		File outputFile = new File("target/" + basicFilename + ".pdf");
		assertNotNull(outputFile);
		assertTrue(outputFile.exists());
		assertTrue(outputFile.isFile());
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
		PdfaConvert convert = new PdfaConvert();
		File inputFile = new File("src/test/resources/test-files/" + inputFilename);
		if (!inputFile.exists() || !inputFile.isFile()) {
			fail(inputFile.getAbsolutePath() + " - either does not exist or is not a file.");
		}
		convert.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		File outputFile = new File("target/" + basicFilename + ".pdf");
		assertNotNull(outputFile);
		assertTrue(outputFile.exists());
		assertTrue(outputFile.isFile());
	}
	
}
