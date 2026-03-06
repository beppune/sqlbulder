package it.github.beppune.unit

import io.github.beppune.sqlbuilder.LIKE
import io.github.beppune.sqlbuilder.select
import kotlin.test.Test
import kotlin.test.assertEquals

class Orders {

    @Test
    fun order1() {
        var p = select("one", "two", "three")
            .from("table")
            .where("uid" LIKE "UID")
            .orderby("one", "two").desc()

        var expected = "SELECT one, two, three FROM table WHERE uid LIKE 'UID%' ORDER BY one, two DESC "
        assertEquals(expected, p.build())

        p =  select("one", "two", "three")
            .from("table")
            .where("uid" LIKE "UID")
            .orderby("one", "two")

        expected = "SELECT one, two, three FROM table WHERE uid LIKE 'UID%' ORDER BY one, two ASC "
        assertEquals(expected, p.build())
    }
}