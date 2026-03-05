package it.github.beppune.unit

import io.github.beppune.sqlbuilder.AS
import io.github.beppune.sqlbuilder.CONCAT
import io.github.beppune.sqlbuilder.ProjectionException
import io.github.beppune.sqlbuilder.StringProjection
import io.github.beppune.sqlbuilder.q
import io.github.beppune.sqlbuilder.select
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Projections {

    @Test
    fun nothing() {
        val p = select()
        val expected = "SELECT * "
        assertEquals(expected, p.build())
    }

    @Test
    fun simple() {

        var p = select( "one",  "two",  "three" )
        var expected = "SELECT one, two, three "

        assertEquals(expected, p.build())

        p = select( listOf(StringProjection("one"), StringProjection("two"), StringProjection("three")) )

        assertEquals( expected, p.build())

        p = select( 9, 2F, 3.0,  "one", "two" )
        expected = "SELECT 9, 2.0, 3.0, one, two "
        assertEquals(expected, p.build())

    }

    @Test
    fun quoted() {
        var p = select( q("one"),  q(" "),  q(StringProjection("two")) )
        var expected = "SELECT 'one', ' ', 'two' "

        assertEquals(expected, p.build())

        p = select( q("one"),  q(" "),  "two", q(2F) )
        expected = "SELECT 'one', ' ', two, '2.0' "

        assertEquals(expected, p.build())
    }

    @Test
    fun alias() {
        val p = select( "one" AS "ONE",  q("two") AS "TWO",  "three" )
        val expected = "SELECT one AS ONE, 'two' AS TWO, three "
        assertEquals(expected, p.build())
    }

    @Test
    fun concat() {
        var p = select( CONCAT( "one",  "two",  "three" ) )
        var expected = "SELECT CONCAT(one, two, three) "
        assertEquals(expected, p.build())

        p = select( CONCAT( 9, "one",  q("two"),  "three" ) AS "alias" )
        expected = "SELECT CONCAT(9, one, 'two', three) AS alias "
        assertEquals(expected, p.build())
    }

    @Test
    fun fail() {

        assertFailsWith<ProjectionException> {
            select( object {} )
        }

        assertFailsWith<ProjectionException> {
            select( CONCAT( object {} ) )
        }
    }
}