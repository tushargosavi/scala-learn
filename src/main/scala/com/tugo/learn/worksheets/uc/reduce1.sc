import scala.annotation.tailrec

case class Environment(map: Map[String, AnyRef]) {
  def extend(a: String,b: AnyRef) : Environment = Environment(map + (a -> b))
}

sealed trait Expr {
  def reduce(e: Environment) : Expr

  def isReducible : Boolean

  def value: Double

  def inspect : String
}

object Expr {
  @tailrec
  def reduceAll(expr: Expr, env: Environment) : Expr = {
    if (expr.isReducible) {
      val next_e = expr.reduce(env)
      println("=> " + next_e.toString)
      reduceAll(next_e, env)
    } else {
      expr
    }
  }

  def reduceAll(expr: Expr) : Expr = {
    println("=> " + expr.toString)
    reduceAll(expr, null)
  }
}

case class Number(n: Double) extends Expr {
  override def reduce(e: Environment) = this

  override def isReducible = false

  override def value = n

  def inspect = n.toString
}

case class BooleanVal(b : Boolean) extends Expr {
  override def reduce(e: Environment) = this

  override def isReducible = false

  override def value = if (b) 0 else 1

  override def inspect = b.toString
}

case class Operator(c: Char, left: Expr, right: Expr) extends Expr {

  override def reduce(e: Environment) = {
    if (left.isReducible) Operator(this.c, this.left.reduce(e), right)
    else if (right.isReducible) Operator(this.c, this.left, this.right.reduce(e))
    else c match {
      case '+' => Number(left.value + right.value)
      case '/' => Number(left.value / right.value)
      case '*' => Number(left.value * right.value)
      case '-' => Number(left.value - right.value)
    }
  }

  override def isReducible = true

  // we can't call value on expression
  override def value = ???

  def inspect = left.inspect + c.toString + right.inspect
}

object Operator {
  def add(left : Expr, right: Expr) = Operator('+', left, right)

  def mul(left: Expr, right: Expr) = Operator('*', left, right)

  def div(left: Expr, right: Expr) = Operator('/', left, right)

  def sub(left: Expr, right: Expr) = Operator('-', left, right)

  //def lte(left: Expr, right: Expr) = LessThanEq(left, right)
}

case class LessThanEq(left: Expr, right: Expr) extends Expr {
  override def reduce(e: Environment) = {
    if (left.isReducible) {
      LessThanEq(left.reduce(e), right)
    } else if (right.isReducible) {
      LessThanEq(left, right.reduce(e))
    } else {
      (left, right) match {
        case (Number(x), Number(y)) => BooleanVal(x <= y)
        case _ => throw new RuntimeException("Not correct type")
      }
    }
  }

  override def isReducible = true

  override def value = ???

  override def inspect = left.inspect ++ "<=" ++ right.inspect
}

case class Variable(name: String) extends Expr {
  override def reduce(e: Environment) = e.map(name).asInstanceOf[Expr]

  override def isReducible = true

  override def value = ???

  override def inspect = name
}


trait Statement {
  // statement executes in the environment, and returns an Environment back, with
  // side effects handled by the Environment
  def execute(e: Environment) : (Statement, Environment)

  def isReducible : Boolean

}

object Statement {
  def executeAll(s: Statement, e: Environment): (Statement, Environment) = {
    if (s.isReducible) {
      val (s1, e1) = s.execute(e)
      println(s"(${s1}, e=${e1}")
      executeAll(s1, e1)
    } else {
      (s, e)
    }
  }

  def executeStatement(s: Statement): (Statement, Environment) = {
    val e = Environment(Map())
    executeAll(s, e)
  }
}

case class DoNothing() extends Statement {
  override def execute(e: Environment) = (this, e)

  override def isReducible: Boolean = false
}

case class AssignStatement(n: String, expr: Expr) extends Statement {

  override def isReducible: Boolean = true

  override def execute(e: Environment) = {
    if (expr.isReducible)
      (AssignStatement(n, expr.reduce(e)), e)
    else
      (DoNothing(), e.extend(n, expr))
  }
}

case class SequenceStatements(s1 : Statement, s2: Statement) extends Statement {
  override def execute(e: Environment) = {
    if (s1.isReducible) {
      val (s3, e3) = s1.execute(e)
      if (s3.isReducible) {
        (SequenceStatements(s3, s2), e)
      } else {
        (s2, e3)
      }
    } else {
      (s2, e)
    }
  }

  override def isReducible = true
}

case class IfElseCondition(
                            cond: Expr,
                            thenPart: Statement,
                            elsePart: Statement) extends Statement {
  override def execute(e: Environment) = {
    if (cond.isReducible) {
      (IfElseCondition(cond.reduce(e), thenPart, elsePart), e)
    } else {
      cond match {
        case BooleanVal(true) => (thenPart, e)
        case BooleanVal(false) => (elsePart, e)
      }
    }
  }

  override def isReducible = true
}

case class IfCondition(cond: Expr, thenPart: Statement) extends Statement {
  override def execute(e: Environment) = (IfElseCondition(cond, thenPart, DoNothing()), e)

  override def isReducible = true
}

case class WhileStatement(cond: Expr, body: Statement) extends Statement {
  override def execute(e: Environment) = {
    (IfElseCondition(cond, SequenceStatements(body, this), DoNothing()), e)
  }

  override def isReducible = true
}

val e = Operator.add(
  Operator.mul(Number(3),
    Operator.add(Number(2), Number(6))),
  Operator.add(Number(5), Variable("x")))

e.inspect

val env = Environment(Map("x" -> Number(4)))
Expr.reduceAll(e, env)

val s = AssignStatement("y", e)
Statement.executeAll(s, env)

val ifs = IfElseCondition(BooleanVal(true),
  AssignStatement("z", Operator.add(Number(4), Number(5))),
  DoNothing())

Statement.executeStatement(ifs)


val wsenv = Environment(Map("x" -> Number(0), "f" -> Number(1)))
val ws = WhileStatement(
  LessThanEq(Variable("x"), Number(5)),
  SequenceStatements(
    AssignStatement("x", Operator.add(Variable("x"), Number(1))),
    AssignStatement("f", Operator.mul(Variable("f"), Variable("x")))
  )
)
Statement.executeAll(ws, wsenv)