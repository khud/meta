import io.kotex.beamer.Frame
import io.kotex.beamer.frame
import io.kotex.bibtex.bibliography
import io.kotex.bibtex.cite
import io.kotex.bibtex.generateBibTex
import io.kotex.core.*
import metacourse.slides.*

val doc = document(createPreamble("Язык TSG")) {
    makeTitle()

    fun Frame.atom(a: String) = verb("(ATOM $a)")

    section("Введение") {
        frame("Язык реализации") {
            +"""
            Язык TSG${footnote("TSG - сокращение от Typed S-Graph")} - это алгоритмически полный функциональный 
            язык первого порядка, ориентированный на обработку символьной информации${cite(Bib.abramovMeta)}. 
              
            TSG является модификацией языка S-Graph${cite(Bib.gluck1993occam)}, который, в свою очередь связан с
            понятием Рефал-граф${cite(Bib.turchin88algorithm)}.
            """
        }

        frame("Методическое пособие") {
            withImage("../assets/metacomputing.png", ratio = 0.7) {
                +"""Это одна из трех существующих книг, посвященных вопросу метавычислений, 
                    в нашем курсе это будет аналог учебника. Некоторые доказательства в ней 
                    для краткости опущены и код приводится частично.""".footnote("Более подробное изложение можно найти в докторской диссертации С. М. Абрамова")
                +"""Нужно также помнить,
                    что код в книге написан не на языке Haskell, а на языке Gofer."""
            }
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
            verbatim(
                """
            a-val ::= (ATOM atom)
 
            e-val ::= a-val | (CONS e-val e-val)

            prog  ::= [def_1, ..., def_n]                         n >= 1
 
            def   ::= (DEFINE fname [param_1, ..., param_n] term) n >= 0
 
            term  ::= (ALT cond term_1 term_2)
                    | (CALL fname [arg_1, ..., arg_n])            n >= 0
                    | (RETURN e-exp)

            cond  ::= (EQA' a-exp a-exp)
                    | (CONS' e-exp e-var e-var a-var)
            """
            )
        }

        frame("Грамматика, часть II") {
            verbatim(
                """
            a-exp ::= a-val | a-var
 
            e-exp ::= a-val | a-var | e-var | (CONS e-exp e-exp)
            
            param  ::= a-var | e-var
            
            arg   ::= a-exp | e-exp
            
            a-var ::= (PVA name)

            e-var ::= (PVE name)     
            """
            )
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
            verbatim("(EQA' x y)")
            val x = verb("x")
            val y = verb("y")

            +"""
            Проверка равенства двух a-значений (атомов)—если значения a-выражений $x и $y— суть один и тот же
            атом, то проверка считается успешной (условие выполнено), в противном случае проверка считается
            неуспешной (условие не выполнено);
            """
        }

        frame("Конструкция CONS'") {
            verbatim("(CONS' x evh evt av)")
            val x = verb("x")
            val cons = verb("(CONS a b)")
            val evh = verb("evh")
            val evt = verb("evt")
            val a = verb("a")
            val b = verb("b")
            val atomA = atom("a")
            val av = verb("av")

            enumerate {
                -"""если значение e-выражения $x имеет вид $cons, то e-переменные $evh, $evt 
                    связываются (принимают значения) соответственно с e-значениями $a, $b и проверка 
                    считается успешной;"""
                -"""если значение e-выражения $x имеет вид $atomA, то a-переменная $av связывается с a-значением 
                    и проверка считается неуспешной."""
            }
        }

        frame("Простейшая программа на языке TSG") {
            verbatim("""
            id_prog :: Prog
            id_prog = [
              (DEFINE "Id" [e.x]
                (RETURN e.x))
              ] where e.x = (PVE "x")    
            """)
            +"${verb("e.x")} не является допустимым идентификатором в Haskell, поэтому в реальной программе лучше использовать ${verb("e'x")}."
        }

        frame("Поиск подстроки в строке") {
            verbatim("""
            match_prog :: Prog
            match_prog = [
            (DEFINE "Match"[e.substr,e.string]
                (CALL "CheckPos"[e.substr,e.string,e.substr,e.string])
              ),
              (DEFINE "NextPos"[e.substring,e.string]
                (ALT (CONS' e.string e._ e.string_tail a._)
                  (CALL "Match"[e.substring, e.string_tail])
                  (RETURN 'FAILURE)
              )),            
            """
            )
        }

        frame("Поиск подстроки в строке (продолжение)") {
            verbatim("""
              (DEFINE "CheckPos" [e.subs,e.str,e.substring,e.string]
                (ALT (CONS' e.subs e.subs_head e.subs_tail a._)
                  (ALT (CONS' e.subs_head e._ e._ a.subs_head)
                  (RETURN 'FAILURE)
                  (ALT (CONS' e.str e.str_head e.str_tail a._)
                    (ALT (CONS' e.str_head e._ e._ a.str_head)
                      (RETURN 'FAILURE)
                      (ALT (EQA' a.subs_head a.str_head)
                        (CALL "CheckPos"[e.subs_tail,e.str_tail,
                                          e.substring,e.string])
                        (CALL "NextPos"[e.substring,e.string])
                      ))
                    (RETURN 'FAILURE)))
                (RETURN 'SUCCESS)))]
            """
            )
        }
    }

    section("Семантика языка TSG") {
        val env = verb("env")
        val env1 = verb("env'")
        val t = verb("t")

        frame("Среда и связи") {
            val atomAtom = atom("atom")
            +"""Будем называть ${"средой".italic()} список из ${"связей".italic()}—упорядоченных пар вида ${verb("var := value")}, 
                связывающих программные переменные—${"var".italic()}—с их значениями—${"value".italic()}, при этом:"""
            itemize {
                -"значением a-переменной может быть только a-значение: $atomAtom;"
                -"значением e-переменной может быть произвольное е-значение: $atomAtom или S-выражение."
            }
        }

        frame("Состояние вычислений") {
            +"""Упорядоченную пару ${verb("(term ,env)")}, где ${verb("term")}—программный терм, 
                ${verb("env")}—среда, связывающая программные 
                переменные из term с их значениями, будем называть состоянием вычисления."""
            verbatim("""
            state   ::= (term , env)
            env     ::= [binding_1,...,binding_n]               n >= 0
            binding ::= a-var := a-val | e-var := e-val    
            """)
        }

        frame("Операция замены (подстановки)") {
            +"${verb("t /. env")} - замена в конструкции $t вхождений переменных на их значения из среды $env."
            newLine()
            newLine()
            +"Например, в результате операции:"
            verbatim("(CONS (CONS a.x e.z) a.y) /. [a.x:='A,a.y:='B,e.z:='C]")
            +"получаем:"
            verbatim("(CONS (CONS 'A 'C) 'B)")
        }

        frame("Обновление среды") {
            val ivar = "var".italic()
            val ivalue = "value".italic()
            +"Результатом операции ${verb("env +. env'")} является среда, связывающая переменную $ivar со значением $ivalue в том, и только в том случае, если:"
            itemize {
                -"$ivar связано со значением $ivalue в среде $env1, или"
                -"$ivar связано со значением $ivalue в среде $env и при этом $ivar не связано ни с каким значением в среде $env1."
            }
            +"Например, результатом операции:"
            verbatim("[a.x:='A,a.y:='B,e.z:='C] +. [e.z:=(CONS 'D 'E),e.u:='F]")
            +"является:"
            verbatim("[e.z:=(CONS 'D 'E),e.u:='F,a.x:='A,a.y:='B]")
        }

        frame("Вспомогательные функции") {
           verbatim("""
           class APPLY a b where (/.) :: a -> b -> a 
           
           instance APPLY Exp Env where
             (ATOM a) /. env = ATOM a
             (CONS h t) /. env = CONS (h /. env) (t /. env)
             var /. env = head [ x | (v := x) <- env, v == var ]
           
           instance Eq Bind where
             (var1 := _) == (var2 := _) = (var1 == var2)
           """)
        }

        frame("Вспомогательные функции (продолжение)") {
            verbatim("""
            class UPDATE a where (+.) :: a -> a -> a
           
            instance UPDATE Env where
              binds +. binds' = nub (binds' ++ binds)
            
            mkEnv :: [Var] -> [EVal] -> Env
            mkEnv = zipWith (\var val -> (var := val))
            
            getDef :: FName -> Prog -> FDef
            getDef fn p = head [ fd | fd@(DEFINE f _ _) <- p, f == fn ]        
            """)
        }

        frame("Интерпретатор TSG") {
            verbatim("""
            int :: Prog -> [EVal] -> EVal
            int p d = eval s p
                        where (DEFINE f prms _) : p' = p
                              e = mkEnv prms d
                              s = ((CALL f prms), e) 
            """)
        }

        frame("Функция eval") {
            verbatim("""
            eval :: State -> Prog -> EVal
            eval s@((CALL f args), e) p = eval s' p
                                          where DEFINE _ prms t' = getDef f p
                                                e' = mkEnv prms (args /. e)
                                                s' = (t', e')
            
            eval s@((ALT c t1 t2), e) p= case cond c e of
                                           TRUE ue -> eval (t1, e +. ue) p
                                           FALSE ue -> eval (t2, e +. ue) p
            
            eval s@(RETURN exp, e) p = exp /. e
            """)
        }

        frame("Функция cond") {
            verbatim("""
            data CondRes = TRUE Env | FALSE Env
            
            cond :: Cond -> Env -> CondRes
            cond (EQA' x y)         e = let x' = x /. e; y' = y /. e in
                                        case (x', y') of
                                           (ATOM a, ATOM b) | a == b -> TRUE  []
                                           (ATOM a, ATOM b)          -> FALSE []
            
            cond (CONS' x vh vt va) e = let x' = x/.e in
                                        case x' of
                                            CONS h t -> TRUE  [vh := h, vt := t]
                                            ATOM a   -> FALSE [va := x'] 
            """)
        }
    }

    frame("Литература") {
        allowFrameBreaks()
        generateBibTex("lecture03")
        bibliography("plain")
    }
}

doc.write("../../../../../tex/lecture03.tex")