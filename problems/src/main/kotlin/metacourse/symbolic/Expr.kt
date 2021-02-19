package metacourse.symbolic

sealed class Expr
data class Value(val value: Double): Expr()
data class Variable(val name: String): Expr()
data class Plus(val x: Expr, val y: Expr): Expr()
data class Minus(val x: Expr, val y: Expr): Expr()
data class Multiply(val x: Expr, val y: Expr): Expr()
data class Divide(val x: Expr, val y: Expr): Expr()
data class Sin(val expr: Expr): Expr()

fun eval(expr: Expr, env: Map<Variable, Value>): Expr {
    TODO()
}

fun diff(expr: Expr, variable: Variable): Expr {
    TODO()
}
