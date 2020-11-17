package metacourse.tsg

fun int(prog: ProgR, d: List<Exp>): Exp {
    val f = prog.first()
    val env = mkEnv(f.params, d)
    return eval(State(CALL(f.name, *f.params.toTypedArray()), env), prog)
}

fun eval(state: State, prog: ProgR): Exp {
    return when (val term = state.term) {
        is Call -> {
            val f = getDef(term.name, prog)!!
            val env = mkEnv(f.params, term.args / state.env)
            eval(State(f.body, env), prog)
        }

        is Alt -> {
            val condRes = cond(term.cond, state.env)
            val env = state.env + condRes.env
            eval(State(if (condRes.res) term.then else term.otherwise, env), prog)
        }

        is Ret -> term.exp / state.env
    }
}

data class CondRes(val res: Boolean, val env: Env)

fun cond(cond: Cond, env: Env): CondRes =
    when (cond) {
        is IsEqa -> {
            val x = cond.x / env
            val y = cond.y / env

            CondRes(x is Atom && y is Atom && x.name == y.name, listOf())
        }

        is IsCons -> {
            when (val x = cond.exp / env) {
                is Cons -> CondRes(true, listOf(Bind(cond.head, x.head), Bind(cond.tail, x.tail)))

                is Atom -> CondRes(false, listOf(Bind(cond.atom, x)))

                else -> throw NonExhaustiveMatchException(x)
            }
        }
    }

fun mkEnv(vars: List<Var>, vals: List<Exp>) = vars.zip(vals).map { Bind(it.first, it.second) }

fun getDef(name: FName, prog: ProgR): FDef? = prog.find { f -> f.name == name }