package com.eden.orchid.utilities

// Taken from https://medium.com/@kezhenxu94/how-to-build-your-own-cache-in-kotlin-1b0e86005591

interface Cache<T, U> {
    val size: Int

    operator fun set(key: T, value: U)

    operator fun get(key: T): U?

    fun containsKey(key: T): Boolean

    fun remove(key: T): U?

    fun clear()
}
fun <T, U> Cache<T, U>.computeIfAbsent(key: T, loader: () -> U): U? {
    return if(this.containsKey(key)) {
        this[key]
    }
    else {
        val value = loader()
        this[key] = value
        return value
    }
}

class PerpetualCache<T, U> : Cache<T, U> {
    private val cache = HashMap<T, U>()

    override val size: Int
        get() = cache.size

    override fun set(key: T, value: U) {
        this.cache[key] = value
    }

    override fun remove(key: T) = this.cache.remove(key)

    override fun get(key: T) = this.cache[key]

    override fun containsKey(key: T) = this.cache.containsKey(key)

    override fun clear() = this.cache.clear()
}

class LRUCache<T, U>
@JvmOverloads
constructor(private val delegate: Cache<T, U> = PerpetualCache(), private val maxSize: Int = DEFAULT_SIZE) : Cache<T, U> {

    private val keyMap = object : LinkedHashMap<T, Boolean>(maxSize, .75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<T, Boolean>): Boolean {
            val tooManyCachedItems = size > maxSize
            if (tooManyCachedItems) eldestKeyToRemove = eldest.key
            return tooManyCachedItems
        }
    }

    private var eldestKeyToRemove: T? = null

    override val size: Int
        get() = delegate.size

    override fun set(key: T, value: U) {
        delegate[key] = value
        cycleKeyMap(key)
    }

    override fun remove(key: T) = delegate.remove(key)

    override fun get(key: T): U? {
        keyMap[key]
        return delegate[key]
    }

    override fun containsKey(key: T) = delegate.containsKey(key)

    override fun clear() {
        keyMap.clear()
        delegate.clear()
    }

    private fun cycleKeyMap(key: T) {
        keyMap[key] = PRESENT
        eldestKeyToRemove?.let { delegate.remove(it) }
        eldestKeyToRemove = null
    }

    companion object {
        private const val DEFAULT_SIZE = 1500
        private const val PRESENT = true
    }
}
