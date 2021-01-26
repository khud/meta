import io.kotex.beamer.column
import io.kotex.beamer.columns
import io.kotex.beamer.frame
import io.kotex.bibtex.cite
import io.kotex.core.*
import metacourse.slides.Bib
import metacourse.slides.createPreamble

val doc = document(createPreamble("Языки программирования")) {
    makeTitle()

    section("Языки программирования") {
        subsection("Языки типа Lisp") {
            frame("История вопроса") {
                +"""
            Язык программирования Lisp был предложен в работе Джона МакКарти (John McCarthy)
            в 1958г. ${cite(Bib.mcCarty60)}
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

doc.write("../../../../../tex/lecture02.tex")