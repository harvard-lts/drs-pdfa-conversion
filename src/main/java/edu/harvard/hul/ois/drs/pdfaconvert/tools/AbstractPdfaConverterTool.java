/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.harvard.hul.ois.drs.pdfaconvert.util.StreamGobbler;

/**
 * This code taken from FITS.
 * Consider making a shared library.
 * 
 * @author dan179
 */
public abstract class AbstractPdfaConverterTool {

	protected AbstractPdfaConverterTool() {
		super();
	}

	protected String exec(List<String> cmd, File directory) throws RuntimeException {
		String output = null;
		ByteArrayOutputStream bos = processCommand(cmd, directory);
			output = new String(bos.toByteArray());
		return output;
	}
	
	protected ByteArrayOutputStream processCommand(List<String> cmd, File directory) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			//Runtime rt = Runtime.getRuntime();
			//Process proc = rt.exec(cmd.toString());
	
			ProcessBuilder builder = new ProcessBuilder(cmd);
			if(directory != null) {
				builder.directory(directory);
			}
			Process proc = builder.start();
	
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(),bos);
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(),bos);
			errorGobbler.start();
			outputGobbler.start();
			proc.waitFor();
		    errorGobbler.join();
		    outputGobbler.join();
		    //output = sb.toString();
		    bos.flush();
		}
		catch (Exception e) {
			throw new RuntimeException("Error calling external command line routine",e);
		}
		finally {
			try {
				bos.close();
			} catch (IOException e) {
				throw new RuntimeException("Error closing external command line output stream",e);
			}
		}
		return bos;
	}
}
