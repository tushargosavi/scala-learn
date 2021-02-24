package com.tugo.learn

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Hello {
  print("Hello")
}

object Hello {
  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val f1 = Future { println("starting f1"); Thread.sleep(5000); println("done f1"); 20 }
    val f2 = Future { println("starting f2"); Thread.sleep(3000); println("done f2"); 30 }

    f1.zip(f2).foreach(t  => println(s"${t._1} and ${t._2}"))

    val f3 = f1.flatMap( x => {
      def fib(x: Int) : Int  = x match {
        case 0 => 1
        case 1 => 1
        case n => fib (x - 1) + fib (x - 2)
      }
      Future { fib(x) }
    })

    val numProcessors = Runtime.getRuntime.availableProcessors()
    println(s"Num processors in system ${numProcessors}")
    f3.foreach(println)

    val f6 = Future.firstCompletedOf(List(f1, f2))

    val res = Await.result(f6, Duration.Inf)
    println(s"The result of f6 is ${res}")

    // don't let main to finish before features
    Thread.sleep(10000)
    println("Done main");
  }
}
