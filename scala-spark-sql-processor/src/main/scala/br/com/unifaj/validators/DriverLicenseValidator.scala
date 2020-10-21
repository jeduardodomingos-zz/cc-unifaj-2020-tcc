package br.com.unifaj.validators

import scala.annotation.tailrec

object DriverLicenseValidator {

  def apply(value: String): Boolean = {

    if (checkIfIsNumber(value) && checkIfIsNumber(value)) {
      return validate(value)
    }

    false
  }

  def validate(value: String): Boolean = {

    val document = Utils.leftPad(value, 11)
    val firstDigit = document.substring(9, 10).toLong
    val secondDigit = document.substring(10, 11).toLong

    def getFirstDigitValidate(calculated: Long, native: Long): Boolean = {
      calculated.equals(native)
    }

    @tailrec
    def getFirstSum(currentValue: String, sum: Long, currentIndex: Int, maxIndex: Int): Long = {
      var currentSUm = sum

      if (currentIndex > 0) {
        val currentDigit = currentValue.substring((maxIndex - currentIndex), ((maxIndex - currentIndex) + 1)).toInt
        currentSUm = (currentDigit * currentIndex) + sum
        getFirstSum(currentValue, currentSUm, currentIndex - 1, maxIndex)
      } else {
        currentSUm
      }
    }

    @tailrec
    def getSecondSum(currentValue: String, sum: Long, currentIndex: Int, minIndex: Int): Long = {
      var currentSUm = sum

      if (currentIndex <= 9) {
        val currentDigit = currentValue.substring((currentIndex - minIndex), ((currentIndex - minIndex) + 1)).toInt
        currentSUm = (currentDigit * currentIndex) + sum
        getSecondSum(currentValue, currentSUm, currentIndex + 1, minIndex)
      } else {
        currentSUm
      }
    }

    val firstSum = getFirstSum(document, sum = 0L, currentIndex = 9, maxIndex = 9)
    val firstModule = firstSum % 11
    val firstDigitResult = if (firstModule > 9) 0 else firstModule

    val secondSum = getSecondSum(document, sum = 0L, currentIndex = 1, minIndex = 1)
    var secondModule = secondSum % 11

    if (firstModule > 9) {
      if ((secondModule - 2) < 0) {
        secondModule += 9
      } else {
        secondModule -= 2
      }
    }

    val secondDigitResult = if (secondModule > 9) 0 else secondModule

    getFirstDigitValidate(firstDigitResult, firstDigit) && getFirstDigitValidate(secondDigitResult, secondDigit)
  }

}
