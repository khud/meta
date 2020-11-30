package metacourse.tsg

import org.junit.Assert.*
import org.junit.Test

class TestUnify {
    @Test
    fun testNotTheSameSize() {
        val res = unify(
            listOf(Cva(1), Cva(2)),
            listOf(Atom("A"))
        )
        assertFalse(res.first)
    }

    @Test
    fun testSimpleCva() {
        val res = unify(
            listOf(Cva(1), Cva(2)),
            listOf(Atom("A"), Atom("B"))
        )
        assertTrue(res.first)
        assertTrue(res.second.contains(SBind(Cva(1), Atom("A"))))
        assertTrue(res.second.contains(SBind(Cva(2), Atom("B"))))
    }
}