
  import org.apache.spark.sql.catalyst.ScalaReflection
  import org.apache.spark.sql.functions._
  import org.apache.spark.sql.types.StructType

  import org.json4s._
  import org.json4s.jackson.JsonMethods._


  case class Transaction(
                          id: String,
                          mcc: String,
                          cardNumber: Long,
                          customerName: String,
                          cardType: String,
                          issuingBank: String,
                          transactionAmount: Double,
                          location: String,
                          acquiringBank: String
                        ){
    def toJson: String = {
      s"""{
         | "id": "$id",
         | "mcc": "$mcc",
         | "cardNumber": $cardNumber,
         | "customerName": "$customerName",
         | "cardType": "$cardType",
         | "issuingBank", "$issuingBank",
         | "transactionAmount": $transactionAmount,
         | "location": "$location",
         | "acquiringBank": "$acquiringBank"
         |}""".stripMargin
    }
  }

  val schema = ScalaReflection.schemaFor[Transaction].dataType.asInstanceOf[StructType]

  dbutils.fs.mount(
    source = "wasbs://<BLOB_STORAGE>@amexpoc.blob.core.windows.net/amex",
    mountPoint = "/mnt/amex",
    extraConfigs = Map("fs.azure.sas.amex-transactions.amexpoc.blob.core.windows.net" -> "<KEY>"))

  import org.apache.spark.eventhubs._
  import com.microsoft.azure.eventhubs._

  import org.apache.spark.sql.types.StringType
  import org.apache.spark.sql.types.TimestampType

  val namespaceName = "amexevents"
  val eventHubName = "transactions"
  val sasKeyName = "RootManageSharedAccessKey"
  val sasKey = "ZaWyPhhmN+GSjHkbNSjSBnozQ6okiL1lkiK91WKKwl8="
  val connStr = new com.microsoft.azure.eventhubs.ConnectionStringBuilder()
    .setNamespaceName(namespaceName)
    .setEventHubName(eventHubName)
    .setSasKeyName(sasKeyName)
    .setSasKey(sasKey)

  val customEventhubParameters =
    EventHubsConf(connStr.toString()).setMaxEventsPerTrigger(500)

  val incomingStream =
    spark
      .readStream
      .format("eventhubs")
      .options(customEventhubParameters.toMap)
      .load()

  incomingStream.printSchema

  incomingStream
    .withColumn("B", $"body".cast(StringType))
    .select("B")
    .select(from_json(col("B"), schema).alias("tmp"))
    .select("tmp.*")
    .withColumn("timestamp", current_timestamp().cast(TimestampType))
    .writeStream
    .option("checkpointLocation", "dbfs:/amex/checkpoints/transactions")
    .outputMode("append")
    .format("delta")
    .option("truncate", false)
    .start("dbfs:/amex/transactions")
    .awaitTermination()

