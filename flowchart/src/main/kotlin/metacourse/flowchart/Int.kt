package metacourse.flowchart

class Env(read: Read, args: List<Expr>, buildIns: List<BuiltIn>) {
    private val env = read.map { it.name }.zip(args).toMap().toMutableMap()
    val buildIns = buildIns.map { Pair(it.name, it.body) }.toMap()

    operator fun get(variable: Var): Expr =
        env.getOrDefault(variable.name, emptyList())

    operator fun set(variable: Var, expr: Expr) {
        env[variable.name] = expr
    }
}

data class BuiltIn(val name: String, val body: (List<Expr>) -> Expr)

fun emptyList(): Constant = Constant("()")

fun Program.getBlock(label: Label): BasicBlock =
    blocks.find { it.label == label } ?: throw IllegalArgumentException("No block named $label")

fun int(prog: Program, args: List<Expr>, builtIns: List<BuiltIn>): Expr {
    val env = Env(prog.read, args, builtIns)
    return run(prog.blocks[0], prog, env)
}

fun run(block: BasicBlock, prog: Program, env: Env): Expr {
    block.assignments.forEach {
        env[it.variable] = eval(it.expr, env)
    }

    return when (val jump = block.jump) {
        is Goto -> run(prog.getBlock(jump.label), prog, env)
        is If -> {
            val cond = eval(jump.expr, env).asBoolean()
            val label = if (cond) jump.goto else jump.otherwise
            run(prog.getBlock(label), prog, env)
        }
        is Return -> eval(jump.expr, env)
    }
}

fun Expr.asBoolean(): Boolean = this == emptyList()

fun eval(expr: Expr, env: Env): Expr {
    return when (expr) {
        is Constant -> expr
        is Var -> env[expr]
        is Op -> {
            val builtIn = env.buildIns[expr.func] ?: throw IllegalArgumentException("No such built-in ${expr.func}")
            val args = expr.args.map { eval(it, env) }
            builtIn(args)
        }
    }
}