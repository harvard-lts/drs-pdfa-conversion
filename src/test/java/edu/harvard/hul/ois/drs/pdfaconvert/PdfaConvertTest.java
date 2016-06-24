package edu.harvard.hul.ois.drs.pdfaconvert;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class PdfaConvertTest {

	private static final Logger logger = LogManager.getLogger();
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullInputFile() {
		try {
			PdfaConvert pdfaConvert = new PdfaConvert();
			pdfaConvert.examine(null);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
}
