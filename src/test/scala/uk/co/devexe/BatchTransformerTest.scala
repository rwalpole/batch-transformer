package uk.co.devexe

import org.junit.Test

import scala.collection.mutable

/**
  * Created by walpolrx on 24/02/2016.
  */
class BatchTransformerTest {

  @Test
  def testRun() = {
    val sourceDir = "src/test/resources"
    val targetDir = "src/test/resources"
    val xsltPath = "src/test/resources/ptv.xsl"
    val output = "html"
    val params = new mutable.HashMap[String, String]
    params += ("target" -> "resources")
    val transformer = new BatchTransformer(sourceDir, targetDir, xsltPath, output, Some(params), None, None)
    transformer.run
  }

}
