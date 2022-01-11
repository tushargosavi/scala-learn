import cats.Monoid
import cats.implicits._
import cats.syntax.monoid._

def foldMap[A, B : Monoid](a : Vector[A])(fn : A => B)(implicit bm: Monoid[B]) : B = {
  (a map fn).
    foldLeft(bm.empty){(a,b) => bm.combine(a,b)}
}


def foldMap1[A, B : Monoid](a : Vector[A])(fn : A => B): B = {
  a.map(fn).foldLeft(Monoid[B].empty)(_ |+| _)
}