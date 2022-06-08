package com.wissen

import java.util.UUID

import com.wissen.mocktypes._

import scala.io.{BufferedSource, Source}
import scala.util.Random

object AmexMockData {

  val namesFile: BufferedSource = Source.fromFile(AmexConstants.fullNameFilePath)

  val nameAndMail: List[String] = (for(name <- namesFile.getLines()) yield name).toList

  val cardTypes = List("Corporate", "Personal", "Commercial")

  val mccTypes = List("Food", "Restaurant", "Travel", "Transport", "Gas", "Misc", "Other")

  val bankNames = List("BOA", "Chase", "Wellsfargo", "Wachovia", "SVB", "Citi", "ICICI", "DBS", "DB", "RBS", "ANZ", "Kiwi Bank", "CIMB", "RBC")

  val cityNames: List[String] = (for (city <- Source.fromFile(AmexConstants.fullNameFilePath).getLines()) yield city).toList

  def getTransactionRecord: Transaction = {

    val amount = math.abs(Random.nextDouble())

      Transaction(
        id = UUID.randomUUID().toString,
        mcc = mccTypes(Random.nextInt(7)),
        cardNumber = math.abs(Random.nextLong()),
        cardType = cardTypes(Random.nextInt(3)),
        customerName = nameAndMail(Random.nextInt(1000)),
        issuingBank = bankNames(Random.nextInt(bankNames.size)),
        transactionAmount = (math.floor(amount*10000) /100.toDouble) * 5,
        location = cityNames(Random.nextInt(1000)),
        acquiringBank = bankNames(Random.nextInt(bankNames.size))
      )
  }

}
