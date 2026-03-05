package io.github.beppune.sqlbuilder

interface Filter {
    fun build(): String
}

class StringFilter(val s: String) : Filter {
    override fun build(): String = s.trim()
}

class LikeFilter(val left:String, val right: String) : Filter {
    override fun build(): String = "$left LIKE '$right%'"
}

class InFilter(val left:String, val right:List<String>) : Filter {
    override fun build(): String = right.joinToString(
        prefix = "$left IN(",
        separator = ", ",
        postfix = ")",
        transform = { "'$it'" }
    )
}

class NotFilter(val filter:Filter) : Filter {
    override fun build(): String = "NOT(${filter.build()})"
}

infix fun String.LIKE(right:String) = LikeFilter(this, right)

infix fun String.IN(right: List<String>) = InFilter(this, right)

fun NOT(f:Filter): Filter = NotFilter(f)

fun map2filter(any: Any): Filter = when(any) {
    is String -> StringFilter(any)
    is Filter -> any
    else -> throw FilterException("Unknown filter source type: ${any.javaClass}")
}
