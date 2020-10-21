package br.com.unifaj.model

import org.apache.spark.sql.SparkSession

case class ProcessInput(InputDir: String,
                        InputFile: String,
                        OutputFile: String,
                        TableName: String,
                        SparkSession: SparkSession) {}
