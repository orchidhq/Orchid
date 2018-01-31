package com.eden.orchid.posts.model

import com.eden.orchid.posts.pages.PostPage

data class CategoryModel(
        val category: String?,
        val first: List<PostPage>
)
