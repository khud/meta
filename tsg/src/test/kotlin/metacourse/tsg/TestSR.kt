package metacourse.tsg

import org.junit.Assert.*
import org.junit.Test

class TestSR {
    @Test
    fun testInEq() {
        val inEq1 = InEq(Cva(1), Cva(3))
        val inEq2 = InEq(Cva(1), Cva(3))
        val inEq3 = InEq(Cva(3), Cva(1))
        val inEq4 = InEq(Cva(3), Cva(2))

        val inEq5 = InEq(Atom("A"), Cva(1))
        val inEq6 = InEq(Cva(1), Atom("A"))
        assertEquals(inEq1, inEq2)
        assertEquals(inEq1, inEq3)
        assertNotEquals(inEq1, inEq4)
        assertEquals(inEq5, inEq6)
    }

    @Test
    fun testClearRestr() {
        val r = cleanRestr(
            Restr(
                listOf(
                    InEq(Cva(1), Cva(2)),
                    InEq(Cva(2), Cva(1)),
                    InEq(Atom("A"), Atom("B")),
                    InEq(Atom("X"), Cva(6))
                )
            )
        )

        assertTrue(r is Restr)
        assertEquals(
            listOf(
                InEq(Cva(1), Cva(2)),
                InEq(Atom("X"), Cva(6))
            ),
            (r as Restr).inEqs
        )
    }
}