package metacourse.slides

import io.kotex.beamer.AspectRatio
import io.kotex.beamer.Frame
import io.kotex.beamer.beamer
import io.kotex.core.Preamble

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