implicit val minOrdering = Ordering.fromLessThan[Int](_ < _)
implicit val maxOrdering = Ordering.fromLessThan[Int](_ > _)

val absOrdering = Ordering.fromLessThan[Int](Math.abs(_) < Math.abs(_))
List(-4,-1, 0,2,3).sorted(absOrdering)

final case class Rational(num : Int, denom : Int)

object Rational {
  def gcd(a : Int,b : Int): Int = if (b == 0) a else gcd(b, a % b)

  def of(num: Int, denom: Int): Rational = {
    val g = gcd(num, denom)
    Rational(num / g, denom / g)
  }
}

implicit  def ratOrdering = Ordering.fromLessThan[Rational]((a,b) =>
  a.num * b.denom < b.num * a.denom)

List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
val exp = List(Rational(1, 3), Rational(1, 2), Rational(3, 4))

Rational.of(10, 20)

trait HtmlWriter[A] {
  def write(a: A): String
}

object HtmlWriter {
  def apply[A](implicit writer: HtmlWriter[A]) : HtmlWriter[A] = {
    writer
  }
}

def htmlFy[A](a : A)(implicit writer: HtmlWriter[A]): Unit = {
  println(writer.write(a))
}
case class Person(name: String, email : String)

object Person {
  implicit object PersonHtmlWriter extends HtmlWriter[Person] {
    override def write(a: Person): String = s"<span>name ${a.name}, email ${a.email}</span>"
  }
}

val p = Person("tushargosvi@gmail.com", "Tushar R. Gosavi")
htmlFy(p)

HtmlWriter[Person].write(p)