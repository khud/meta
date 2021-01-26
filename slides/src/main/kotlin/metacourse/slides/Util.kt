package metacourse.slides

import io.kotex.beamer.*
import io.kotex.core.*

fun createPreamble(lectureTitle: String): Preamble {
    val preamble = beamer {
        title = "Введение в метавычисления"
        subtitle = lectureTitle
        author = "Виталий Худобахшов" / "МКН СПбГУ"
        theme = "metropolis"
        date = today()
        aspectRatio = AspectRatio.RATIO_1610
    }
    preamble.usePackage("fontenc", opts = listOf("T2A"))
    preamble.usePackage("inputenc", opts = listOf("utf8"))
    preamble.usePackage("babel", opts = listOf("russian"))
    return preamble
}

val Frame.consAB
    get() = this.verb("(CONS a b)")

val Frame.atomA
    get() = this.verb("(ATOM a)")

fun Frame.withImage(imagePath: String, ratio: Double = 0.6, left: Column.() -> Unit): Columns =
    columns {
        column(ratio * textWidth(), left)
        column((1.0 - ratio) * textWidth()) {
            includeGraphics(imagePath, textWidth())
        }
    }

