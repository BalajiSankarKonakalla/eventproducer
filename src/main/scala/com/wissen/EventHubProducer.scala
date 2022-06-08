package com.wissen

import com.azure.messaging.eventhubs._
import java.util.Arrays
import java.util.List

import com.wissen.AmexMockData.getTransactionRecord

object EventHubProducer {

  private final val connectionString = "Endpoint=sb://<EVENT_HUB>.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=<SHARED_ACCESS_KEY>"
  private final val eventHubName = "<EVENT_HUB_NAME>"

  def publishEvents(): Unit = {
    val producer = new EventHubClientBuilder()
      .connectionString(connectionString, eventHubName)
      .buildProducerClient()

    val eventDataBatch = producer.createBatch()

    while(true){
      val eventJson = getTransactionRecord.toJson
      val events = new EventData(eventJson)
      println(eventJson)
      eventDataBatch.tryAdd(events)
      producer.send(eventDataBatch)
    }

    producer.close()

  }

  def main(args: Array[String]): Unit = {
    publishEvents()
  }

}
