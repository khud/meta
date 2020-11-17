package metacourse.tsg


sealed class Exp

abstract class Val : Exp()

abstract class Var(val name: Name) : Exp() {
    override fun equals(other: Any?): Boolean {
        return other != null
                && other is Var
                && this::class == other::class
                && this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class Atom(val name: String) : Val() {
    override fun equals(other: Any?): Boolean {
        return other != null && other is Atom && other.name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class Cons(val head: Exp, val tail: Exp) : Exp()

open class Pve(name: Name) : Var(name) {
    override fun toString(): String = "e.$name"
}

class Pva(name: Name) : Pve(name) {
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

data class Bind(val variable: Var, val value: Exp) {
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

// ------------------

abstract class CVar(val index: Int) : Exp() {
    override fun equals(other: Any?): Boolean {
        return other != null
                && other is CVar
                && this::class == other::class
                && this.index == other.index
    }

    override fun hashCode(): Int {
        return index.hashCode()
    }
}

open class Cve(index: Int) : CVar(index) {
    override fun toString(): String = "E.$index"
}

class Cva(index: Int) : Cve(index) {
    override fun toString(): String = "A.$index"
}

typealias CBind = Bind
typealias CEnv = Env
typealias CExp = Exp

class InEq(val left: CExp, val right: CExp) {
    override fun equals(other: Any?): Boolean {
        return other != null && other is InEq &&
                ((left == other.left && right == other.right) || (left == other.right && right == other.left))
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + right.hashCode()
        return result
    }
}

sealed class Restriction

object Inconsistent : Restriction()

class Restr(val inEqs: List<InEq>) : Restriction()

fun isContradiction(inEq: InEq): Boolean = inEq.left == inEq.right

fun isTautology(inEq: InEq): Boolean =
    if (inEq.left is Atom && inEq.right is Atom) {
        inEq.left == inEq.right
    } else false

fun cleanRestr(restr: Restriction): Restriction =
    when (restr) {
        is Inconsistent -> Inconsistent
        is Restr ->
            if (restr.inEqs.any(::isContradiction))
                Inconsistent
            else
                Restr(restr.inEqs.filter(::isTautology).distinct())
    }