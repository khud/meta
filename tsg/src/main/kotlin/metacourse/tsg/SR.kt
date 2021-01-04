package metacourse.tsg

typealias CBind = Bind
typealias CEnv = Env
typealias CExp = Exp

open class CVar(val index: Int): Var

class Cve(index: Int) : CVar(index) {
    override fun toString(): String = "E.$index"
}

class Cva(index: Int) : CVar(index) {
    override fun toString(): String = "A.$index"
}

class InEq(val left: CExp, val right: CExp) {
    override fun equals(other: Any?): Boolean {
        return other != null && other is InEq &&
                ((left == other.left && right == other.right) || (left == other.right && right == other.left))
    }

    override fun hashCode(): Int {
        val (x, y) = if (less(left, right)) Pair(left, right) else Pair(right, left)
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String = "$left =/= $right"
}

sealed class Restriction

object Inconsistent : Restriction() {
    override fun toString(): String = "Inconsistent"
}

class Restr(val inEqs: List<InEq>) : Restriction() {
    override fun toString(): String = "Restr($inEqs)"
}


fun isContradiction(inEq: InEq): Boolean = inEq.left == inEq.right

fun isTautology(inEq: InEq): Boolean =
    if (inEq.left is Atom && inEq.right is Atom) {
        inEq.left != inEq.right
    } else false

fun Restriction.clean(): Restriction =
    when (this) {
        is Inconsistent -> Inconsistent
        is Restr ->
            if (inEqs.any(::isContradiction))
                Inconsistent
            else
                Restr(inEqs.filterNot(::isTautology).distinct())
    }

operator fun Restriction.plus(other: Restriction): Restriction {
    if (this is Inconsistent || other is Inconsistent) return Inconsistent
    return Restr((this as Restr).inEqs + (other as Restr).inEqs).clean()
}

fun less(x: CExp, y: CExp): Boolean {
    if (x is Atom && y is Atom) return x.name < y.name
    if (x is Atom && y is Cva) return false     // y < x
    if (x is Atom && y is Cve) return false     // y < x
    if (x is Atom && y is Cons) return true     // x < y

    if (x is Cva && y is Atom) return true      // x < y
    if (x is Cva && y is Cva) return x.index < y.index
    if (x is Cva && y is Cve) return x.index < y.index
    if (x is Cva && y is Cons) return true      // x < y

    if (x is Cve && y is Atom) return true      // x < y
    if (x is Cve && y is Cva) return x.index < y.index
    if (x is Cve && y is Cve) return x.index < y.index
    if (x is Cve && y is Cons) return true      // x < y

    if (x is Cons && y is Atom) return false    // y < x
    if (x is Cons && y is Cva) return false     // y < x
    if (x is Cons && y is Cve) return false     // y < x
    if (x is Cons && y is Cons) return less(x.head, y.head) || (x.head == y.head && less(x.tail, y.tail))

    throw IllegalArgumentException("$x ? $y")
}

data class SBind(val cva: CVar, val exp: CExp)

typealias Subst = List<SBind>

fun Subst.dom(): List<CVar> = map { it.cva }

operator fun CExp.div(s: Subst): CExp {
    return when (this) {
        is Atom -> this
        is Cons -> Cons(this.head / s, this.tail / s)
        is CVar -> s.find { it.cva == this }?.exp ?: this
        else -> throw IllegalArgumentException(s.toString())
    }
}

operator fun InEq.div(s: Subst) = InEq(left / s, right / s)

operator fun CBind.div(s: Subst) = CBind(variable, value / s)

operator fun Restriction.div(s: Subst) =
    when (this) {
        is Inconsistent -> Inconsistent
        is Restr -> Restr(this.inEqs.map { it / s }).clean()
    }


operator fun Subst.times(other: Subst): Subst {
    val dom = (dom() + other.dom()).distinct()
    return dom.map { SBind(it, (it / this) / other) }
}

operator fun CEnv.div(subst: Subst): CEnv = this.map { it / subst }

sealed class Contraction
data class S(val subst: Subst) : Contraction()
data class R(val restr: Restriction) : Contraction()

abstract class SR<T>(val t: T) {
    abstract operator fun div(subst: Subst): T
}

operator fun <T> Pair<SR<T>, Restriction>.div(contraction: Contraction): Pair<T, Restriction> {
    return when (contraction) {
        is S -> Pair(first / contraction.subst, second / contraction.subst)
        is R -> Pair(first.t, second + contraction.restr)
    }
}

@JvmName("div0")
operator fun Pair<CExp, Restriction>.div(contraction: Contraction): Pair<CExp, Restriction> {
    val sr = object : SR<CExp>(first) {
        override fun div(subst: Subst): CExp = first / subst
    }
    return Pair(sr, second) / contraction
}

@JvmName("div1")
operator fun Pair<List<CExp>, Restriction>.div(contraction: Contraction): Pair<List<CExp>, Restriction> {
    val sr = object : SR<List<CExp>>(first) {
        override fun div(subst: Subst): List<CExp> = first.map { it / subst }
    }
    return Pair(sr, second) / contraction
}

@JvmName("div2")
operator fun Pair<CBind, Restriction>.div(contraction: Contraction): Pair<CBind, Restriction> {
    val sr = object : SR<CBind>(first) {
        override fun div(subst: Subst): CBind = first / subst
    }
    return Pair(sr, second) / contraction
}

@JvmName("div3")
operator fun Pair<CEnv, Restriction>.div(contraction: Contraction): Pair<CEnv, Restriction> {
    val sr = object : SR<CEnv>(first) {
        override fun div(subst: Subst): CEnv = first / subst
    }
    return Pair(sr, second) / contraction
}

val idC = S(listOf())
val emptyC = R(Inconsistent)

typealias Class = Pair<List<CExp>, Restriction>

