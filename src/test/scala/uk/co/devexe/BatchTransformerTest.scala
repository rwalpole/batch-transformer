package uk.co.devexe

import org.junit.{After, Assert, Test}

import java.io.File
import scala.collection.mutable

/**
  * Created by Rob Walpole on 24/02/2016.
  */
class BatchTransformerTest {

  val sourceDir = "src/test/resources"
  val targetDir = "src/test/resources"
  val xsltPath = "src/test/resources/test.xsl"
  val testOutputFile1 = new File(targetDir, "test-1.html")
  val testOutputFile2 = new File(targetDir, "test-2.html")

  @Test
  def testRun(): Unit = {
    val output = "html"
    val params = new mutable.HashMap[String, String]
    params += ("name" -> "Rob")
    params += ("year" -> "2016")
    val transformer = BatchTransformer(sourceDir, targetDir, xsltPath, output)
    transformer.transform(Some(params), None, None)
    Assert.assertTrue(testOutputFile1.exists)
    Assert.assertTrue(testOutputFile2.exists)
  }

  @Test
  def testMain(): Unit = {
    BatchTransformer.main(Array("html", sourceDir, targetDir, xsltPath, "test", "name=Rob", "year=2016"))
  }

  @After
  def tearDown(): Unit = {
    testOutputFile1.deleteOnExit()
    testOutputFile2.deleteOnExit()
  }

}
