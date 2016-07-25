# PDF/A Converter Utility
Is a command line application that converts word processing document formats to PDF/A

This utility attempts to convert the following file types to PDF/A format:
- .doc
- .docx
- .epub
- .odt
- .pdf
- .rtf
- .wpd

It is necessary to have a set of external applications on the path of the system on which this is deploy.

For converting .doc, .docx, .odt, .rtf, and .wpd documents it is necessary to have both [LibreOffice](http://www.libreoffice.org/) and [unoconv](http://dag.wiee.rs/home-made/unoconv/). (unoconv serves as a wrapper for and calls out to LibreOffice.)

For converting .epub documents it is necessary to have an installation of Calibre.

For converting .pdf files (non-PDF/A) it is necessary to have the commercial product [pdfaPilot CLI](https://www.callassoftware.com/en/products/pdfapilot/?type=product&product=pdfapilotcli) by Callas Software.

## Build
This project is built with [Maven](https://maven.apache.org/). The final artifact is a ZIP file. This should be unpacked at which point can be configured and executed.

## Configuration
There is a configuration file for the application contained within the pdfa-converter.jar file and also in the source tree at src/main/resources/project.properties.

The project.properties file that is packaged with the application **WILL NOT** contain the correct setting.
This file should be copied and placed external to the application then referenced with a system property as follows: <br>
`-DPDFA_CONVERTER_PROPS=/path/to/customized/project.properties`

There are 3 properties for the locations of unoconv, Calibre, and pdfaPilot, respectively. Make sure LibreOffice is also installed and available on the system's Path.

'output_dir' is the location on the local system for placement of converted documents for ALL tools.

### Using pdfaPilot remotely
There are properties for referencing and using pdfaPilot remotely via SSH tunneling. Note: this assumes the local system has a public key set on the remote system to avoid manual login intervention when using the SSH command. The property 'pdfaPilotRemoteInputDir' and 'pdfaPilotRemoteOutputDir' represent the locations on the remote system where the application will SCP a file to be converted and and SCP to retrieve the converted file.

## Logging
The application uses [Log4j 2](http://logging.apache.org/log4j/2.x/). Though there is a log4j2.xml configuration file it is highly recommended to configure your own file and point to it with a system property with <br>
`-Dlog4j.configurationFile=/path/to/customized/log4j2.xml`

## Usage
The application is used at a command prompt by executing the main JAR file. The following example uses external application configuration and log4j files.

`java -DPDFA_CONVERTER_PROPS=/path/to/customized/project.properties -Dlog4j.configurationFile=/path/to/customized/log4j2.xml -jar pdfa-converter.jar -i /path/to/file/or/directory/`

### Input options:
-i -- Path to input file or directory containing multiple input files for conversion.<br>
-o -- (optional) The sub-directory withing the 'output-dir' property into which the converted files will be placed. This directory will be created if necessary.<br>
-v -- Display the version of the application.<br>
-h -- Display simple help text for the application.
