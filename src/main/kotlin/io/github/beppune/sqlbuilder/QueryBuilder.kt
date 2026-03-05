package io.github.beppune.sqlbuilder

interface SqlPart {
    fun build(): String
}

interface JoinBuilder: SqlPart {
    fun join(sb: StringBuilder)
}

interface SelectBuilder: SqlPart {
    fun project(sb:StringBuilder)
    fun from(t:String): JoinBuilder
}

class QueryBuilder(
    val projections: List<Projection>,
    val joins: MutableList<Join> = mutableListOf(),
    ) : SelectBuilder, JoinBuilder {

    override fun build(): String {
        val sb = StringBuilder()
        project(sb)
        join(sb)
        return sb.toString()
    }

    override fun project(sb: StringBuilder) {

        if (projections.isEmpty()) {
            sb.append("SELECT * ")
            return
        }

        projections.joinToString(
            prefix = "SELECT ",
            separator = ", ",
            postfix = " ",
            transform = Projection::build
        ).also(sb::append)

    }

    override fun from(t: String): JoinBuilder {
        joins.add(0, Tablename(t))
        return this
    }

    override fun join(sb: StringBuilder) {
        if( joins.isNotEmpty() ) {
            joins.joinToString(
                prefix = "FROM ",
                separator = "JOIN ",
                postfix = " ",
                transform = Join::build
            ).also(sb::append)
        }
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