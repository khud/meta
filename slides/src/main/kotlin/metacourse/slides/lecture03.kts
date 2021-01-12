import io.kotex.beamer.frame
import io.kotex.bibtex.cite
import io.kotex.core.document
import io.kotex.core.makeTitle
import metacourse.slides.*
import java.io.File

val doc = document(createPreamble("Язык TSG")) {
    makeTitle()

    section("Введение") {
        frame("Язык реализации") {
            +"""
            Язык TSG${footnote("TSG - сокращение от Typed S-Graph")} - это алгоритмически полный функциональный 
            язык первого порядка, ориентированный на обработку символьной информации. 
              
            TSG является модификацией языка S-Graph${cite(gluck1993occam)}, который, в свою очередь связан с
            понятием Рефал-граф${cite(turchin88algorithm)}.
            """
        }
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
 
            term  ::= (IF cond term_1 term_2)
                    | (CALL fname [arg_1, ..., arg_n])           n >= 0
                    | e-exp

            cond  ::= (EQ a-exp a-exp)
                    | (MATCH e-exp e-var e-var a-var)
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
            В условных конструкциях ${verb("IF (cond, then, otherwise)")} языка TSG в качестве условия ветвления—cond—могут 
            быть использованы следующие конструкции${footnote("В оригинальной работе EQA' и CONS' соответственно")}:
            """
            itemize {
                -verb("EQ(x, y)")
                -verb("MATCH(x, head, tail, atom)")
            }
        }

        frame("Конструкция EQ") {
            verbatim("EQ(x, y)")

            +"""
            Проверка равенства двух a-значений (атомов)—если значения a-выражений x и y— суть один и тот же 
            атом, то проверка считается успешной (условие выполнено), в противном случае проверка считается 
            неуспешной (условие не выполнено);
            """
        }

        frame("Конструкция MATCH") {
            verbatim("MATCH(x, head, tail, atom)")

            val head = verb("head")
            val tail = verb("tail")
            val atom = verb("atom")

            +"Анализ вида e-значения:"
            itemize {
                -"""
                если значение e-выражения x имеет вид $consAB, то e-переменные $head, $tail связываются (принимают 
                значения) соответственно с e-значениями a, b и проверка считается успешной;    
                """
                -"""
                если значение x имеет вид $atomA, то a-переменная $atom связывается с a-значением x и 
                проверка считается неуспешной.    
                """
            }
        }

        frame("Простейшая программа") {

        }

        frame("Поиск подстроки в строке") {
            verbatim("""
            DEFINE("Match", eSubstr, eString) {
                CALL("CheckPos", eSubstr, eString, eSubstr, eString)
            },
            
            DEFINE("NextPos", eSubstring, eString) {
                IF (MATCH( eString, e_,  eStringTail, a_)) {
                    CALL("Match", eSubstring, eStringTail)
                } ELSE {
                    FAILURE
                }
            }
            """)
        }

        frame("Поиск подстроки в строке (продолжение)") {
            verbatim("""
            DEFINE("CheckPos", eSubs, eStr, eSubstring, eString) {
              IF (MATCH(eSubs, eSubsHead, eSubsTail, a_)) {
                IF (MATCH(eSubsHead, e_,  e_,  aSubsHead)) { FAILURE } ELSE {
                  IF (MATCH(eStr, eStrHead, eStrTail, a_)) {
                    IF (MATCH(eStrHead, e_, e_, aStrHead)) { FAILURE } ELSE {
                      IF (MATCH(eStrHead, e_, e_, aStrHead)) { FAILURE } ELSE {
                        IF (EQ(aSubsHead, aStrHead)) {
                          CALL("CheckPos", eSubsTail, eStrTail, eSubstring, eString)
                        } ELSE {
                          CALL("NextPos", eSubstring, eString)
                        }}}
                  } ELSE { FAILURE }}
                } ELSE { SUCCESS }}
            """)
        }
    }

    section("Семантика языка") {
        subsection("Среда, связи и состояние") {

        }

        subsection("Интерпретатор языка") {
            frame("Функция int") {
                verbatim("""
                fun int(prog: ProgR, d: List<Exp>): Exp {
                  val f = prog.first()
                  val env = mkEnv(f.params, d)
                  return eval(State(CALL(f.name, *f.params.toTypedArray()), env), prog)
                }
                
                data class CondRes(val res: Boolean, val env: Env)
                
                fun mkEnv(vars: List<Var>, vals: List<Exp>) = 
                  vars.zip(vals).map { Bind(it.first, it.second) }

                fun getDef(name: FName, prog: ProgR): FDef? = 
                  prog.find { f -> f.name == name }
                """)
            }

            frame("Функция eval") {
                verbatim("""
                fun eval(state: State, prog: ProgR): Exp =
                  when (val term = state.term) {
                    is Call -> {
                      val f = getDef(term.name, prog)!!
                      val env = mkEnv(f.params, term.args / state.env)
                      eval(State(f.body, env), prog)
                    }
                    is Alt -> {
                      val condRes = cond(term.cond, state.env)
                      val env = state.env + condRes.env
                      val next = if (condRes.res) term.then else term.otherwise
                      eval(State(next, env), prog)
                    }
                    is Ret -> term.exp / state.env
                  }""")
            }

            frame("Функция cond") {
                verbatim("""
                fun cond(cond: Cond, env: Env): CondRes =
                  when (cond) {
                    is IsEqa -> {
                      val x = cond.x / env
                      val y = cond.y / env
                      CondRes(x is Atom && y is Atom && x.name == y.name, listOf())
                    }
                    is IsCons -> {
                      when (val x = cond.exp / env) {
                        is Cons -> CondRes(true, listOf(Bind(cond.head, x.head), 
                                                        Bind(cond.tail, x.tail)))
                        is Atom -> CondRes(false, listOf(Bind(cond.atom, x)))
                        else -> throw IllegalArgumentException(x.toString())
                      }
                    }}""")
            }
        }
    }
}.toTex()

val outputDir = File("../../../../../tex/")
if (!outputDir.exists()) outputDir.mkdirs()

File("$outputDir/lecture03.tex").writeText(doc)