package com.eden.orchid.strikt

import org.jsoup.Jsoup
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/**
 * Two HTML trees are considered similar if:
 *
 * - they both contain the same tags in the same order
 * - the text content of all tags are the same
 * - the attributes on each tag are the same, but not necessarily in the same order
 *
 * This is a recursive check; each tag will check that all its sub-tags also are similar based on the same criteria
 */
fun String.hasHtmlSimilarTo(
    other: String,
    thisSelectorToCheck: String = "body",
    otherSelectorToCheck: String = thisSelectorToCheck
): Boolean {
    val doc1 = this.normalizeDoc().select(thisSelectorToCheck).flatMap { it.childNodes() }
    val doc2 = other.normalizeDoc().select(otherSelectorToCheck).flatMap { it.childNodes() }

    return doc1.hasHtmlSimilarTo(doc2)
}

// Prep trees for evaluation
//----------------------------------------------------------------------------------------------------------------------

private fun String.normalizeDoc(removeComments: Boolean = true): Document {
    return Jsoup.parse(this)
        .apply {
            outputSettings(Document.OutputSettings().apply {
                indentAmount(2)
                prettyPrint(true)
                outline(true)
            })

            if (removeComments) {
                filter(CommentFilter)
            }
        }
}


// Evaluate trees for similarity
//----------------------------------------------------------------------------------------------------------------------

private fun List<Node>.hasHtmlSimilarTo(other: List<Node>): Boolean {
    val actualThis = this.filter { it !is TextNode || !it.isBlank }
    val actualOther = other.filter { it !is TextNode || !it.isBlank }

    // both these sets are empty, so they are equal
    if (actualThis.isEmpty() && actualOther.isEmpty()) {
        return true
    }

    // sets have different sizes, it's easy to tell they are not equal
    if (actualThis.size != actualOther.size) {
        return false
    }

    actualThis.forEachIndexed { index, thisElement ->
        val otherElement = actualOther[index]

        if (thisElement is TextNode) {
            if (otherElement !is TextNode || thisElement.text().trimLines() != otherElement.text().trimLines()) {
                return@hasHtmlSimilarTo false
            }
        } else if (thisElement is Element) {
            if (otherElement !is Element || !thisElement.hasHtmlSimilarTo(otherElement)) {
                return@hasHtmlSimilarTo false
            }
        }
    }

    return true
}

private fun Element.hasHtmlSimilarTo(other: Element): Boolean {
    // check tag name
    val tagName = { it: Element -> it.tagName() }
    val thisTagName = tagName(this)
    val otherTagName = tagName(other)
    if (thisTagName != otherTagName) {
        return false
    }

    // check own text content
    val ownTextContent = { it: Element -> it.ownText().trimLines() }
    val thisOwnTextContent = ownTextContent(this)
    val otherOwnTextContent = ownTextContent(other)
    if (thisOwnTextContent != otherOwnTextContent) {
        return false
    }

    // check classes
    val ownClasses = { it: Element -> it.classNames().sorted().distinct() }
    val thisOwnClasses = ownClasses(this)
    val otherOwnClasses = ownClasses(other)
    if (thisOwnClasses != otherOwnClasses) {
        return false
    }

    // check attributes
    val attributes = { it: Element -> it.attributes() }
    val thisAttributes = attributes(this)
    val otherAttributes = attributes(other)
    if (!thisAttributes.hasAttributesSimilarTo(otherAttributes)) {
        return false
    }

    // check child notes
    if (!this.childNodes().hasHtmlSimilarTo(other.childNodes())) {
        return false
    }

    return true
}

private fun Attributes.hasAttributesSimilarTo(other: Attributes): Boolean {
    // sets have different sizes, it's easy to tell they are not equal
    if (this.size() != other.size()) {
        return false
    }

    // check all the props in `this` match those in `other`
    this.forEach { thisAttribute ->
        if (thisAttribute.key != "class") {
            val actualAttributeKey = thisAttribute.key.removePrefix("data-")

            val otherKeyExists = other.hasKey(actualAttributeKey) || other.hasKey("data-$actualAttributeKey")
            val otherAttribute = if (other.hasKey(actualAttributeKey)) other.get(thisAttribute.key) else other.get("data-$actualAttributeKey")
            if (!otherKeyExists || thisAttribute.value != otherAttribute) {
                return@hasAttributesSimilarTo false
            }
        }
    }

    return true
}
