package metacourse.tsg

interface CVar

data class Cve(val index: Int) : CExp, CVar {
    override fun toString(): String = "E.$index"
}

data class Cva(val index: Int) : CExp, CVar {
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

fun cleanRestr(restr: Restriction): Restriction =
    when (restr) {
        is Inconsistent -> Inconsistent
        is Restr ->
            if (restr.inEqs.any(::isContradiction))
                Inconsistent
            else
                Restr(restr.inEqs.filterNot(::isTautology).distinct())
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
