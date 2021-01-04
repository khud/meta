package metacourse.tsg

import java.lang.RuntimeException

typealias Conf = Pair<State, Restriction>

typealias Branch = Pair<Contraction, Tree>

typealias Split = Pair<Contraction, Contraction>

sealed class Tree
class Leaf(val conf: Conf): Tree()
class Node(val conf: Conf, val branches: List<Branch>): Tree()

fun ptr(prog: ProgR, cl: Class): Tree {
    val (ces, r) = cl
    val fDef = prog[0]
    val ce = mkEnv(fDef.params, ces)
    val c: Conf = State(Call(fDef.name, fDef.params), ce) to r
    val i = freeIndex(0, cl)
    return evalT(c, prog, i)
}

fun evalT(c: Conf, p: ProgR, i: FreeIndex): Tree {
    val (state, r) = c

    when (val term = state.term) {
        is Call -> {
            val f = getDef(term.name, p)!!
            val ce = mkEnv(f.params, term.args / state.env)
            val c1 = State(f.body, ce) to r
            return Node(c1, listOf(idC to evalT(c1, p, i)))
        }

        is Alt -> {
            val (split, uce1, uce2, i_) = cond(term.cond, state.env, i)
            val (cnt1, cnt2) = split
            val (ce1, r1) = (state.env to r) / cnt1
            val c1 = State(term.then, ce1 + uce1) to r1
            val (ce2, r2) = (state.env to r) / cnt2
            val c2 = State(term.otherwise, ce2 + uce2) to r2
            return Node(c, listOf(cnt1 to evalT(c1, p, i_), cnt2 to evalT(c2, p, i_)))
        }

        else -> return Leaf(c)
    }
}

data class CCondRes(val split: Split, val ce1: CEnv, val ce2: CEnv, val i: FreeIndex)

fun cond(cond: Cond, ce: CEnv, i: FreeIndex): CCondRes {
    when (cond) {
        is IsEqa -> {
            val x = cond.x / ce
            val y = cond.y / ce

            return if (x == y) {
                CCondRes(idC to emptyC, listOf(), listOf(), i)
            } else if (x is Atom && y is Atom) {
                CCondRes(emptyC to idC, listOf(), listOf(), i)
            } else if (x is Cva) {
                CCondRes(splitA(x, y), listOf(), listOf(), i)
            } else if (y is Cva) {
                CCondRes(splitA(y, x), listOf(), listOf(), i)
            } else throw RuntimeException()
        }
        is IsCons -> {
            return when (val x = cond.exp / ce) {
                is Cons -> CCondRes(idC to emptyC, listOf(Bind(cond.head, x.head), Bind(cond.tail, x.tail)), listOf(), i)
                is Atom -> CCondRes(emptyC to idC, listOf(), listOf(Bind(cond.atom, x)), i)
                is Cva -> CCondRes(emptyC to idC, listOf(), listOf(Bind(cond.atom, x)), i)
                is Cve -> {
                    val (split, i_) = splitE(x, i)
                    val (ch, ct) = ((split.first as S).subst[0].exp as Cons)
                    val ca = (split.second as S).subst[0].exp
                    CCondRes(split, listOf(Bind(cond.head, ch), Bind(cond.tail, ct)), listOf(Bind(cond.atom, ca)), i_)
                }
                else -> throw RuntimeException()
            }
        }
    }
}

fun mkCAVar(i: FreeIndex): Pair<Cva, FreeIndex> = Cva(i) to i + 1

fun mkCEVar(i: FreeIndex): Pair<Cve, FreeIndex> = Cve(i) to i + 1

fun splitA(cva: Cva, exp: CExp): Split =
    S(listOf(SBind(cva, exp))) to R(Restr(listOf(InEq(cva, exp))))

fun splitE(cve: Cve, i: FreeIndex): Pair<Split, FreeIndex> {
    val (cvh, i1) = mkCEVar(i)
    val (cvt, i2) = mkCEVar(i1)
    val (cva, i_) = mkCAVar(i2)
    return (S(listOf(SBind(cve, Cons(cvh, cvt)))) to S(listOf(SBind(cve, cva)))) to i_
}

typealias FreeIndex = Int

fun freeIndex(i: FreeIndex, cl: Class): FreeIndex {
    val (ces, _) = cl
    val index = ces.filterIsInstance<CVar>().maxOfOrNull { it.index } ?: (i - 1)
    return index + 1
}