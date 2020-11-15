package metacourse.slides

import io.kotex.beamer.AspectRatio
import io.kotex.beamer.beamer
import io.kotex.core.Preamble

fun createPreamble(lectureTitle: String): Preamble {
    val preamble = beamer {
        title = "Введение в метавычисления"
        subtitle = "Лекция 1: Введение"
        author = "Vitaly Khudobakhshov" / "JetBrains"
        theme = "metropolis"
        date = today()
        aspectRatio = AspectRatio.RATIO_1610
    }
    preamble.usePackage("fontenc", opts = listOf("T2A"))
    preamble.usePackage("inputenc", opts = listOf("utf8"))
    preamble.usePackage("babel", opts = listOf("russian"))
    return preamble
}