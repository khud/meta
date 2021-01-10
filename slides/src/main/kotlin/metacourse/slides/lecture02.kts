import io.kotex.beamer.column
import io.kotex.beamer.columns
import io.kotex.beamer.frame
import io.kotex.bibtex.cite
import io.kotex.core.*
import metacourse.slides.createPreamble
import metacourse.slides.mcCarty60
import java.io.File

val doc = document(createPreamble("Метавычисления")) {
    makeTitle()

    section("История") {
        frame("Валентин Федорович Турчин") {
            columns {
                column(0.6 * textWidth()) {
                    +"Валентин Фёдорович Турчин (1931 -- 2010)"
                }
                column(0.4 * textWidth()) {
                    includeGraphics("../assets/turchin.jpg", textWidth())
                }
            }
        }
    }

    section("Языки программирования") {
        subsection("Языки типа Lisp") {
            frame("История вопроса") {
                +"""
            Язык программирования Lisp был предложен в работе Джона МакКарти (John McCarthy)
            в 1958г. ${cite(mcCarty60)}
            """.footnote("History of Lisp: http://jmc.stanford.edu/articles/lisp/lisp.pdf")
            }

            frame("Деревья абстрактного синтаксиса") {
                +"""
            Старайтесь избегать плохого перевода с английского термина ${"abstract syntax tree".textbf()}
            как абстрактное синтаксическое дерево. Дело в том что дерево на самом деле очень конкретное, а
            синтаксис абстрактный.
            """
            }

            frame("Атомы и S-выражения") {

            }

            frame("Рекурсия") {

            }

            frame("Плоские языки") {

            }

//        frame("Связь с \\lambda-исчислением") {
//
//        }
        }
    }
}.toTex()

val outputDir = File("../../../../../tex/")
if (!outputDir.exists()) outputDir.mkdirs()

File("$outputDir/lecture02.tex").writeText(doc)