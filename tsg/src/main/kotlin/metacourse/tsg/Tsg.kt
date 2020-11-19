package metacourse.tsg

/*
 * a-val ::= (ATOM atom)
 *
 * e-val ::= a-val | (CONS e-val e-val)
 *
 * prog  ::= [def_1, ..., def_n]                        n >= 1
 *
 * def   ::= (DEFINE fname [parm_1, ..., parm_n])     n >= 0
 *
 * term  ::= (ALT cond term_1 term_2)
 *         | (CALL fname [arg_1, ..., arg_n])           n >= 0
 *         | e-exp
 *
 * cond  ::= (EQA' a-exp a-exp)
 *         | (CONS' e-exp e-var e-var a-var)
 *
 * a-exp ::= a-val | a-var
 *
 * e-exp ::= a-val | a-var | e-var | (CONS e-exp e-exp)
 *
 * parm  ::= a-var | e-var
 *
 * arg   ::= a-exp | e-exp
 *
 * a-var ::= (PVA name)
 *
 * e-var ::= (PVE name)
 */

interface Exp

interface Val: Exp

interface Var: Exp

class Atom(val name: String) : Val {
    override fun equals(other: Any?): Boolean {
        return other != null && other is Atom && other.name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String = "'$name"
}

data class Cons(val head: Exp, val tail: Exp) : Exp

data class Pve(val name: Name) : Var {
    override fun toString(): String = "e.$name"
}

data class Pva(val name: Name) : Var {
    override fun toString(): String = "a.$name"
}

typealias Name = String

sealed class Cond

data class IsEqa(val x: Exp, val y: Exp) : Cond()

data class IsCons(val exp: Exp, val head: Pve, val tail: Pve, val atom: Pva) : Cond()

typealias FName = String
typealias Param = Var

sealed class Term

data class Alt(val cond: Cond, val then: Term, val otherwise: Term) : Term()

data class Call(val name: FName, val args: List<Exp>) : Term()

data class Ret(val exp: Exp) : Term() {
    override fun toString(): String = exp.toString()
}

data class FDef(val name: FName, val params: List<Param>, val body: Term)

typealias ProgR = List<FDef>


fun DEFINE(name: FName, vararg params: Var, body: () -> Term) = FDef(name, params.toList(), body())

fun CALL(name: FName, vararg args: Exp) = Call(name, args.toList())

class Alt1(private val cond: Cond, private val then: Term) {
    infix fun ELSE(body: () -> Term): Alt = Alt(cond, then, body())
}

fun IF(cond: Cond, then: () -> Term) = Alt1(cond, then())

fun MATCH(exp: Exp, head: Pve, tail: Pve, atom: Pva) = IsCons(exp, head, tail, atom)

fun EQ(x: Exp, y: Exp) = IsEqa(x, y)

// ------------

class Bind(val variable: Var, val value: Exp) {
    override fun equals(other: Any?): Boolean =
        other != null && other is Bind && other.variable == this.variable

    override fun hashCode(): Int = variable.hashCode()
}

typealias Env = List<Bind>

data class State(val term: Term, val env: Env)

operator fun List<Exp>.div(env: Env): List<Exp> = this.map { it / env }

operator fun Exp.div(env: Env): Exp {
    return when (this) {
        is Atom -> this

        is Cons -> Cons(head / env, tail / env)

        is Var -> env.find { it.variable == this }!!.value

        else -> throw NonExhaustiveMatchException(this)
    }
}

class NonExhaustiveMatchException(obj: Any) : Exception("Non-exhaustive match $obj")

operator fun Env.plus(other: Env): Env = this.plus(other as Iterable<Bind>).distinct()