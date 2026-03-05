package io.github.beppune.sqlbuilder

import sun.tools.jconsole.Tab

interface Join {
    fun build(): String
}

class TableName(val name:String): Join {
    override fun build(): String = name
}

fun map2join(any: Any): Join = when(any) {
    is String -> TableName(any)
    is Join -> any
    else -> throw JoinException("Unknown join source type: ${any.javaClass}")
}