package org.jakubf.training

import org.jakubf.training.configuration.LocalSparkConfiguration
import org.jakubf.training.processor.BookRatingProcessor

object Main extends App with LocalSparkConfiguration {
  override def main(args: Array[String]): Unit = {
    new BookRatingProcessor(hiveSparkSession)
      .startProcessing
  }
}