package br.com.unifaj.model

import org.apache.spark.sql.SparkSession

case class ProcessInput(InputFile: String,
                        OutputFile:String,
                        SparkSession: SparkSession){}
