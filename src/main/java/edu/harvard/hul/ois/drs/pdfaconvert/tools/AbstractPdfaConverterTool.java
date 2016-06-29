/**
 * 
 */
package edu.harvard.hul.ois.drs.pdfaconvert.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.GeneratedFileUnavailableException;
import edu.harvard.hul.ois.drs.pdfaconvert.util.StreamGobbler;

/**
 * This code taken from FITS.
 * Consider making a shared library.
 * 
 * @author dan179
 */
public abstract class AbstractPdfaConverterTool {

	private static final Logger logger = LogManager.getLogger();

	protected AbstractPdfaConverterTool() {
		super();
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

	/*
	 * Log output from application performing the conversion, appending output if file already exists.
	 */
	protected void logApplicationOutput(String outputFilePath, ByteArrayOutputStream baos) {
		try {
			FileOutputStream outFile = new FileOutputStream(outputFilePath, true);
			outFile.write(baos.toByteArray());
			outFile.flush();
			outFile.close();
		} catch(IOException ioe) {
			logger.error("Problem writing to application logging output:", ioe);
		}
	}

	/**
	 * 
	 * @param outputDirectory
	 * @param outputFilename
	 * @throws GeneratedFileUnavailableException If the file is not available to be returned.
	 */
	protected File retrieveGeneratedFile(String outputDirectory, String outputFilename) {
		String filePath = outputDirectory + File.separator + outputFilename;
		File generatedFile = new File(filePath);
		if ( !generatedFile.isFile() || !generatedFile.canRead()) {
			throw new GeneratedFileUnavailableException("The generated file [" + generatedFile + "] is not available to be returned.");
		}
		return generatedFile;
	}
}
