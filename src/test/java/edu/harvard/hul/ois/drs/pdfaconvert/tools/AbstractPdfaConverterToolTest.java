/*
Copyright (c) 2016 by The President and Fellows of Harvard College
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permission and limitations under the License.
*/
package edu.harvard.hul.ois.drs.pdfaconvert.tools;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

			@Override
			protected String getToolName() {
				return "Test AbstractPdfaConverterTool";
			}
			
		};
	}
	
}
