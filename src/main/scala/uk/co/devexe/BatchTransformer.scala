package uk.co.devexe

import java.io.{File, FilenameFilter}

import javax.xml.transform.stream.StreamSource
import net.sf.saxon.s9api._
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable

/**
  * The purpose of this tool is to provide a wrapper around the Saxon XSLT processor that can be accessed from the
  * command line and enables a batch of files to be processed using a single command and a single instance of Saxon.
  * Created by Rob Walpole on 23/02/2016.
  */
object BatchTransformer {

  def apply(sourcePath: String, targetPath: String, xsltPath: String, output: String): BatchTransformer = new BatchTransformer(sourcePath, targetPath, xsltPath, output)

  val LOG: Logger = LoggerFactory.getLogger(classOf[BatchTransformer])

  def main(args: Array[String]): Unit = {

    if(args.length < 4) {
      usage()
    } else {
      val output = args(0)
      val sourceDir = args(1)
      val targetDir = args(2)
      val xsltPath = args(3)
      val transformer = BatchTransformer(sourceDir, targetDir, xsltPath,output)
      args.length match {
        case 4 =>
          transformer.transform()
        case 5 =>
          if(args(4).contains("=")){
            val params = extractParams(args, 4)
            transformer.transform(params = Some(params))
          } else {
            val prefix = args(4)
            transformer.transform(prefixOpt = Some(prefix))
          }
        case 6 =>
          if(args(5).contains("=")){
            val prefix = args(4)
            val params = extractParams(args, 5)
            transformer.transform(params = Some(params), prefixOpt = Some(prefix))
          } else {
            val prefix = args(4)
            val outPrefix = args(5)
            transformer.transform(prefixOpt = Some(prefix), outPrefixOpt = Some(outPrefix))
          }
        case 7 =>
          val prefix = args(4)
          val outPrefix = args(5)
          val params = extractParams(args, 6)
          transformer.transform(Some(params), Some(prefix), Some(outPrefix))
        case _ => usage()
      }

        //}
//      } catch {
//        case ex: BatchTransformerException => LOG.error(ex.getMessage)
//      }
    }
  }

  private def extractParams(args: Array[String], index: Int): mutable.Map[String,String] = {
    val params = new mutable.HashMap[String,String]
    try{
      args.slice(index ,args.length) map { arg =>
        if(arg.contains("=")) {
          val param = arg.split("=")
          params += (param(0) -> param(1))
        }else{
          throw new RuntimeException()
        }
      }
    } catch {
       case ex: RuntimeException => usage()
    }
    params
  }

  def usage() {
    System.out.println("Usage:")
    System.out.println("\tjava -jar saxon-wrapper-1.0.jar html <source-directory> <target-directory> <path-to-xsl-file> [<in-prefix>] [<out-prefix>] [<param-name=param-value> ...]")
    System.out.println("\tjava -jar saxon-wrapper-1.0.jar xml <source-directory> <target-directory> <path-to-xsl-file> [<in-prefix>] [<out-prefix>] [<param-name=param-value> ...]")
  }

}

class BatchTransformer(sourcePath: String, targetPath: String, xsltPath: String, output: String) {

  val LOG: Logger = LoggerFactory.getLogger(classOf[BatchTransformer])

  def transform(params: Option[mutable.Map[String,String]] = None, prefixOpt: Option[String] = None, outPrefixOpt: Option[String] = None) {
    LOG.info("Executing transformation with source={} target={} xslt={} output={} params={} prefix={} output-prefix={}", sourcePath, targetPath, xsltPath, output, params, prefixOpt, outPrefixOpt)
    val proc = new Processor(false)
    val comp = proc.newXsltCompiler()
    val exec = comp.compile(new StreamSource(new File(xsltPath)))
    val trans = exec.load()
    val prefix = prefixOpt.getOrElse("")
    val outPrefix = outPrefixOpt.getOrElse("")

    val srcDir = new File(sourcePath)
    val targetDir = new File(targetPath)
    srcDir.listFiles(new XmlFilenameFilter(prefixOpt)) map { xmlFile =>
      params match {
        case Some(parameters) =>
          parameters map { param =>
            trans.setParameter(new QName(param._1), new XdmAtomicValue(param._2))
          }
        case None =>
      }
      val source = proc.newDocumentBuilder().build(new StreamSource(xmlFile))
      val target = getOutputFile(xmlFile, targetDir, output, prefix, outPrefix)
      trans.setInitialContextNode(source)
      trans.setDestination(getSerializer(target, proc))
      try {
        trans.transform()
      } catch {
        case ex: SaxonApiException => throw BatchTransformerException(cause = ex)
      }
      LOG.info("Output written to " + target.getPath)
    }
  }

  private def getSerializer(target: File, processor: Processor): Serializer = {
    val out = processor.newSerializer(target)
    out.setOutputProperty(Serializer.Property.METHOD, output)
    out.setOutputProperty(Serializer.Property.INDENT, "yes")
    out
  }

  private def getOutputFile(inFile: File, targetDir: File, output: String, inPrefix: String, outPrefix: String): File = {
    if(output.equals("html")) {
      if(!inPrefix.equals("") && !outPrefix.equals("")){
        new File(targetPath, inFile.getName.replaceAll("\\.(xml|XML)", ".html").replace(inPrefix, outPrefix))
      }else{
        new File(targetPath, inFile.getName.replaceAll("\\.(xml|XML)", ".html"))
      }
    }else{
      if(!inPrefix.equals("") && !outPrefix.equals("")){
        new File(targetPath, inFile.getName.replace(inPrefix,outPrefix))
      }else{
        if(inFile.getParentFile.getPath.equals(targetDir.getPath)) {
          throw BatchTransformerException(message = "Output file " + targetDir + File.separator + inFile.getName + " cannot be the same as input " + inFile.getPath)
        }
        new File(targetPath, inFile.getName)
      }
    }
  }

}

class XmlFilenameFilter(prefixOpt: Option[String]) extends FilenameFilter {
  override def accept(dir: File, name: String): Boolean = {
    if(name.toLowerCase.endsWith(".xml")){
      prefixOpt match {
        case Some(prefix) =>
          if(name.startsWith(prefix)) {
            true
          } else {
            false
          }
        case None => true
      }
    }else{
      false
    }
  }
}

case class BatchTransformerException(message: String = null, cause: Throwable = null) extends Exception(message, cause)
