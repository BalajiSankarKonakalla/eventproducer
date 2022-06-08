package com.wissen

import java.util.{Properties, UUID}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import com.wissen.AmexConstants._
import com.wissen.AmexMockData._

import scala.util.Random

object AmexProducer {

  def main(args: Array[String]): Unit = {

    val props: Properties = new Properties()
    props.put("bootstrap.servers", kafkaBrokers)
    props.put("key.serializer", keySerilaizer)
    props.put("value.serializer", valueSerilaizer)
    props.put("acks", "all")
    val producer = new KafkaProducer[String, String](props)


    val topic = topicName


    try{
      while(true){
        val transactionRecord = new ProducerRecord[String, String](topic, "transaction", getTransactionRecord.toJson)
        val metadata = producer.send(transactionRecord)

        println("---------->>> ",
          "Recored key = " + transactionRecord.key(),
          "Record value = " + transactionRecord.value(),
          "partition = " + metadata.get.partition(),
          "offset = " + metadata.get.offset(),
          "timestamp = " + metadata.get.timestamp()
        )

      }
    }catch{
        case e: Exception => e.printStackTrace()
    }finally{
        producer.close()
    }
  }
}