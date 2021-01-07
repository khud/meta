import io.kotex.beamer.frame
import io.kotex.bibtex.cite
import io.kotex.core.document
import io.kotex.core.footnote
import io.kotex.core.makeTitle
import metacourse.slides.*
import java.io.File

val doc = document(createPreamble("Язык TSG")) {
    makeTitle()

    section("Введение") {
        +"""
        Язык TSG${footnote("TSG - сокращение от Typed S-Graph")} - это алгоритмически полный функциональный 
        язык первого порядка, ориентированный на обработку символьной информации. 
          
        TSG является модификацией языка S-Graph${cite(gluck1993occam)}, который, в свою очередь связан с
        понятием Рефал-граф${cite(turchin88algorithm)}.
        """
    }

    section("Синтаксис языка") {
        frame("a-значения и e-значения") {
            +"Данные в языке представляют собой:"
            itemize {
                -"Атомы - неделимые элементы из некоторого фиксированного конечного множества;"
                -"S-выражения, построенные при помощи конструктора ${verb("CONS")}."
            }
            +"Также будем использовать следующие обозначения:"
            itemize {
                -"a-значения -- значения, имеющие вид вид  ${verb("(ATOM atom)")};"
                -"e-значения -- произвольные S-выражения;"
                -"${verb("AVal")} - множество всех a-значений;"
                -"${verb("EVal")} - множество всех e-значений."
            }
        }

        frame("Грамматика, часть I") {
            verbatim("""
            a-val ::= (ATOM atom)
 
            e-val ::= a-val | (CONS e-val e-val)

            prog  ::= [def_1, ..., def_n]                        n >= 1
 
            def   ::= (DEFINE fname [param_1, ..., param_n])     n >= 0
 
            term  ::= (ALT cond term_1 term_2)
                    | (CALL fname [arg_1, ..., arg_n])           n >= 0
                    | e-exp

            cond  ::= (EQA' a-exp a-exp)
                    | (CONS' e-exp e-var e-var a-var)
            """)
        }

        frame("Грамматика, часть II") {
            verbatim("""
            a-exp ::= a-val | a-var
 
            e-exp ::= a-val | a-var | e-var | (CONS e-exp e-exp)
            
            param  ::= a-var | e-var
            
            arg   ::= a-exp | e-exp
            
            a-var ::= (PVA name)

            e-var ::= (PVE name)     
            """)
        }

        frame("Некоторые полезные сокращения") {
            itemize {
                -"${verb("'atom")} - сокращенная запись a-значения ${verb("(ATOM atom)")};"
                -"${verb("a.name")}, ${verb("e.name")} - сокращенные записи для a- и е-переменных."
            }
        }

        frame("Типы a- и e- в языке TSG") {
            +"Типизация в языке определена следующим образом:"
            itemize {
                -"Значением a-переменной или a-выражения может быть только a-значение;"
                -"Значением e-переменной или e-выражения может быть произвольное e-значение: S-выражение или атом."
            }
        }

        frame("Ветвления") {
            +"""
            В условных конструкциях ${verb("(ALT cond term1 term2)")} языка TSG в качестве условия ветвления—cond—могут 
            быть использованы следующие конструкции:
            """
            itemize {
                -verb("(EQA' x y)")
                -verb("(CONS' x evh evt av)")
            }
        }

        frame("Конструкция EQA'") {
            itemize {
                -verb("(EQA' x y)")
            }
            +"""
            Проверка равенства двух a-значений (атомов)—если значения a-выражений x и y— суть один и тот же 
            атом, то проверка считается успешной (условие выполнено), в противном случае проверка считается 
            неуспешной (условие не выполнено);
            """
        }

        frame("Конструкция CONS'") {
            itemize {
                -verb("(CONS' x evh evt av)")
            }
            +"Анализ вида e-значения:"
            itemize {
                -"""
                если значение e-выражения x имеет вид ${verb("(CONS a b)")}, то e-переменные evh, evt связываются (принимают 
                значения) соответственно с e-значениями a, b и проверка считается успешной;    
                """
                -"""
                если значение x имеет вид ${verb("(ATOM a)")}, то a-переменная av связывается с a-значением x и 
                проверка считается неуспешной.    
                """
            }
        }

        frame("Простейшая программа") {

        }

        frame("Поиск подстроки в строке") {

        }
    }

    section("Семантика языка") {
        frame("Среда, связи и состояние") {

        }

        frame("Интерпретатор языка") {

        }
    }
}.toTex()

val outputDir = File("../../../../../tex/")
if (!outputDir.exists()) outputDir.mkdirs()

File("$outputDir/lecture03.tex").writeText(doc)