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
        val r = Restr(
            listOf(
                InEq(Cva(1), Cva(2)),
                InEq(Cva(2), Cva(1)),
                InEq(Atom("A"), Atom("B")),
                InEq(Atom("X"), Cva(6))
            )
        ).clean()


        assertTrue(r is Restr)
        assertEquals(
            listOf(
                InEq(Cva(1), Cva(2)),
                InEq(Atom("X"), Cva(6))
            ),
            (r as Restr).inEqs
        )
    }

    @Test
    fun testSubst() {
        // [ A.2 =/= A.1, A.2 =/= 'C, A.3 =/= 'A]
        val rs = Restr(
            listOf(
                InEq(Cva(2), Cva(1)),
                InEq(Cva(2), Atom("C")),
                InEq(Cva(3), Atom("A"))
            )
        )

        // [ A.1 :-> 'A, A.2 :-> A.3 ]
        val s1 = listOf(
            SBind(Cva(1), Atom("A")),
            SBind(Cva(2), Cva(3))
        )

        // [ A.1 :-> 'B, A.3 :-> 'B ]
        val s2 = listOf(
            SBind(Cva(1), Atom("B")),
            SBind(Cva(3), Atom("B"))
        )

        // [ A.2 :-> A.1, A.3 :-> 'A ]
        val s3 = listOf(
            SBind(Cva(2), Cva(1)),
            SBind(Cva(3), Atom("A"))
        )

        // rs / s1 == Restr([A.3 =/= 'A, A.3 =/= 'C])
        assertEquals(
            listOf(
                InEq(Cva(3), Atom("A")),
                InEq(Cva(3), Atom("C"))
            ),
            ((rs / s1) as Restr).inEqs
        )

        // rs / s2 == Restr([A.2 =/= 'B, A.2 =/= 'C])
        assertEquals(
            listOf(
                InEq(Cva(2), Atom("B")),
                InEq(Cva(2), Atom("C"))
            ),
            ((rs / s2) as Restr).inEqs
        )

        // rs / s3 == Inconsistent
        assertEquals(Inconsistent, rs / s3)
    }
}