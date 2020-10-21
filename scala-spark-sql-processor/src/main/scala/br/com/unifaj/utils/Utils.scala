package br.com.unifaj.utils

import java.security.MessageDigest

object Utils {

  def checkIfIsNumber(value: String): Boolean = {
    value forall Character.isDigit
  }

  def checkLength(value: String, length: Int): Boolean = {
    value.length.equals(length)
  }

  def md5(value: String): String = {
    MessageDigest.getInstance("MD5").digest(value.getBytes)
      .map(0xFF & _)
      .map {
        "%02x".format(_)
      }
      .foldLeft("") {
        _ + _
      }
  }

  def leftPad(str: String = "", paddedLength: Int = 0, character: Char = '0'): String = {
    val builder = StringBuilder.newBuilder
    val remLength = paddedLength - str.length;

    if (remLength <= 0) {
      return str;
    }

    for (_ <- 0 until remLength) {
      builder.append(character);
    }

    builder.append(str);

    builder.toString();
  }

}
