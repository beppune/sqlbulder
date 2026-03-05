package io.github.beppune.sqlbuilder

interface Filter {
    fun build(): String
}

class StringFilter(val s: String) : Filter {
    override fun build(): String = s.trim()
}

fun map2filter(any: Any): Filter = when(any) {
    is String -> StringFilter(any)
    is Filter -> any
    else -> throw FilterException("Unknown filter source type: ${any.javaClass}")
}
