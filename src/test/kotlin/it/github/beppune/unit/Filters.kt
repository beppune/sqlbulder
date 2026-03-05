package it.github.beppune.unit

import io.github.beppune.sqlbuilder.QueryBuilder
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

}