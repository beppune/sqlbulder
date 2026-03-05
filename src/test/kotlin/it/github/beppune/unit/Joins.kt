package it.github.beppune.unit

import io.github.beppune.sqlbuilder.ON
import io.github.beppune.sqlbuilder.q
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

    @Test
    fun conditional() {
        val join = select("one", "two", "three")
            .from("left", "right" ON ("uid" to "user_id") )

        val expected = "SELECT one, two, three FROM left JOIN right ON(uid=user_id) "

        assertEquals(expected, join.build())
    }

}