/*
Copyright (c) 2016 by The President and Fellows of Harvard College
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at:
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permission and limitations under the License.
*/
package edu.harvard.hul.ois.drs.pdfaconvert;

/**
 * @author dan179
 */
public class ApplicationConstants {

	/** Name of version properties file for this application. */
	public static final String VERSION_PROPS = "version.properties";
	
	/** Key into version properties file for the version. */
	public static final String VERSION_KEY = "build.version";

	/** Name of default properties file for this application. */
	public static final String PROJECT_PROPS = "project.properties";

	/** Key into the project properties of environment variable to point to external project properties file. */
	public static final String ENV_PROJECT_PROPS = "PDFA_CONVERTER_PROPS";

	/** Key into the project properties of the path to the Unoconv application home. */
	public static final String UNOCONV_HOME_PROP = "unoconv_home";

	/** PKey into the project properties of the path to the PdfaPilot application home. */
	public static final String PDFA_PILOT_HOME_PROP = "pdfaPilot_home";

	/** Key into the project properties of the path to the Calibre application home. */
	public static final String CALIBRE_HOME_PROP = "calibre_home";

	/** Key into the project properties of the output directory for converted files. */
	public static final String OUTPUT_DIR_PROP = "output_dir";
}
