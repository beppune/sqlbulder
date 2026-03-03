package io.github.beppune.sqlbuilder

interface SqlPart {
    fun build(): String
}

interface SelectBuilder: SqlPart {
    override fun build(): String
}

class QueryBuilder(val projections: List<Projection>) : SelectBuilder {
    override fun build(): String {
        return projections.joinToString(
            prefix = "SELECT ",
            separator = ", ",
            postfix = " ",
            transform = Projection::build
        )
    }
}

fun select(vararg columns: Any?): SelectBuilder {
    val list:List<Projection> = columns.filterNotNull()
        .map {
            when (it) {
                is String -> StringProjection(it)
                is Int -> StringProjection(it.toString())
                is Float -> StringProjection(it.toString())
                is Double -> StringProjection(it.toString())
                is Projection -> it
                else -> throw ProjectionException("Illegal source type: ${it::class.java}")
            }
        }
    return select(list)
}

fun select(columns: List<Projection>): SelectBuilder {
    return QueryBuilder( columns )
}