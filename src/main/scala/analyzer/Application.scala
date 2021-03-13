package analyzer

import akka.actor
import akka.stream.{ActorAttributes, Supervision}
import analyzer.processor.BlogDataProcessor
import analyzer.shared.Logging
import analyzer.source.BlogDataReader

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

object Application extends Logging {

  def main(args: Array[String]): Unit = {
    implicit val ec = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())
    implicit val system: actor.ActorSystem = actor.ActorSystem("BlogDataStream")
    val logAndStoppingDecider: Supervision.Decider = {
      case NonFatal(e) =>
        logger.error("BlogData flow caught unknown exception. Will STOP Stream !!", e)
        Supervision.Stop
    }

    BlogDataReader.source("small.jsonl")
      .map { o =>
        BlogDataProcessor.process(o)
      }
      .withAttributes(ActorAttributes.supervisionStrategy(logAndStoppingDecider))
      .watchTermination() {
        (_, future) => {
          future.onComplete(_ => {
            logger.warn("BlogDataStream finished. Terminating Stream !!")
            system.terminate()
          })
        }
      }
      .run()
  }
}
