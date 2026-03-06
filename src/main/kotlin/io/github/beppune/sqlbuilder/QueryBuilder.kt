package io.github.beppune.sqlbuilder

interface SqlPart {
    fun build(): String
}

interface SortBuilder: SqlPart {
    fun sort(sb: StringBuilder)
    fun desc(): SortBuilder
}

interface WhereBuilder: SqlPart {
    fun filter(sb: StringBuilder)

    fun orderby(vararg s:String): SortBuilder
}

interface JoinBuilder: SqlPart {
    fun join(sb: StringBuilder)
    fun join(right:String): JoinBuilder

    fun where(vararg any: Any?): WhereBuilder
}

interface SelectBuilder: SqlPart {
    fun project(sb:StringBuilder)
    fun from(vararg any:Any?): JoinBuilder
}

class QueryBuilder(
    val projections: List<Projection>,
    val joins: MutableList<Join> = mutableListOf(),
    val filters: MutableList<Filter> = mutableListOf(),
    val sorts: MutableList<String> = mutableListOf(),
    var asc: Boolean = true,
    ) : SelectBuilder, JoinBuilder, WhereBuilder, SortBuilder {

    override fun build(): String {
        val sb = StringBuilder()
        project(sb)
        join(sb)
        filter(sb)
        sort(sb)
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

    override fun from(vararg any:Any?): JoinBuilder {
        any.filterNotNull().map(::map2join).also(joins::addAll)
        return this
    }

    override fun join(sb: StringBuilder) {
        if( joins.isNotEmpty() ) {
            joins.joinToString(
                prefix = "FROM ",
                separator = " JOIN ",
                postfix = " ",
                transform = Join::build
            ).also(sb::append)
        }
    }

    override fun join(right: String): JoinBuilder {
        joins.add(TableName(right))
        return this
    }

    override fun filter(sb: StringBuilder) {
        if( filters.isNotEmpty() ) {
            filters.joinToString(
                prefix = "WHERE ",
                separator = " AND ",
                postfix = " ",
                transform = Filter::build
            ).also(sb::append)
        }
    }

    override fun orderby(vararg s: String): SortBuilder {
        sorts.addAll(s)
        return this
    }

    override fun where(vararg any: Any?): WhereBuilder {
        any.filterNotNull().map(::map2filter).also(filters::addAll)
        return this
    }

    override fun sort(sb: StringBuilder) {
        if(sorts.isNotEmpty()) {

            val postfix = if (asc) " ASC " else " DESC "

            sorts.joinToString(
                prefix = "ORDER BY ",
                separator = ", ",
                postfix = postfix,
            ).also(sb::append)
        }
    }

    override fun desc(): SortBuilder {
        asc = false
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