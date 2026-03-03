package io.github.beppune.sqlbuilder

interface Projection {
    fun build(): String
}

class StringProjection(val value:String) : Projection {
    override fun build(): String = value
}

class QuotedProjection(val value: Projection) : Projection {
    override fun build(): String = "'${value.build()}'"
}

fun q(p: Projection): Projection = QuotedProjection(p)
fun q(s:Any): Projection = QuotedProjection(StringProjection(s.toString()))

class AliasProjection(val value: Projection, val alias:String) : Projection {
    override fun build(): String = "${value.build()} AS $alias"
}

infix fun String.AS(alias:String): Projection = AliasProjection(StringProjection(this), alias)
infix fun Projection.AS(alias:String): Projection = AliasProjection(this, alias)

class ConcatProjection(val projections: List<Projection>) : Projection {
    override fun build() =
        projections.joinToString(
            prefix = "CONCAT(",
            postfix = ")",
            separator = ", ",
            transform = Projection::build
        )
}

fun CONCAT(vararg cat: Any?): Projection {
    val list = cat.filterNotNull()
        .map {
            when(it) {
                is String -> StringProjection(it)
                is Projection -> it
                is Int -> StringProjection(it.toString())
                is Float -> StringProjection(it.toString())
                is Double -> StringProjection(it.toString())
                else -> throw ProjectionException("Illegal source type: ${it::class.java}")
            }
        }
    return ConcatProjection(list)
}