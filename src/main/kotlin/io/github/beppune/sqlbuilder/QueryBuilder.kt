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
        .map(::map2projections)
    return select(list)
}

fun select(columns: List<Projection>): SelectBuilder {
    return QueryBuilder( columns )
}