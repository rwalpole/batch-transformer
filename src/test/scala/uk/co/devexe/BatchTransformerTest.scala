package uk.co.devexe

import java.io.File

import org.junit.{Assert, After, Test}

import scala.collection.mutable

/**
  * Created by Rob Walpole on 24/02/2016.
  */
class BatchTransformerTest {

  val sourceDir = "src/test/resources"
  val targetDir = "src/test/resources"
  val xsltPath = "src/test/resources/test.xsl"
  val testOutputFile1 = new File(targetDir, "test1.html")
  val testOutputFile2 = new File(targetDir, "test2.html")

  @Test
  def testRun() {
    val output = "html"
    val params = new mutable.HashMap[String, String]
    params += ("name" -> "Rob")
    val transformer = new BatchTransformer(sourceDir, targetDir, xsltPath, output, Some(params), None, None)
    transformer.run
    Assert.assertTrue(testOutputFile1.exists)
    Assert.assertTrue(testOutputFile2.exists)
  }

  @After
  def tearDown() {
    testOutputFile1.deleteOnExit
    testOutputFile2.deleteOnExit

  }

}
