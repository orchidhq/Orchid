package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.HardcodedResourceSourceTest.Companion.createStringResource
import com.eden.orchid.strikt.asExpected
import com.eden.orchid.testhelpers.OrchidUnitTest
import com.eden.orchid.utilities.LRUCache
import com.eden.orchid.utilities.readToString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class CachingResourceSourceTest : OrchidUnitTest {

    private var resource1Counter: Int = 0
    private var resource2Counter: Int = 0
    private var resource3Counter: Int = 0

    val testDelegate: OrchidResourceSource by lazy {
        HardcodedResourceSource(
            listOf<(OrchidContext) -> OrchidResource>(
                { resource1Counter++; createStringResource(it, "res1.md") },
                { resource2Counter++; createStringResource(it, "res2.md") },
                { resource3Counter++; createStringResource(it, "res3.md") }
            ),
            0,
            LocalResourceSource
        )
    }
    val context: OrchidContext by lazy {
        mock(OrchidContext::class.java)
    }

    @Test
    fun testResourceCounterWithoutCacheing() {
        val underTest = testDelegate

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(1)
        expectThat(resource2Counter).isEqualTo(0)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(2)
        expectThat(resource2Counter).isEqualTo(0)
        expectThat(resource3Counter).isEqualTo(0)
    }

    @Test
    fun testResourceCounterWithCacheing() {
        val lruCache = LRUCache<CachingResourceSourceCacheKey, OrchidResource?>()
        val underTest = testDelegate.cached(lruCache, null, emptyList())

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(1)
        expectThat(resource2Counter).isEqualTo(0)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(1)
        expectThat(resource2Counter).isEqualTo(0)
        expectThat(resource3Counter).isEqualTo(0)
    }

    @Test
    fun testResourceCounterWithCacheMultipleResourceLookups() {
        val lruCache = LRUCache<CachingResourceSourceCacheKey, OrchidResource?>()
        val underTest = testDelegate.cached(lruCache, null, emptyList())

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(1)
        expectThat(resource2Counter).isEqualTo(0)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(1)
        expectThat(resource2Counter).isEqualTo(0)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res2.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res2.md")

        expectThat(resource1Counter).isEqualTo(2)
        expectThat(resource2Counter).isEqualTo(1)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(2)
        expectThat(resource2Counter).isEqualTo(1)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res3.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res3.md")

        expectThat(resource1Counter).isEqualTo(3)
        expectThat(resource2Counter).isEqualTo(2)
        expectThat(resource3Counter).isEqualTo(1)

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(3)
        expectThat(resource2Counter).isEqualTo(2)
        expectThat(resource3Counter).isEqualTo(1)

        underTest
            .getResourceEntry(context, "res2.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res2.md")

        expectThat(resource1Counter).isEqualTo(3)
        expectThat(resource2Counter).isEqualTo(2)
        expectThat(resource3Counter).isEqualTo(1)
    }

    @Test
    fun testResourceCounterWithCacheMultipleResourceLookupsWithEvictions() {
        val lruCache = LRUCache<CachingResourceSourceCacheKey, OrchidResource?>(maxSize = 1)
        val underTest = testDelegate.cached(lruCache, null, emptyList())

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(1)
        expectThat(resource2Counter).isEqualTo(0)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(1)
        expectThat(resource2Counter).isEqualTo(0)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res2.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res2.md")

        expectThat(resource1Counter).isEqualTo(2)
        expectThat(resource2Counter).isEqualTo(1)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(3)
        expectThat(resource2Counter).isEqualTo(1)
        expectThat(resource3Counter).isEqualTo(0)

        underTest
            .getResourceEntry(context, "res3.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res3.md")

        expectThat(resource1Counter).isEqualTo(4)
        expectThat(resource2Counter).isEqualTo(2)
        expectThat(resource3Counter).isEqualTo(1)

        underTest
            .getResourceEntry(context, "res1.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res1.md")

        expectThat(resource1Counter).isEqualTo(5)
        expectThat(resource2Counter).isEqualTo(2)
        expectThat(resource3Counter).isEqualTo(1)

        underTest
            .getResourceEntry(context, "res2.md")
            .asExpected()
            .isNotNull()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo("res2.md")

        expectThat(resource1Counter).isEqualTo(6)
        expectThat(resource2Counter).isEqualTo(3)
        expectThat(resource3Counter).isEqualTo(1)
    }
}
