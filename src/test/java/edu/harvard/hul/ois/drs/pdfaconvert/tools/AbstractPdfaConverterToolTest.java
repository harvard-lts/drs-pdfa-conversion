/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert.tools;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Test;

import edu.harvard.hul.ois.drs.pdfaconvert.GeneratedFileUnavailableException;

/**
 * @author dan179
 */
public class AbstractPdfaConverterToolTest {

	// for creating a temporary directory in the transient "target" directory of the Maven build
	private static String tempDir = "target" + File.separator + "temp";
	private static String filename = "tempfile.txt";

	private static final Logger logger = LogManager.getLogger();
	
	/**
	 * Test that a file can be retrieved from its expected location.
	 */
	@Test
	public void testRetrieveGeneratedFile() {
		File testFile = null;
		try {
			File directory = new File(tempDir);
			directory.mkdir();
			testFile = new File(directory, filename);
			testFile.createNewFile();
		} catch (IOException e) {
			fail("Could not create file: " + e.getMessage());
		}
		AbstractPdfaConverterTool tool = createAbstractPdfaConverterTool();
		File retrievedFile = tool.retrieveGeneratedFile(tempDir, filename);
		assertNotNull(retrievedFile);
		assertTrue(retrievedFile.exists());
		assertTrue(retrievedFile.isFile());
		assertTrue(retrievedFile.canRead());
		boolean fileDeleted = testFile.delete();
		assertTrue(fileDeleted);
	}
	
	/**
	 * Tests that an exception is thrown when a file cannot be found in its expected location.
	 */
	@Test(expected=GeneratedFileUnavailableException.class)
	public void testRetrieveGeneratedFileError() {
		File testFile = null;
		try {
			File directory = new File(tempDir);
			directory.mkdir();
			testFile = new File(directory, filename);
			testFile.createNewFile();
		} catch (IOException e) {
			fail("Could not create file: " + e.getMessage());
		}
		AbstractPdfaConverterTool tool = createAbstractPdfaConverterTool();
		// attempt to retrieve file from a different location
		File retrievedFile = tool.retrieveGeneratedFile("temp2", filename);
		assertNotNull(retrievedFile);
		assertTrue(retrievedFile.exists());
		assertTrue(retrievedFile.isFile());
		assertTrue(retrievedFile.canRead());
	}
	
	private AbstractPdfaConverterTool createAbstractPdfaConverterTool() {
		return new AbstractPdfaConverterTool() {
		};
	}
	
	/**
	 * Delete all temporary files created during testing.
	 */
	@AfterClass
	public static void postTest() {
		File directory = new File(tempDir);
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File f : files) {
				f.delete();
			}
		}
		boolean tempDirDeleted = directory.delete();
		logger.debug("temporary directory: [{}] deleted: {}", tempDir, tempDirDeleted);
	}
	
}
