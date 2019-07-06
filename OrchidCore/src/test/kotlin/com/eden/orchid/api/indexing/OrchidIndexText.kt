package com.eden.orchid.api.indexing

import com.eden.orchid.testhelpers.BaseOrchidTest

class OrchidIndexText : BaseOrchidTest() {

//    private lateinit var context: OrchidContext
//    private lateinit var extractor: OptionsExtractor
//
//    private var wikiKey = "wiki"
//    private lateinit var wikiPages: List<OrchidPage>
//    private lateinit var wikiIndex: OrchidIndex
//    private lateinit var wiki_summary: OrchidPage
//    private lateinit var wiki_userManual_summary: OrchidPage
//    private lateinit var wiki_userManual_inner_page1: OrchidPage
//    private lateinit var wiki_userManual_inner_page2: OrchidPage
//    private lateinit var wiki_userManual_inner_page3: OrchidPage
//    private lateinit var wiki_userManual_inner_deep_page1: OrchidPage
//    private lateinit var wiki_userManual_inner_deep_page2: OrchidPage
//    private lateinit var wiki_developersGuide_summary: OrchidPage
//    private lateinit var wiki_developersGuide_inner_page1: OrchidPage
//    private lateinit var wiki_developersGuide_inner_page2: OrchidPage
//    private lateinit var wiki_developersGuide_inner_page3: OrchidPage
//    private lateinit var wiki_developersGuide_inner_deep_page1: OrchidPage
//    private lateinit var wiki_developersGuide_inner_deep_page2: OrchidPage
//
//    private var pagesKey = "pages"
//    private lateinit var pagesPages: List<OrchidPage>
//    private lateinit var pagesIndex: OrchidIndex
//    private lateinit var pages_page1: OrchidPage
//    private lateinit var pages_page12: OrchidPage
//    private lateinit var pages_page2: OrchidPage
//    private lateinit var pages_page22: OrchidPage
//    private lateinit var pages_page3: OrchidPage
//    private lateinit var pages_page32: OrchidPage
//
//    private lateinit var rootIndex: OrchidRootIndex
//
//    @BeforeEach
//    override fun setUp() {
//        super.setUp()
//        context = mock(OrchidContext::class.java)
//        extractor = mock(OptionsExtractor::class.java)
//        `when`(context.getEmbeddedData(anyString(), anyString())).thenReturn(EdenPair("", emptyMap()))
//        `when`(context.resolve(OptionsExtractor::class.java)).thenReturn(extractor)
//        rootIndex = OrchidRootIndex("test")
//
//        // emulated Wiki generator
//        wiki_summary = OrchidPage(StringResource(context, "wiki/index.md", ""), "", "")
//        wiki_userManual_summary = OrchidPage(StringResource(context, "wiki/user-manual/summary.md", ""), "", "")
//        wiki_userManual_inner_page1 = OrchidPage(StringResource(context, "wiki/user-manual/inner/overview1.md", ""), "", "")
//        wiki_userManual_inner_page2 = OrchidPage(StringResource(context, "wiki/user-manual/inner/overview2.md", ""), "", "")
//        wiki_userManual_inner_page3 = OrchidPage(StringResource(context, "wiki/user-manual/inner/overview3.md", ""), "", "")
//        wiki_userManual_inner_deep_page1 = OrchidPage(StringResource(context, "wiki/user-manual/inner/deep/overview1.md", ""), "", "")
//        wiki_userManual_inner_deep_page2 = OrchidPage(StringResource(context, "wiki/user-manual/inner/deep/overview2.md", ""), "", "")
//        wiki_developersGuide_summary = OrchidPage(StringResource(context, "wiki/developers-guide/summary.md", ""), "", "")
//        wiki_developersGuide_inner_page1 = OrchidPage(StringResource(context, "wiki/developers-guide/inner/overview1.md", ""), "", "")
//        wiki_developersGuide_inner_page2 = OrchidPage(StringResource(context, "wiki/developers-guide/inner/overview2.md", ""), "", "")
//        wiki_developersGuide_inner_page3 = OrchidPage(StringResource(context, "wiki/developers-guide/inner/overview3.md", ""), "", "")
//        wiki_developersGuide_inner_deep_page1 = OrchidPage(StringResource(context, "wiki/developers-guide/inner/deep/overview1.md", ""), "", "")
//        wiki_developersGuide_inner_deep_page2 = OrchidPage(StringResource(context, "wiki/developers-guide/inner/deep/overview2.md", ""), "", "")
//        wikiPages = listOf(
//                wiki_summary,
//                wiki_userManual_summary,
//                wiki_userManual_inner_page1,
//                wiki_userManual_inner_page2,
//                wiki_userManual_inner_page3,
//                wiki_userManual_inner_deep_page1,
//                wiki_userManual_inner_deep_page2,
//                wiki_developersGuide_summary,
//                wiki_developersGuide_inner_page1,
//                wiki_developersGuide_inner_page2,
//                wiki_developersGuide_inner_page3,
//                wiki_developersGuide_inner_deep_page1,
//                wiki_developersGuide_inner_deep_page2
//        )
//        wikiIndex = OrchidIndex(null, wikiKey)
//        wikiPages.forEach { page -> wikiIndex.addToIndex("$wikiKey/${page.reference.path}", page) }
//        rootIndex.addChildIndex(wikiKey, wikiIndex)
//
//        // emulated Pages generator
//        pages_page1 = OrchidPage(StringResource(context, "page1.md", ""), "", "")
//        pages_page12 = OrchidPage(StringResource(context, "page1/page2.md", ""), "", "")
//        pages_page2 = OrchidPage(StringResource(context, "page2.md", ""), "", "")
//        pages_page22 = OrchidPage(StringResource(context, "page2/page2.md", ""), "", "")
//        pages_page3 = OrchidPage(StringResource(context, "page3.md", ""), "", "")
//        pages_page32 = OrchidPage(StringResource(context, "page3/page3.md", ""), "", "")
//        pagesPages = listOf(
//                pages_page1,
//                pages_page12,
//                pages_page2,
//                pages_page22,
//                pages_page3,
//                pages_page32
//        )
//        pagesIndex = OrchidIndex(null, pagesKey)
//        pagesPages.forEach { page -> pagesIndex.addToIndex("$pagesKey/${page.reference.path}", page) }
//        rootIndex.addChildIndex(pagesKey, pagesIndex)
//    }
//
//    @TestFactory
//    fun testIndexBuiltCorrectly(): List<DynamicTest> {
//        return listOf(
//                DynamicTest.dynamicTest("wikiIndex has all of wikiPages") {
//                    expect {
//                        that(wikiIndex)
//                                .get(OrchidIndex::allPages)
//                                .containsExactlyInAnyOrder(wikiPages)
//                    }
//                },
//                DynamicTest.dynamicTest("pagesIndex has all of pagesPages") {
//                    expect {
//                        that(pagesIndex)
//                                .get(OrchidIndex::allPages)
//                                .containsExactlyInAnyOrder(pagesPages)
//                    }
//                },
//                DynamicTest.dynamicTest("rootIndex has all of wikiIndex and pagesPages") {
//                    expect {
//                        that(rootIndex)
//                                .get(OrchidIndex::allPages)
//                                .containsExactlyInAnyOrder(wikiPages + pagesPages)
//                    }
//                }
//        )
//    }
//
//    @TestFactory
//    fun testFindPage(): List<DynamicTest> {
//        return (wikiPages + pagesPages).map { page ->
//            DynamicTest.dynamicTest("when looking up 'findPage ${page.reference.path}' from its path, the index returns the same instance") {
//                expect {
//                    that(rootIndex)
//                            .get { findPage(page.reference.path) }
//                            .isSameInstanceAs(page)
//                }
//            }
//        }
//    }
//
//    @TestFactory
//    fun testFind(): List<DynamicTest> {
//        return (wikiPages + pagesPages).map { page ->
//            DynamicTest.dynamicTest("when looking up 'find ${page.reference.path}' from its path, the index returns a list containing the same instance") {
//                expect {
//                    that(rootIndex)
//                            .get { find(page.reference.path) }
//                            .contains(page)
//                }
//            }
//        }
//    }
//
//    @TestFactory
//    fun testFindIndex(): List<DynamicTest> {
//        return (wikiPages + pagesPages).map { page ->
//            DynamicTest.dynamicTest("when looking up 'findIndex ${page.reference.path}' from its path, the index returns an index containing the same instance as its own page") {
//                expect {
//                    that(rootIndex)
//                            .get { findIndex(page.reference.path) }
//                            .isNotNull()
//                            .get(OrchidIndex::getOwnPages)
//                            .contains(page)
//                }
//            }
//        }
//    }
//
//    @TestFactory
//    fun testFindSubtrees(): List<DynamicTest> {
//        return listOf(
//                DynamicTest.dynamicTest("the subtree of 'wiki/user-manual' has 5 pages total") {
//                    expect {
//                        that(rootIndex)
//                                .get { findIndex("wiki/user-manual") }
//                                .isNotNull()
//                                .get(OrchidIndex::allPages)
//                                .containsExactlyInAnyOrder(
//                                        wiki_userManual_summary,
//                                        wiki_userManual_inner_page1,
//                                        wiki_userManual_inner_page2,
//                                        wiki_userManual_inner_page3,
//                                        wiki_userManual_inner_deep_page1,
//                                        wiki_userManual_inner_deep_page2
//                                )
//                    }
//                },
//                DynamicTest.dynamicTest("the subtree of 'wiki/developers-guide' has 5 pages total") {
//                    expect {
//                        that(rootIndex)
//                                .get { findIndex("wiki/developers-guide") }
//                                .isNotNull()
//                                .get(OrchidIndex::allPages)
//                                .containsExactlyInAnyOrder(
//                                        wiki_developersGuide_summary,
//                                        wiki_developersGuide_inner_page1,
//                                        wiki_developersGuide_inner_page2,
//                                        wiki_developersGuide_inner_page3,
//                                        wiki_developersGuide_inner_deep_page1,
//                                        wiki_developersGuide_inner_deep_page2
//                                )
//                    }
//                }
//        )
//    }
//
//    @TestFactory
//    fun testFindSubtreeParent(): List<DynamicTest> {
//        return listOf(
//                DynamicTest.dynamicTest("the subtree of 'wiki/user-manual' has a parent of `wiki`") {
//                    expect {
//                        that(rootIndex)
//                                .get { findIndex("wiki/user-manual") }
//                                .isNotNull()
//                                .and {
//                                    var indexParent: OrchidIndex? = null
//                                    get { indexParent = this; this.parent }
//                                            .isNotNull()
//                                            .and {
//                                                get { ownKey }.isEqualTo("wiki")
//                                                get { children }["user-manual"]
//                                                        .isNotNull()
//                                                        .isSameInstanceAs(indexParent)
//                                            }
//                                }
//
//                    }
//                },
//                DynamicTest.dynamicTest("the subtree of 'wiki/developers-guide' has a parent of `wiki`") {
//                    expect {
//                        that(rootIndex)
//                                .get { findIndex("wiki/developers-guide") }
//                                .isNotNull()
//                                .and {
//                                    var indexParent: OrchidIndex? = null
//                                    get { indexParent = this; this.parent }
//                                            .isNotNull()
//                                            .and {
//                                                get { ownKey }.isEqualTo("wiki")
//                                                get { children }["developers-guide"]
//                                                        .isNotNull()
//                                                        .isSameInstanceAs(indexParent)
//                                            }
//                                }
//
//                    }
//                }
//        )
//    }

}