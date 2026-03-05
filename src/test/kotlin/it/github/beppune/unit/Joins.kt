package it.github.beppune.unit

import io.github.beppune.sqlbuilder.select
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Joins {

    @Test
    fun table() {

        val joins = select()
            .from("table")

        val expected = "SELECT * FROM table "
        assertEquals(expected, joins.build())

    }

    @Test
    fun simplejoin() {
        var joins = select("one", "two", "three")
            .from("table")
                .join("right")

        var expected = "SELECT one, two, three FROM table JOIN right "

        assertEquals(expected, joins.build())

        joins = select("one", "two", "three")
            .from("left", "right")

        expected = "SELECT one, two, three FROM left JOIN right "

        assertEquals(expected, joins.build())
    }

}