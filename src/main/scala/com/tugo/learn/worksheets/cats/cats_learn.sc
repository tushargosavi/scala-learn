import cats.Monad
import cats.Semigroup._
import cats.implicits.catsSyntaxSemigroup
import cats.kernel.Semigroup

Semigroup[Int].combine(1,2)

Semigroup[Option[Int]].combine(Some(2), None)

Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6)

Map("foo" -> List(1, 2)).combine(Map("foo" -> List(3, 4), "bar" -> List(42)))

val aMap = Map("foo" -> Map("bar" -> 5))
val anotherMap = Map("foo" -> Map("bar" -> 6))
val combinedMap = Semigroup[Map[String, Map[String, Int]]].combine(aMap, anotherMap)

combinedMap.get("foo")

Semigroup[Option[Int]].combine(None, None)

import cats.implicits._
val addArity2 = (a: Int, b: Int) => a + b
val addArity3 = (a: Int, b: Int, c: Int) => a + b + c

val option2 = (Option(1), Option(2))
val option3 = (option2._1, option2._2, Option.empty[Int])
option2 mapN addArity2

option3 mapN addArity3

option3.tupled

option2 apWith Some(addArity2)
option3 apWith Some(addArity3)

Monad[Option].ifM(Option(true))(Option("truthy"), Option("falsy"))
Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4))

import cats.implicits._
case class OptionT[F[_], A](value: F[Option[A]])