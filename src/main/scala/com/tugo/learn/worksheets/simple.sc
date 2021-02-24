
def sqrt(x : Double) : Double = {

  def sqrtIter(guess: Double) : Double = {
    if (goodEnough(guess)) guess
    else sqrtIter(improveGuess(guess))
  }

  def goodEnough(guess : Double) = Math.abs(guess * guess - x) < 0.001

  def improveGuess(guess: Double): Double = (guess + x / guess) / 2

  sqrtIter(1)
}

sqrt(2)

val builder = new StringBuilder

val x = { println("x"); 1 }
lazy val y = { println("y"); 2 }
def z = { println("z"); 3 }

z + y + x + z + y + x

builder.result()

var rec = 0
def llRange(lo: Int, hi: Int): LazyList[Int] = {
  rec = rec + 1
  if (lo >= hi) LazyList.empty
  else LazyList.cons(lo, llRange(lo + 1, hi))
}
llRange(1, 10).take(3).toList

println(rec)

class Rational(x: Int, y: Int) {

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
  private val g = gcd(x, y)

  lazy val numer: Int = x / g
  lazy val denom: Int = y / g
}

val compareRationals: (Rational, Rational) => Int =
  (a,b) => Ordering.Int.compare((a.numer * b.denom), (b.numer * a.denom))


implicit val rationalOrder: Ordering[Rational] =
  new Ordering[Rational] {
    def compare(x: Rational, y: Rational): Int = compareRationals(x, y)
  }

val half = new Rational(1, 2)
val third = new Rational(1, 3)
val fourth = new Rational(1, 4)
val rationals = List(third, half, fourth)