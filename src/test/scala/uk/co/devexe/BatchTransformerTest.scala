package uk.co.devexe

import java.io.File

import org.junit.{Ignore, Assert, After, Test}

import scala.collection.mutable

/**
  * Created by Rob Walpole on 24/02/2016.
  */
class BatchTransformerTest {

  val sourceDir = "/home/devexe/TresoritDrive/S1000D/S1000D_Issue_5.0/S1000D_Issue_5.0_Bike_Sample_Data_Set/Bike_Data_Set_for_Release_number_5.0/"
  val targetDir = "/home/devexe/Source/GitHub/rwalpole/publishing-engine/target/"
  val xsltPath = "/home/devexe/Source/GitHub/rwalpole/publishing-engine/Issue_5.0/xslt/pm.xsl"
  val testOutputFile1 = new File(targetDir, "test-1.html")
  val testOutputFile2 = new File(targetDir, "test-2.html")

  //@Ignore
  @Test
  def testRun() {
    val output = "html"
    //val params = new mutable.HashMap[String, String]
    //params += ("name" -> "Rob")
    //params += ("year" -> "2016")
    val transformer = BatchTransformer(sourceDir, targetDir, xsltPath, "html")
    transformer.transform(None, Some("PMC"),None)
    //Assert.assertTrue(testOutputFile1.exists)
    //Assert.assertTrue(testOutputFile2.exists)
  }

  @Test
  def testMain(): Unit = {
    //BatchTransformer.main(Array("html", sourceDir, targetDir, xsltPath, "test", "name=Rob", "year=2016"))
  }

  @After
  def tearDown() {
    testOutputFile1.deleteOnExit
    testOutputFile2.deleteOnExit

  }

}
