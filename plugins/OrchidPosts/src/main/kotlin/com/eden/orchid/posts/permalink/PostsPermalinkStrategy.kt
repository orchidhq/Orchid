package com.eden.orchid.posts.permalink

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.posts.pages.PermalinkPage
import com.eden.orchid.utilities.OrchidUtils
import java.util.*
import javax.inject.Inject


@JvmSuppressWildcards
class PostsPermalinkStrategy @Inject
constructor(
        val context: OrchidContext,
        val postsModel: PostsModel,
        val injectedPathTypes: Set<PermalinkPathType>
) {

    private val pathTypes: Set<PermalinkPathType> = TreeSet(injectedPathTypes)

    fun applyPermalink(page: PermalinkPage) {
        applyPermalink(page, getPermalinkTemplate(page))
    }

    fun applyPermalink(page: OrchidPage, permalink: String) {
        val pieces = permalink.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val resultingUrl = applyPermalinkTemplate(page, pieces)
        val title = applyPermalinkTemplatePiece(page, pieces[pieces.size - 1])

        page.reference.path = OrchidUtils.normalizePath(resultingUrl)
        page.reference.fileName = title

        page.reference.isUsePrettyUrl = true
    }

    private fun getPermalinkTemplate(post: PermalinkPage): String {
        return if (!EdenUtils.isEmpty(post.permalink)) {
            post.permalink
        } else {
            postsModel.permalink
        }
    }

    private fun applyPermalinkTemplate(page: OrchidPage, pieces: Array<String>): String {
        var resultingUrl = ""

        for (i in 0 until pieces.size - 1) {
            resultingUrl += applyPermalinkTemplatePiece(page, pieces[i])
        }

        return resultingUrl
    }

    private fun applyPermalinkTemplatePiece(page: OrchidPage, piece: String): String {
        var resultingPiece: String? = null

        var pieceKey: String? = null

        if (!EdenUtils.isEmpty(piece) && piece.startsWith(":")) {
            pieceKey = piece.substring(1)
        } else if (!EdenUtils.isEmpty(piece) && piece.startsWith("{") && piece.endsWith("}")) {
            pieceKey = piece.substring(1, piece.length - 1)
        }

        if (pieceKey != null) {
            for (pathType in pathTypes) {
                if (pathType.acceptsKey(page, pieceKey)) {
                    resultingPiece = pathType.format(page, pieceKey)
                }
            }

            if (resultingPiece == null) {
                throw IllegalArgumentException("'$piece' is not a valid permalink key")
            }
        } else {
            resultingPiece = piece
        }

        return sanitizePathPiece(resultingPiece) + "/"
    }

    private fun sanitizePathPiece(pathPiece: String): String {
        var s = pathPiece.replace("\\s+".toRegex(), "-").toLowerCase()
        s = s.replace("[^\\w-_]".toRegex(), "")
        return s
    }

}

