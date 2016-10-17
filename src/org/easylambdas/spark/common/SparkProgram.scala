package org.easylambdas.spark.common

import org.apache.spark.sql.SparkSession

trait SparkProgram extends Serializable {

  //init spark
  val spark = SparkSession
    .builder
    .master("local[2]")
    .config("spark.sql.warehouse.dir", "file:////temp")
    .appName("SparkProgram")
    .getOrCreate()

  val sc = spark.sparkContext
  val sqlContext = spark.sqlContext

}