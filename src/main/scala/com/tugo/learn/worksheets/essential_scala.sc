class Calculator {
  def readInt(str: String): Option[Int] =
    if(str matches "-?\\d+") Some(str.toInt) else None

  def divide(num : Int, den : Int) =
    if (den == 0) None else Some(num / den)

  def calculate(n1: Int, n2: Int, op: String): Option[Int] = {
    op match {
      case "+" => Some(n1+n2)
      case "-" => Some(n1-n2)
      case "*" => Some(n1 * n2)
      case "/" => divide(n1, n2)
      case _ => None
    }
  }

  def calculate(n1 : String, n2 : String, op: String) : Option[Int] = {
    for {
      a <- readInt(n1)
      b <- readInt(n2)
      c <- calculate(a, b, op)
    } yield c
  }
}

val c = new Calculator()
c.calculate("123", "31", "/") match {
  case None => println("Not valid input")
  case Some(a) => println(s"Result it $a")
}

(1 to 10).foreach(println)