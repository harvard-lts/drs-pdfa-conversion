package edu.harvard.hul.ois.drs.pdfaconvert;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class PdfaConvertTest {

	/*
	 * Directory where test files are stored within src/test/resources/
	 */
	private static final String TEST_FILE_DIR = "test-files";
	
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
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullInputFile() {
		converter.examine(null);
	}
	
	@Test(expected=UnknownFileTypeException.class)
	public void testWrongFileType() throws URISyntaxException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL fileUrl = loader.getResource(TEST_FILE_DIR + File.separator + "test-file.txt");
		File inputFile = new File(fileUrl.toURI());
		assertNotNull(inputFile);
		assertTrue(inputFile.exists());
		assertTrue(inputFile.isFile());
		assertTrue(inputFile.canRead());
		
		converter.examine(inputFile);
	}
	
}
