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

}