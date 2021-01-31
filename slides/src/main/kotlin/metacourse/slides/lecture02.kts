import io.kotex.beamer.column
import io.kotex.beamer.columns
import io.kotex.beamer.frame
import io.kotex.bibtex.cite
import io.kotex.core.*
import metacourse.slides.Bib
import metacourse.slides.createPreamble
import metacourse.slides.withImage

val doc = document(createPreamble("Языки программирования")) {
    makeTitle()

    section("Языки типа Lisp") {
        frame("Джон Маккарти") {
            withImage("../assets/mccarthy.png") {
                +"""${"Джон Маккарти".bold()} (англ. John McCarthy; 4 сентября 1927, Бостон — 24 октября 2011, Стэнфорд)
                — американский информатик, автор термина «искусственный интеллект» (1956), 
                изобретатель языка Лисп (1958)${footnote("History of Lisp:" + url("http://jmc.stanford.edu/articles/lisp/lisp.pdf"))}${
                    cite(
                        Bib.mcCarty60
                    )
                }, 
                основоположник функционального программирования, 
                лауреат премии Тьюринга (1971) за огромный вклад в область исследований искусственного 
                интеллекта.""".footnote(url("https://en.wikipedia.org/wiki/John_McCarthy_(computer_scientist)"))
            }
        }

        frame("Структура и интерпретация компьютерных программ") {
            withImage("../assets/sicp.png", ratio = 0.7) {
                +"""Компилятор любого функционального языка должен останавливать каждого,
                кто не читал эту книгу."""
            }
        }

        frame("Деревья абстрактного синтаксиса") {
            +"""
            Старайтесь избегать плохого перевода с английского термина ${"abstract syntax tree".textbf()}
            как абстрактное синтаксическое дерево. Дело в том что дерево на самом деле очень конкретное, а
            синтаксис абстрактный.
            """
        }

        frame("Атомы и S-выражения") {
            +"""S-выражения это выражения, состоящие из символов, от англ. ${"symbolic expressions".bold()},
            представляют собой (вложенные) списки. Например:"""

            verbatim("'((this is (a nested)) list (that (represents a) tree))")

            +"""Здесь ${verb("'this")} - это сокращенная форма для ${verb("(quote this)")}."""

            +"\n\n"

            +"Атомы - это листья S-выражений, как строки, только для них нельзя разложить посимвольно, т.е. они атомарны."
        }

        frame("S-выражения как деревья абстрактного синтаксиса") {
            +"""Lisp - это уникальный язык, в котором деревья абстрактного синтаксиса являются
            неотъемлемой частью языка.    
            """
        }

        frame("Язык РЕФАЛ") {
            +"""РЕФАЛ (${"РЕ".bold()}курсивных ${"Ф".bold()}ункций ${"АЛ".bold()}горитмический) — один из старейших функциональных языков программирования, 
            ориентированный на символьные вычисления: обработку символьных строк. Созданный
            В. Турчиным в 1966 г. (первая реализация появилась в 1968 г).""".footnote(url("https://en.wikipedia.org/wiki/Refal"))
           
            +"Это один из первых языков, где было реализовано сопоставление с образцом."
        }

        frame("Hello world") {
            verbatim("""
            ${"$"}ENTRY Go { = <Hello>;}
            Hello {
               = <Prout 'Hello world'>;
            }
            """)
        }

        frame("Плоские языки") {

        }
    }

}

doc.write("../../../../../tex/lecture02.tex")