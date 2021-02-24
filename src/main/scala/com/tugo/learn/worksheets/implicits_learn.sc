
case class MyClass() {
  def update(x : Int): Unit = {
    println(s"Update called with value ${x}")
  }
}

object Test1 {
  implicit class MyClassExtension(val c : MyClass) {
    def update2(a: Int) = {
      println("Inside implicit call")
      c.update(a)
      println("Done implicit call")
    }
  }
}

import Test1._
import cats.Functor
val c = MyClass()
Test1.MyClassExtension(c).update2(10)
c.update2(10)

