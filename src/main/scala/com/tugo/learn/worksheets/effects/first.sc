import cats.effect.IO

def fib(n: Int, a: Int, b: Int) : Int = {
  if (n == 0) a else fib(n-1, b, a+b)
}

fib(10, 1, 1)