import cats.{Apply, Functor, Monoid}
import cats.implicits.toFoldableOps

Monoid[String].combineAll(List("a", "b", "c"))
Monoid[String].combineAll(List())

val l = List(1, 2, 3, 4, 5)
l.foldMap(identity)
l.foldMap(i => i.toString)

val source = List("Cats", "is", "awesome")
val product = Functor[List].fproduct(source)(_.length).toMap

val listOpt = Functor[List] compose Functor[Option]
listOpt.map(List(Some(1), None, Some(3)))(_ + 1)

val plusOne = (x: Int) => x + 1
val double = (x: Int) => x * 2
val listOpt1 = Apply[List] compose Apply[Option]
listOpt1.ap(List(Some(plusOne), None, Some(double)))(List(Some(1), None, Some(3)))