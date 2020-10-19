package br.com.unifaj

import java.time.LocalDateTime.now

import br.com.unifaj.model.ProcessInput
import br.com.unifaj.processor.DataProcessor
import org.apache.spark.sql.SparkSession

object ApplicationStarter extends App {

  override def main(args: Array[String]): Unit = {
    println(s"Starting data process at: ${now()}")

    val Session: SparkSession = getSparkSession();
    val InputFile: String = "gs://unifaj-lake/generated.csv";
    val OutputFile: String = "gs://unifaj-lake/output.csv";

    val Input = ProcessInput(InputFile, OutputFile, Session);

    DataProcessor.apply(Input);

    println(s"Finishing data process at: ${now()}")
  }

  def getSparkSession(): SparkSession = {
     SparkSession.builder()
       .master("master")
       .appName("DataProcessApp")
       .getOrCreate()
  }

}
