package com.eden.orchid.kss.parser

import org.apache.commons.lang3.StringUtils
import java.util.regex.Pattern

class CommentParser {
    companion object {

        fun parse(input: String): List<String> {
            val blocks = ArrayList<String>()
            var currentBlock: String? = null
            var insideSingleLineBlock = false
            var insideMultiLineBlock = false

            for (l in StringUtils.split(input, "\r\n")) {
                val line = (l ?: "").trim()

                if (isSingleLineComment(line)) {
                    val parsed = parseSingleLine(line)
                    if (insideSingleLineBlock) {
                        currentBlock += "\n" + parsed
                    } else {
                        currentBlock = parsed
                        insideSingleLineBlock = true
                    }
                }

                if (isStartOfMultiLineComment(line) || insideMultiLineBlock) {
                    val parsed = parseMultiLine(line)
                    if (insideMultiLineBlock) {
                        currentBlock += "\n" + parsed
                    } else {
                        currentBlock = parsed
                        insideMultiLineBlock = true
                    }
                }

                if (isEndOfMultiLineComment(line)) {
                    insideMultiLineBlock = false
                }

                if (!isSingleLineComment(line) && !insideMultiLineBlock) {
                    if (currentBlock != null) {
                        val normalizedBlock = normalize(currentBlock)
                        if ("" != normalizedBlock) {
                            blocks.add(normalizedBlock)
                        }
                    }

                    insideSingleLineBlock = false
                    currentBlock = null
                }
            }

            return blocks
        }

        private fun normalize(currentBlockArg: String): String {
            var currentBlock = currentBlockArg

            currentBlock = currentBlock.replace("(?m)^\\s*\\*".toRegex(), "")

            var indentSize: Int? = null
            val unindented = StringBuffer()
            val precedingWhitespacePattern = Pattern.compile("^\\s*")
            for (piece in currentBlock.split("\n")) {
                val m = precedingWhitespacePattern.matcher(piece)
                var match = ""
                if (m.find()) {
                    match = m.group()
                }
                var precedingWhitespace = match.length
                if (indentSize == null) {
                    indentSize = precedingWhitespace
                }

                if (piece != "") {
                    if (indentSize <= precedingWhitespace && indentSize > 0) {
                        unindented.append(piece.substring(indentSize))
                    } else {
                        unindented.append(piece)
                    }
                }
                unindented.append("\n")
            }
            return unindented.toString().trim()
        }

        fun isSingleLineComment(line: String): Boolean {
            return line.matches("^\\s*//.*".toRegex())
        }

        fun isStartOfMultiLineComment(line: String): Boolean {
            return line.matches("^\\s*/\\*.*".toRegex())
        }

        fun isEndOfMultiLineComment(line: String): Boolean {
            return line.matches(".*\\*\\/".toRegex())
        }

        fun parseSingleLine(line: String): String {
            return rstrip(line.replaceFirst("\\s*//".toRegex(), ""))
        }

        fun parseMultiLine(line: String): String {
            return rstrip(line.replaceFirst("\\s*/\\*".toRegex(), "").replaceFirst("\\*/".toRegex(), ""))
        }

        private fun rstrip(input: String): String {
            return input.replace("\\s*$".toRegex(), "")
        }
    }
}
