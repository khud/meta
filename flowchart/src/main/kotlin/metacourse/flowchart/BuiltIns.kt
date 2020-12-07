package metacourse.flowchart

import java.lang.IllegalArgumentException

object BuiltIns {
    val cons = BuiltIn("cons") {
        Op("cons", it)
    }

    val hd = headTailTemplate("hd", 0)

    val tl = headTailTemplate("tl", 1)

    private fun headTailTemplate(name: String, position: Int) =
        BuiltIn(name) { args ->
            if (args.size > 1) throw IllegalArgumentException("$name function accepts only one argument buf found ${args.size}")
            val arg = args[0]
            if (arg is Op && arg.func == "cons") {
                arg.params[position]
            } else throw IllegalArgumentException("The only argument should be non empty list but found $arg")
        }
}