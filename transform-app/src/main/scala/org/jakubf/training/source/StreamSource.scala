package org.jakubf.training.source

import org.apache.spark.sql.DataFrame

trait StreamSource {
  def read(): DataFrame
}