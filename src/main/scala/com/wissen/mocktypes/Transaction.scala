package com.wissen.mocktypes

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
      | "issuingBank": "$issuingBank",
      | "transactionAmount": $transactionAmount,
      | "location": "$location",
      | "acquiringBank": "$acquiringBank"
      |}""".stripMargin
  }
}
