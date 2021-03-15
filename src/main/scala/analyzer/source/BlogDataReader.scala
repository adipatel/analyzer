package analyzer.source

import analyzer.shared.{BlogPost, Logging}

object BlogDataReader extends Logging {

  def source(fileName: String): Iterator[BlogPost] = readPosts(fileName)

  private[this] def readPosts(fileName: String): Iterator[BlogPost] = {
    io.Source.fromFile(fileName, "UTF-8")
      .getLines()
      .map { l =>
        val blogDataObj = ujson.read(l).obj
        val blogPostId = s"${blogDataObj.get("blog_id").get.num.toInt}_${blogDataObj.get("post_id").get.num.toInt}"
        val authorId = blogDataObj.get("author_id").get.num.toInt
        val title = blogDataObj.get("title").get.str
        val lang = blogDataObj.get("lang").get.str
        val likeCount = blogDataObj.get("like_count").get.num.toInt
        val likes = if (likeCount > 0) blogDataObj.get("liker_ids").get.arr.toList.map(_.num.toInt) else List.empty[Int]

        BlogPost(blogPostId, authorId, likeCount, likes, title, lang)
      }
  }
}

