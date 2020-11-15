package metacourse.tsg

import java.lang.StringBuilder

fun ProgR.pretty(): String {
    val builder = StringBuilder()
    this.forEach {
        builder.appendLine(it.pretty())
        builder.appendLine()
    }
    return builder.toString()
}

fun FDef.pretty(): String {
    val params = params.joinToString(separator = ",") { it.pretty() }
    return """DEFINE ${name}($params)
        |${body.pretty().indent()}
    """.trimMargin()
}

fun Exp.pretty(): String {
    return when (this) {
        is Pva -> "a.$name"
        is Pve -> "e.$name"
        is Atom -> "'$name"
        is Cons -> "(CONS $head $tail)"
        else -> throw IllegalArgumentException()
    }
}

fun Cond.pretty(): String {
    return when (this) {
        is IsEqa -> "EQA' $x $y"
        is IsCons -> "CONS' $exp $head $tail $atom"
    }
}

fun Term.pretty(): String {
    fun List<Exp>.pretty(): String {
        return this.joinToString(separator = ",") { it.pretty() }
    }

    return when (this) {
        is Call -> "CALL ${name}(${args.pretty()})"
        is Alt -> """IF (${cond.pretty()}):
            |${then.pretty().indent()}
            |ELSE:
            |${otherwise.pretty().indent()}
        """.trimMargin()
        is Ret -> exp.pretty()
    }
}

fun String.indent(): String {
    return this.lines().joinToString(separator = System.lineSeparator()) { "\t$it" }
}
