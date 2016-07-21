/*
Copyright (c) 2016 by The President and Fellows of Harvard College
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permission and limitations under the License.
*/
package edu.harvard.hul.ois.drs.pdfaconvert.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.harvard.hul.ois.drs.pdfaconvert.ApplicationConstants;
import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConvert;
import edu.harvard.hul.ois.drs.pdfaconvert.PdfaConverterOutput;

/**
 * These are integration tests needing configuration in src/test/resources/project.properties of
 * external applications so should only have @Ignore commented-out for local development testing.
 * 
 * @author dan179
 */
@Ignore // ALWAYS uncomment before saving this class -- This is an integration test!
public class PdfaConvertIntegrationTest {

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
	
	/**
	 * This test needs an environment-specific setting pointing to the home location for Unoconv.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testExamineDocx() throws IOException, URISyntaxException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String inputFilename = "TrivialDocument.docx";
		URL fileUrl = loader.getResource(TEST_FILE_DIR + File.separator + inputFilename);
		File inputFile = new File(fileUrl.toURI());
		assertNotNull(inputFile);
		assertTrue(inputFile.exists());
		assertTrue(inputFile.isFile());
		assertTrue(inputFile.canRead());

		PdfaConverterOutput output = converter.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		String expectedFilename = basicFilename + ".pdf";
		File outputFile = new File(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.OUTPUT_DIR_PROP) + File.separator + expectedFilename);
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
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String inputFilename = "Calibre_has_tables.epub";
		URL fileUrl = loader.getResource(TEST_FILE_DIR + File.separator + inputFilename);
		File inputFile = new File(fileUrl.toURI());
		assertNotNull(inputFile);
		assertTrue(inputFile.exists());
		assertTrue(inputFile.isFile());
		assertTrue(inputFile.canRead());

		PdfaConverterOutput output = converter.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		String expectedFilename = basicFilename + ".pdf";
		File outputFile = new File(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.OUTPUT_DIR_PROP) + File.separator + expectedFilename);
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
	@Test
	public void testExaminePdf() throws IOException, URISyntaxException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String inputFilename = "Has_document_properties.pdf";
		URL fileUrl = loader.getResource(TEST_FILE_DIR + File.separator + inputFilename);
		File inputFile = new File(fileUrl.toURI());
		assertNotNull(inputFile);
		assertTrue(inputFile.exists());
		assertTrue(inputFile.isFile());
		assertTrue(inputFile.canRead());

		converter.examine(inputFile);
		String basicFilename = inputFilename.substring(0, inputFilename.indexOf('.'));
		File outputFile = new File(PdfaConvert.getApplicationProperties().getProperty(ApplicationConstants.OUTPUT_DIR_PROP) + File.separator + basicFilename + ".pdf");
		assertNotNull(outputFile);
		assertTrue(outputFile.exists());
		assertTrue(outputFile.isFile());
	}
	
}
