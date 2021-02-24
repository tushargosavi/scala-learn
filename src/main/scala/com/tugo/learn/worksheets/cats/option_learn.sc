import cats.Apply

Option(10).map(_ + 1)

Option(10).flatMap(a => Some(a + 1))

def ap[A, B](f: Option[A => B])(fa: Option[A]): Option[B] =
  fa.flatMap(a => f.map(ff => ff(a)))

val intToString: Int => String = _.toString
val double: Int => Int = _ * 2
val addTwo: Int => Int = _ + 2

Apply[Option].ap(Some((a:Int) => a.toString))(Some(1))
Apply[Option].ap(None)(Some(1))
Apply[Option].ap(None)(None)
Apply[Option].ap(Some(double))(Some(1))

val addArity2 = (a: Int, b: Int) => a + b
Apply[Option].ap2(Some(addArity2))(Some(1), Some(2))

val addArity3 = (a: Int, b: Int, c: Int) => a + b + c
Apply[Option].ap3(Some(addArity3))(Some(1), Some(2), Some(3))

Apply[Option].tuple2(Some(1), Some(2))

Option.empty[Int]