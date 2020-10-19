package br.com.unifaj.validators

import br.com.unifaj.utils.Utils

import scala.annotation.tailrec

object DocumentValidator {


  def apply(value: String): Boolean = {

    if(Utils.checkIfIsNumber(value) && Utils.checkLength(value, 11)) {
      return validateDocument(value)
    }

    false
  }

  def validateDocument(value: String): Boolean = {
    val INITIAL_SUM = 0;

    def getDigit(value: String): String = {

      @tailrec
      def makeSum(value: String, sum: Long, currentIndex: Int): Long = {

        if(currentIndex == 0) {
          sum
        } else {
          val treatIndex = value.length - (currentIndex - 1)
          val sumValue = sum + value.substring(treatIndex - 1, treatIndex).toLong * (currentIndex + 1)

          makeSum(value, sumValue, currentIndex-1);
        }

      }

      val sum = makeSum(value, INITIAL_SUM, value.length)
      val module = sum%11
      val digit = if ((11 - module) >= 10) 0 else (11 - module)

      digit.toString
    }

    var document = Utils.leftPad(value, paddedLength = 11)

    document = document.substring(0, 9)
    document = s"$document${getDigit(document)}"
    document = s"$document${getDigit(document)}"

    document.equals(value)
  }


}
