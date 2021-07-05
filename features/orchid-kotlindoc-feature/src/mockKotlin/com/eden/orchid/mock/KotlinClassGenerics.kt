package com.eden.orchid.mock

import java.util.Collections.emptyList

/**
 * This class _freaking awesome_ class links to [KotlinInterface], **yo**!
 */
class KotlinClassGenerics<T : Any>
/**
 * This class _freaking awesome_ constructor links to [KotlinInterface], **yo**!
 *
 * @param s1 This class _freaking awesome_ param links to [KotlinInterface], **yo**!
 */
constructor(s1: T?) {

    constructor(s2: String) : this(null)

    /**
     * This class _freaking awesome_ field links to [KotlinInterface], **yo**!
     */
    var someData = ""

    /**
     * This class _freaking awesome_ field links to [KotlinInterface], **yo**!
     */
    lateinit var someData2: String

    /**
     * This class _freaking awesome_ field links to [KotlinInterface], **yo**!
     */
    lateinit var someData3: T

    /**
     * This class _freaking awesome_ field links to [KotlinInterface], **yo**!
     */
    lateinit var someData4: List<String>

    /**
     * This class _freaking awesome_ field links to [KotlinInterface], **yo**!
     */
    lateinit var someData5: List<T>

    /**
     * This class _freaking awesome_ field links to [KotlinInterface], **yo**!
     */
    lateinit var someData5: List<KotlinClassGenerics<String>>

    /**
     * This class _freaking awesome_ method links to [KotlinInterface], **yo**!
     *
     * @param s1 This class _freaking awesome_ param links to [KotlinInterface], **yo**!
     * @return This class _freaking awesome_ return value links to [KotlinInterface], **yo**!
     */
    fun doThing(s1: String): String {
        return ""
    }

    /**
     * This class _freaking awesome_ method links to [KotlinInterface], **yo**!
     *
     * @param s1 This class _freaking awesome_ param links to [KotlinInterface], **yo**!
     * @return This class _freaking awesome_ return value links to [KotlinInterface], **yo**!
     */
    fun doThing2(s1: T): T? {
        return null
    }

    /**
     * This class _freaking awesome_ method links to [KotlinInterface], **yo**!
     *
     * @param s1 This class _freaking awesome_ param links to [KotlinInterface], **yo**!
     * @return This class _freaking awesome_ return value links to [KotlinInterface], **yo**!
     */
    fun doThing3(s1: List<String>): List<String> {
        return emptyList()
    }

    /**
     * This class _freaking awesome_ method links to [KotlinInterface], **yo**!
     *
     * @param s1 This class _freaking awesome_ param links to [KotlinInterface], **yo**!
     * @return This class _freaking awesome_ return value links to [KotlinInterface], **yo**!
     */
    fun doThing4(s1: List<T>): List<T> {
        return emptyList()
    }
}
