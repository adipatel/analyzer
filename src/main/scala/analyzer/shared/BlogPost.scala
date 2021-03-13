package analyzer.shared

import analyzer.shared.BlogPost.UserID


case class BlogPost(blogPostID: String,
                    authorID: UserID,
                    likeCount: Int,
                    likedBy: List[UserID],
                    title: String,
                    language: String)

object BlogPost {
  type UserID = Int
}