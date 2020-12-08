package metacourse.flowchart

class ProgramBuilder {
    val blocks = mutableListOf<BasicBlock>()
}

fun program(vararg vars: Var, body: ProgramBuilder.() -> Unit): Program {
    val builder = ProgramBuilder()
    builder.body()
    return Program(vars.toList(), builder.blocks)
}

class BasicBlockBuilder {
    val assignments = mutableListOf<Assignment>()
    lateinit var jump: Jump
}

fun ProgramBuilder.block(label: Label, body: BasicBlockBuilder.() -> Unit) {
    val builder = BasicBlockBuilder()
    builder.body()
    blocks.add(BasicBlock(label, builder.assignments, builder.jump))
}

fun BasicBlockBuilder.assign(variable: Var, expression: Expr) {
    assignments.add(Assignment(variable, expression))
}

fun BasicBlockBuilder.ret(expr: Expr) {
    jump = Return(expr)
}

fun BasicBlockBuilder.goto(label: Label) {
    jump = Goto(label)
}

fun BasicBlockBuilder.cond(expr: Expr, goto: Label, otherwise: Label) {
    jump = If(expr, goto, otherwise)
}