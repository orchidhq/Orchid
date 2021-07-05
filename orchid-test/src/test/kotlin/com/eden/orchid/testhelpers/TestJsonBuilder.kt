package com.eden.orchid.testhelpers

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.get
import strikt.assertions.getValue
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class TestJsonBuilder : OrchidUnitTest {

    @Test
    fun testJsonBuilder() {
        enableLogging()

        val result = obj {
            "a" to "b"
            "b" to 1
            "c" to 2.2
            "d" to true
            "e" to false
            "f" to obj {
                "aa" to "b"
                "bb" to 1
                "cc" to 2.2
                "dd" to true
                "ee" to false
                "ff" to obj {
                    "aaa" to "b"
                    "bbb" to 1
                    "ccc" to 2.2
                    "ddd" to true
                    "eee" to false
                    "fff" to obj {

                        for (i in 1..20) {
                            "val$i" to i * i
                        }
                    }
                }
            }

            "g" to arr {
                add("A")
                add("B")
                add("C")
                add(1)
                add(2)
                add(3)
                add(4.4)
                add(5.5)
                add(6.6)
                add(true)
                add(false)
                this add obj {
                    "AA" to "B"
                    "BB" to obj {
                        "AAA" to "B"
                        "BBB" to obj {
                            for (i in 1..2) {
                                "VAL$i" to i * i * i
                            }
                        }
                    }
                }

                this add arr {
                    add("A")
                    add("B")
                }
                this add arr {
                    add("A")
                    add("B")
                }
                this add arr {
                    add("A")
                    add("B")
                }
                this add arr {
                    add("A")
                    add("B")
                }
                this add arr {
                    add("A")
                    add("B")
                }
            }
        }

        expectThat(result)
            .getValue("g")
            .isA<List<*>>()
            .and {
                get(2).isEqualTo("C")
            }
            .and {
                get(10).isEqualTo(false)
            }
            .and {
                get(11)
                    .isA<Map<String, Any>>()
                    .getValue("BB")
                    .isA<Map<String, Any>>()
                    .getValue("BBB")
                    .isA<Map<String, Any>>()
                    .getValue("VAL2")
                    .isEqualTo(8)
            }
    }
}
