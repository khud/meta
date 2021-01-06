import io.kotex.beamer.frame
import io.kotex.core.document
import io.kotex.core.makeTitle
import metacourse.slides.createPreamble
import java.io.File

val doc = document(createPreamble("Язык TSG")) {
    makeTitle()

    section("Введение") {

    }

    section("Синтаксис языка") {
        frame("a-значения и e-значения") {

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

        frame("Ветвления") {

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