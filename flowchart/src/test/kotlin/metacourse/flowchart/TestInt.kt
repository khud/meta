package metacourse.flowchart

import org.junit.Assert.*
import org.junit.Test

class TestInt {
    private fun simpleAssignment(expr: Expr) =
        Program(
            listOf(Var("x")),
            listOf(
                BasicBlock(
                    "init", listOf(
                        Assignment(Var("y"), expr)
                    ), Return(Var("y"))
                )
            )
        )

    private val id = simpleAssignment(Var("x"))

    @Test
    fun testSimpleProg() {
        val result = int(id, listOf(Constant("test")), listOf())
        assertEquals(Constant("test"), result)
    }

    @Test
    fun testCons() {
        val myList = Op("cons", listOf(Constant("A"), Constant("()")))
        val result = int(id, listOf(myList), listOf(BuiltIns.cons))
        assertEquals(myList, result)
    }

    @Test
    fun testHead() {
        val myList = Op("cons", listOf(Constant("A"), Constant("()")))
        val head = Op("hd", listOf(Var("x")))
        val result = int(simpleAssignment(head), listOf(myList),
            listOf(BuiltIns.cons, BuiltIns.hd))
        assertEquals(Constant("A"), result)
    }

    @Test
    fun testTail() {
        val myList = Op("cons", listOf(Constant("A"), Constant("()")))
        val head = Op("tl", listOf(Var("x")))
        val result = int(simpleAssignment(head), listOf(myList),
            listOf(BuiltIns.cons, BuiltIns.tl))
        assertEquals(Constant("()"), result)
    }
}