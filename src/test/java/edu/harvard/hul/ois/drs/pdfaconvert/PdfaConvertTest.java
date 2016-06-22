package edu.harvard.hul.ois.drs.pdfaconvert;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class PdfaConvertTest {

	private static final Logger logger = LogManager.getLogger();
	
	@Test
    public void testPdfaConvert() {
		logger.debug("Entering trivial test");
        assertTrue( true );
    }
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullFile() {
		try {
			PdfaConvert pdfaConvert = new PdfaConvert();
			pdfaConvert.examine(null);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
}
