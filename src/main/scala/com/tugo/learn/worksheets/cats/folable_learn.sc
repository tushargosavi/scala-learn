import cats.Foldable

Foldable[List].find(List(1, 2, 3))(_ > 2)

Foldable[List].foldK(List(List(1, 2), List(3, 4, 5)))

Foldable[List].foldK(List(None, Option("two"), Option("three")))

import cats.implicits._

def parseInt(s: String): Option[Int] =
  Either.catchOnly[NumberFormatException](s.toInt).toOption

Foldable[List].traverse_(List("1", "2", "3"))(parseInt)

Foldable[List].traverse_(List("a", "b", "c"))(parseInt)

val FoldableListOption = Foldable[List].compose[Option]
FoldableListOption.fold(List(Option(1), Option(2), Option(3), Option(4)))

FoldableListOption.fold(List(Option("1"), Option("2"), None, Option("3")))