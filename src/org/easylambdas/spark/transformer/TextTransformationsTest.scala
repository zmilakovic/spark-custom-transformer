package org.easylambdas.spark.transformer

import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.ml.util.SchemaUtils
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.easylambdas.spark.common.SparkProgram

object TextTransformationsTest
    extends SparkProgram {

  /**
   * Main test method
   */
  def main(args: Array[String]): Unit = {

    /** Load the data */
    val data = spark.createDataFrame(Seq(
      "I like running when it is a sunny day.",
      "This is an example of Bag of Words model.",
      "Scala is a cool language for Data Science tasks")
      .map(Tuple1.apply)).toDF("text")

    /** Test custom Transformer */
    val customTransformer = new TextPreprocessorTransformer
    customTransformer.setInputCol("text").setOutputCol("newText").transform(data).show(3)

  }

}
