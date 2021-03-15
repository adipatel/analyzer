package analyzer.processor

import analyzer.shared.{BlogPost, Logging}
import com.tdunning.math.stats.{FloatHistogram, TDigest}
import org.apache.commons.math3.stat.Frequency
import org.apache.commons.math3.stat.descriptive.moment.Mean

import scala.collection.mutable
import scala.util.control.NonFatal

object BlogDataProcessor extends Logging {
  private[this] final val postLikesDigest = TDigest.createDigest(100)
  private[this] final val postLikesMean = new Mean()
  private[this] final val authorIds = mutable.BitSet.empty
  // Mix and Max bounds for FloatHistogram were found by a prior sequential scan
  // Min was found to be 0 & Max was 87424888 for the authorId from the sample data.
  private[this] final val postsByAuthorHistogram = new FloatHistogram(1, 88000000)
  private[this] final val postByAuthor = new Frequency()
  private[this] final var postHavingRedInTitle = 0

  final def firstPass(blogPost: BlogPost): Unit = {
    try {
      postLikesDigest.add(blogPost.likeCount)
      postLikesMean.increment(blogPost.likeCount)
      authorIds.addOne(blogPost.authorID)
      postsByAuthorHistogram.add(blogPost.authorID + 1) // Adding 1 since minAuthorId is 0
      postByAuthor.addValue(blogPost.authorID)
      if (postHasRedInTitle(blogPost)) postHavingRedInTitle += 1
    } catch {
      case NonFatal(e) =>
        logger.error(s"Got error while doing firstPass for $blogPost", e)
    }
  }

  // In the firstPass we accumulated all the authorIds in the BitSet
  // During the secondPass we remove all the userIds which liked any post.
  // Hence after this pass `authorIds` will only contain the authorIds which didn't like any post in the sample.
  final def secondPass(blogPost: BlogPost): Unit = {
    try {
      blogPost.likedBy.foreach(u => authorIds.remove(u))
    } catch {
      case NonFatal(e) =>
        logger.error(s"Got error while doing secondPass for $blogPost", e)
    }
  }

  final def printStats(): Unit = {
    logger.info(s"Median number of likes per post : ${postLikesDigest.quantile(0.5d)}")
    logger.info(s"Mean number of likes per post : ${postLikesMean.getResult}")
    logger.info(s"Mean number of posts per author: $meanOfPostsByAuthor")
    logger.info(s"Mean(Accurate) number of posts per author: $meanOfPostsByAuthorAccurate")
    logger.info(s"Number of posts having 'red' in their title : $postHavingRedInTitle")
    logger.info(s"Number of authors who didn't like any posts: ${authorIds.size}")
  }

  // FIXME: doesn't seems like an accurate way of calculating the mean number of posts per author
  private final def meanOfPostsByAuthor(): Double = {
    val m = new Mean()
    for (v <- postsByAuthorHistogram.getCounts)
      if (v > 0) m.increment(v)
    m.getResult
  }

  private final def meanOfPostsByAuthorAccurate(): Double = {
    if (postByAuthor.getUniqueCount > 0)
      postByAuthor.getSumFreq / postByAuthor.getUniqueCount
    else 0
  }

  // Only finding a match if the language of the post is English/en
  private final def postHasRedInTitle(blogPost: BlogPost): Boolean = {
    if (blogPost.language.equalsIgnoreCase("en"))
      wordsInTitle(blogPost).exists(w => w.equalsIgnoreCase("red"))
    else false
  }

  private final def wordsInTitle(blogPost: BlogPost): Array[String] = blogPost.title.split("""\s+""")
}
