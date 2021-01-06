import io.kotex.beamer.frame
import io.kotex.bibtex.cite
import io.kotex.core.document
import io.kotex.core.makeTitle
import io.kotex.core.textbf
import metacourse.slides.createPreamble
import metacourse.slides.mcCarty60
import java.io.File

val doc = document(createPreamble("Метавычисления")) {
    makeTitle()

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
}