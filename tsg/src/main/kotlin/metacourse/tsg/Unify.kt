package metacourse.tsg

typealias UnifRes = Pair<Boolean, Subst>

fun fail(): UnifRes = UnifRes(false, listOf())

data class Clash(val lhs: CExp, val rhs: CExp)

fun <T> List<T>.decap(): Pair<T, List<T>> = Pair(get(0), subList(1, size))

fun Cons.decap() = Pair(head, tail)

fun moveCl(rchs: List<Clash>, chs: List<Clash>): UnifRes {
    val (ch, chs_) = chs.decap()
    val (cvar, cexp) = ch
    val r = rchs.filter { it.lhs == cvar }.map { it.rhs }
    if (r.isEmpty()) return unify_(listOf(ch) + rchs, chs_)
    if (r.size == 1 && r[0] == cexp) unify_(rchs, chs_)
    return fail()
}

fun unify_(rchs: List<Clash>, chs: List<Clash>): UnifRes {
    if (chs.isEmpty()) return UnifRes(true, rchs.map { SBind(it.lhs as CVar, it.rhs) })

    val (ch, chs_) = chs.decap()

    if (ch.lhs is Atom && ch.rhs is Atom && ch.lhs.name == ch.rhs.name) return unify_(rchs, chs_)
    if (ch.lhs is Atom) return fail()
    if (ch.lhs is Cva && ch.rhs is Atom) return moveCl(rchs, chs)
    if (ch.lhs is Cva && ch.rhs is Cva) return moveCl(rchs, chs)
    if (ch.lhs is Cva) return fail()
    if (ch.lhs is Cve) return moveCl(rchs, chs)
    if (ch.lhs is Cons && ch.rhs is Cons) {
        val (a1, b1) = ch.lhs.decap()
        val (a2, b2) = ch.lhs.decap()
        val p = listOf(Clash(a1, a2), Clash(b1, b2))
        return unify_(rchs, p + chs_)
    }

    return fail()
}

fun unify(ces1: List<CExp>, ces2: List<CExp>): UnifRes {
    return if (ces1.size != ces2.size) fail()
    else unify_(listOf(), ces1.zip(ces2, transform = { ce1, ce2 -> Clash(ce1, ce2) }))
}