package com.eden.orchid.posts.utils

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.PostsGenerator
import com.eden.orchid.posts.model.PostsModel
import java.util.stream.Stream

class LatestPostsCollection(
    generator: PostsGenerator,
    private val postsModel: PostsModel
) : OrchidCollection<OrchidPage>(generator, "latestPosts", emptyList()) {

    override fun find(id: String?): Stream<OrchidPage> {
        if(id != null) {
            val parsed = PostsUtils.parseLatestPostCollectionId(id)

            if(parsed != null) {
                return (postsModel.getRecentPosts(parsed.first, parsed.second) as List<OrchidPage>).stream()
            }
        }

        return emptyList<OrchidPage>().stream()
    }

}