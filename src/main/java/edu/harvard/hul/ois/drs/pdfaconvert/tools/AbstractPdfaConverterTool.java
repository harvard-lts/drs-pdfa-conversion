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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.harvard.hul.ois.drs.pdfaconvert.ExternalToolException;
import edu.harvard.hul.ois.drs.pdfaconvert.GeneratedFileUnavailableException;
import edu.harvard.hul.ois.drs.pdfaconvert.util.StreamGobbler;

/**
 * This code taken from FITS.
 * Consider making a shared library.
 * 
 * @author dan179
 */
public abstract class AbstractPdfaConverterTool implements PdfaConvertable {

	private static final Logger logger = LogManager.getLogger();

	protected AbstractPdfaConverterTool() {
		super();
	}
	
	abstract protected String getToolName();
	
	/**
	 * Executes the command on the external tool using the supplied directory if not <code>null</code>.
	 * 
	 * @param cmd - The command to execute
	 * @param directory - The directory where to execute the command if not <code>null</code>.
	 * @return The output from the executed tool.
	 * @throws ExternalToolException - If there is a problem executing the command on the external tool.
	 */
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
		    bos.flush();
		}
		catch (IOException | InterruptedException e) {
			throw new ExternalToolException("Error calling external command line tool: " + getToolName(), e);
		}
		finally {
			try {
				bos.close();
			} catch (IOException e) {
				// nothing really to do except log and proceed
				logger.error("Couldn't close ByteArrayOutputStream", e);;
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
