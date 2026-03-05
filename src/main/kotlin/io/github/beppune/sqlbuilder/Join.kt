package io.github.beppune.sqlbuilder

interface Join {
    fun build(): String
}

class Tablename(val name:String): Join {
    override fun build(): String = name
}