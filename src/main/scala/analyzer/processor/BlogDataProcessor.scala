package analyzer.processor

import analyzer.shared.{BlogPost, Logging}

object BlogDataProcessor extends Logging {

  def process(blogPost: BlogPost): Unit = {
    logger.info(s"Processing $blogPost")
  }

}
