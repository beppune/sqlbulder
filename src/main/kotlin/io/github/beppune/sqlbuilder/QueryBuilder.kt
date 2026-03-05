package io.github.beppune.sqlbuilder

interface SqlPart {
    fun build(): String
}

interface JoinBuilder: SqlPart

interface SelectBuilder: SqlPart {
    fun from(t:String): JoinBuilder
}

class QueryBuilder(val projections: List<Projection>) : SelectBuilder, JoinBuilder {

    override fun build(): String {
        return projections.joinToString(
            prefix = "SELECT ",
            separator = ", ",
            postfix = " ",
            transform = Projection::build
        )
    }

    override fun from(t: String): JoinBuilder {
        return this
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