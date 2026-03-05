package it.github.beppune.unit

import io.github.beppune.sqlbuilder.IN
import io.github.beppune.sqlbuilder.LIKE
import io.github.beppune.sqlbuilder.ON
import io.github.beppune.sqlbuilder.select
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Filters {

    @Test
    fun empty() {
        val p = select()
            .from().where()

        val expected = "SELECT * "

        assertEquals(expected, p.build())
    }

    @Test
    fun string() {
        val p = select("one", "two", "three")
            .from("table")
            .where( "one=uid" )
        val expected = "SELECT one, two, three FROM table WHERE one=uid "

        assertEquals(expected, p.build())
    }

    @Test
    fun like() {
        val p = select("one", "two", "three")
            .from("table")
            .where( "uid" LIKE "UID" )
        val expected = "SELECT one, two, three FROM table WHERE uid LIKE 'UID%' "

        assertEquals(expected, p.build())
    }

    @Test
    fun inlist() {val p = select("one", "two", "three")
        .from("table")
        .where( "uid" IN listOf("ONE", "TWO", "THREE") )
        val expected = "SELECT one, two, three FROM table WHERE uid IN('ONE', 'TWO', 'THREE') "
        assertEquals(expected, p.build())
    }

    @Test
    fun mixed() {
        val p = select("one", "two", "three")
            .from("left", "right" ON ("uid" to "user_id") )
            .where( "uid" LIKE "UID", "uid" IN listOf("ONE", "TWO", "THREE") )

        val expected = "SELECT one, two, three FROM left JOIN right ON(uid=user_id) WHERE uid LIKE 'UID%' AND uid IN('ONE', 'TWO', 'THREE') "
        assertEquals(expected, p.build())
    }

}