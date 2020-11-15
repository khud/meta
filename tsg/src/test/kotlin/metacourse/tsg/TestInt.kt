package metacourse.tsg

import org.junit.Assert.*
import org.junit.Test

class TestInt {
    @Test
    fun testSimple() {
        fun simple(): ProgR {
            return listOf(
                DEFINE("test") {
                    Ret(Atom("OK"))
                }
            )
        }

        assertEquals(Atom("OK"), int(simple(), listOf()))
    }

    @Test
    fun testHead() {
        fun head(): ProgR {
            val str = Pve("str")
            val h = Pve("h")
            val t = Pve("t")
            val a = Pva("a")
            return listOf(
                DEFINE("head", str) {
                    IF(MATCH(str, h, t, a)) {
                        Ret(h)
                    } ELSE {
                        Ret(Atom("ERROR"))
                    }
                }
            )
        }

        assertEquals(Atom("ERROR"), int(head(), listOf(Atom("A"))))
        assertEquals(Atom("A"), int(head(), listOf(Cons(Atom("A"), Atom("B")))))
    }
}