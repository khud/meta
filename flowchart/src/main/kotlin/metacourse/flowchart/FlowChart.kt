package metacourse.flowchart

/*
The Flow Chart Language from the book
Partial Evaluation and Automatic Program Generation, p. 70

<Program>           ::= read <Var>, ..., <Var>; <BasicBlock>+
<BasicBlock>        ::= <Label>: <Assignment>* <Jump>
<Assignment>        ::= <Var> := <Expr>
<Jump>              ::= goto <Label>;
                        if <Expr> goto <Label> else <Label>;
                        return <Expr>;
<Expr>              ::= <Constant>
                    |   <Var>
                    |   <Op> <Expr> ... <Expr>
<Constant>          ::= quote <Val>
<Op>                ::= hd | tl | cons | ...
                    plus any others needed for writing
                    interpreters or program specializers
<Label>             :: any identifier or number
 */
data class Program(val read: Read, val blocks: List<BasicBlock>)

typealias Read = List<Var>

data class BasicBlock(val label: Label, val assignments: List<Assignment>, val jump: Jump)

typealias Label = String

data class Assignment(val variable: Var, val expr: Expr)

sealed class Expr

data class Var(val name: String): Expr()

data class Constant(val value: Val): Expr()

data class Op(val func: Func, val args: List<Expr>): Expr()

typealias Val = String

typealias Func = (List<Expr>) -> Expr

sealed class Jump

data class Goto(val label: Label): Jump()

data class If(val expr: Expr, val goto: Label, val otherwise: Label)

data class Return(val expr: Expr)




