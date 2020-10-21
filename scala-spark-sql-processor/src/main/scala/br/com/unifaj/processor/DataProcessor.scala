package br.com.unifaj.processor

import br.com.unifaj.model.ProcessInput
import br.com.unifaj.utils.Utils
import br.com.unifaj.validators.{DocumentValidator, DriverLicenseValidator}
import org.apache.spark.sql.functions.lit

object DataProcessor {

  def applyWithoutFilter(processInput: ProcessInput) = {
    envBuild(processInput)
    setup(processInput)
    process(processInput)
  }

  def applyWithFilter(processInput: ProcessInput) = {
    envBuild(processInput)
    setup(processInput)
    processSinglePartition(processInput)
  }

  def process(processInput: ProcessInput): Unit = {
    val Spark = processInput.SparkSession

    val data = Spark.sql(s"SELECT * FROM ${processInput.TableName} WHERE 1 == 1")

    Spark.udf.register("hash", hash)

    data.columns.foreach(cols => {
      data.withColumn(cols, lit(hash(cols)))
    })

    data.write.parquet(processInput.OutputFile)
  }

  def processSinglePartition(processInput: ProcessInput): Unit = {
    val Spark = processInput.SparkSession

    val data = Spark.sql(s"SELECT * FROM ${processInput.TableName} WHERE index = 1")

    Spark.udf.register("hash", hash)

    data.columns.foreach(cols => {
      data.withColumn(cols, lit(hash(cols)))
    })

    data.write.parquet(processInput.OutputFile)
  }

  val hash = (value: String) => {
    if (DriverLicenseValidator(value) && DocumentValidator(value)) {
      Utils.md5(value)
    }
  }

  def envBuild(processInput: ProcessInput): Unit = {
    val Spark = processInput.SparkSession

    Spark.sql("SET hive.input.dir.recursive=true")
    Spark.sql("SET hive.mapred.supports.subdirectories=true")
    Spark.sql("SET hive.supports.subdirectories=true")
    Spark.sql("SET mapred.input.dir.recursive=true")
    Spark.sql("SET hive.exec.dynamic.partition.mode=nonstrict")
    Spark.sql("SET hive.enforce.bucketing=true")
    Spark.sql("SET hive.exec.dynamic.partition=true")
    Spark.sql("SET hive.exec.max.dynamic.partitions.pernode=1000")
  }

  def setup(processInput: ProcessInput): Unit = {

    val partition: Array[String] = Array("PART01", "PART02", "PART04", "PART05")
    val fields: Array[String] = Array("id STRING", "cpf STRING", "address STRING", "city STRING", "state STRING", "parent_name STRING", "parent_document STRING", "parent_city STRING", "parent_state STRING")
    val Spark = processInput.SparkSession

    //WE'VE CREATED AN EXTERNAL TABLE, BECAUSE WE DON'T WANT CHANGE RAW DATA
    val createTableCmd = s"CREATE EXTERNAL TABLE ${processInput.TableName} (${fields.mkString(",")}) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' PARTITIONED BY (index INT) STORED AS TEXTFILE LOCATION gs://${processInput.InputDir}"
    Spark.sql(createTableCmd)

    var index = 0

    partition.foreach({part =>
      val AlterTableCmd = s"ALTER TABLE ${processInput.TableName} ADD PARTITION (index=$index) LOCATION 'gs://${processInput.InputDir}/$part'"
      Spark.sql(AlterTableCmd)
      index += 1
    })
  }


}
