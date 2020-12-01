package metacourse.flowchart

import org.junit.Assert.*
import org.junit.Test

class TestInt {
    @Test
    fun testSimpleProg() {
        val prog = Program(
            listOf(Var("x")),
            listOf(
                BasicBlock("init", listOf(
                    Assignment(Var("y"), Var("x"))
                ), Return(Var("x")))
            )
        )
        val result = int(prog, listOf(Constant("test")), listOf())
        assertEquals(Constant("test"), result)
    }
}