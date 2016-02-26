#Batch Transformer

The purpose of this tool is to provide a wrapper around the [Saxon XSLT processor](http://www.saxonica.com/welcome/welcome.xml) that can be accessed from the command line and enables a batch of files to be processed using a single command and a single instance of Saxon.

You will need [Apache Maven](https://maven.apache.org/) and the [Java SE 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html) to build this tool. 

To build run `mvn clean compile assembly:single` from the root of this project. This will create an executable jar file in the target directory which can then be used from the command line as follows:
 
    java -jar target/batch-transformer-1.0-SNAPSHOT-jar-with-dependencies.jar html src/test/resources src/test/resources src/test/resources/test.xsl name=Rob

The available command line paramters are as follows:

- **html | xml** (required = yes) - specifies the output format as either XML or HTML
- **source directory** (required = yes) - the URL of the input directory - can be relative or absolute
- **target directory** (required = yes) - the URL of the output directory - can be relative or absolute
- **path to XSL file** (required = yes) - the URL of the XSL transformation to be used - can be relative or absolute 
- **in prefix** (required = no) - the prefix of the XML files in the source directory which are to be transformed - other XML files are ignored 
- **out prefix** (required = no) - a prefix to replace the input prefix in the output files 
- **XSL parameters** (required = no) - a list of parameters to be passed to the XSL transformation in the format param-name=param-value