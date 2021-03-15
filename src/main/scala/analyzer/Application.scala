package analyzer

import analyzer.processor.BlogDataProcessor
import analyzer.shared.Logging
import analyzer.source.BlogDataReader

object Application extends Logging {

  def main(args: Array[String]): Unit = {
    val fileName = args.head
    logger.info(s"Starting Analyzer for $fileName")

    BlogDataReader.source(fileName)
      .foreach(BlogDataProcessor.firstPass(_))
    logger.info("Finished firsPass")

    BlogDataReader.source(fileName)
      .foreach(BlogDataProcessor.secondPass(_))
    logger.info("Finished secondPass")

    BlogDataProcessor.printStats()
  }
}
