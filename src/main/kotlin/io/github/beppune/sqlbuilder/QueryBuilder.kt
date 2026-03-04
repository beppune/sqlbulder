package io.github.beppune.sqlbuilder

interface SqlPart {
    fun build(): String
}

interface SelectBuilder: SqlPart {
    override fun build(): String
}

class QueryBuilder(val projections: List<Projection>) : SelectBuilder {

    companion object {
        fun map2projections(any:Any): Projection = when (any) {
            is String -> StringProjection(any)
            is Int -> StringProjection(any.toString())
            is Float -> StringProjection(any.toString())
            is Double -> StringProjection(any.toString())
            is Projection -> any
            else -> throw ProjectionException("Illegal source type: ${any::class.java}")
        }
    }

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
        .map(QueryBuilder::map2projections)
    return select(list)
}

fun select(columns: List<Projection>): SelectBuilder {
    return QueryBuilder( columns )
}