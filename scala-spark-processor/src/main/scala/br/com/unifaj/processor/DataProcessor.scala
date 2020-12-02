package br.com.unifaj.processor

import br.com.unifaj.model.ProcessInput
import br.com.unifaj.utils.Utils
import br.com.unifaj.validators.{DocumentValidator, DriverLicenseValidator}
import org.apache.spark.sql.functions.{col, lit}

object DataProcessor {

  def apply(processInput: ProcessInput) = {
    process(processInput)
  }

  def process(processInput: ProcessInput): Unit = {
    val Spark = processInput.SparkSession

    val data = Spark.read.csv(processInput.InputFile)

    Spark.udf.register("hash", hash)

    data.columns.foreach(cols => {
        data.withColumn(cols, lit(hash(cols)))
    })

    data.write.parquet(processInput.OutputFile)
  }

  val hash = (value: String) => {
    if(DriverLicenseValidator(value) || DocumentValidator(value)) {
      Utils.md5(value)
    }
  }

}
