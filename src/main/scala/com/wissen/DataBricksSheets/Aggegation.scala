val tran = spark
  .readStream
  .format("delta").load("dbfs:/amex/transactions")

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.TimestampType

tran
  .withWatermark("timestamp", "10 minutes")
  .groupBy("acquiringBank")
  .agg(sum("transactionAmount").as("transactionAmount"))
  .writeStream
  .format("console")
  .outputMode("complete")
  .start()
  .awaitTermination()
