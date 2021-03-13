package analyzer.shared

import com.typesafe.scalalogging.Logger

trait Logging {
  protected[this] val logger = Logger(this.getClass)
}
