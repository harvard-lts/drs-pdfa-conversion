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
import java.io.StringWriter;
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
	
	// the sub-directory within the output directory for storing converted documents
	private File outputDir;

	private static final Logger logger = LogManager.getLogger();

	protected AbstractPdfaConverterTool(File outputDir) {
		super();
		this.outputDir = outputDir;
	}
	
	abstract protected String getToolName();
	
	/**
	 * Output directory for converted files. May be a sub-directory of the value configured in properties file.
	 * Does not contain a trailing slash character.
	 * 
	 * @return The path to directory where converted file will be placed.
	 */
	protected String getOutputDirectory() {
		return outputDir.getAbsolutePath();
	}
	
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
		    int exitCode = proc.exitValue();
		    if (exitCode != 0) {
		    	throw new ExternalToolException("Error executing external command line tool: " + getToolName() + " -- with exit code: " + exitCode);
		    }
		}
		catch (IOException | InterruptedException e) {
			throw new ExternalToolException("Error executing external command line tool: " + getToolName(), e);
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
	
	protected String getToolLoggingOutput(ByteArrayOutputStream baos) {
		StringWriter sw = new StringWriter();
		sw.append(baos.toString());
		return sw.toString();
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
