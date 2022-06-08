package com.wissen

object AmexConstants {

  val kafkaBrokers2 = "kafka2.datacanny.com:9092"
  val kafkaBrokers3 = "kafka3.datacanny.com:9092"
  val kafkaBrokers = "kafka.datacanny.com:9092,kafka2.datacanny.com:9092,kafka3.datacanny.com:9092"
  val keySerilaizer = "org.apache.kafka.common.serialization.StringSerializer"
  val valueSerilaizer = "org.apache.kafka.common.serialization.StringSerializer"
  val topicName = "amex"

  val transactionsFilePath = "/Users/balaji/training/eventproducer/src/main/scala/resources/transaction.csv"
  val fullNameFilePath = "/Users/balaji/training/eventproducer/src/main/scala/resources/full_name.csv"
  val citiesFilePath = "/Users/balaji/training/eventproducer/src/main/scala/resources/cities.csv"

}
