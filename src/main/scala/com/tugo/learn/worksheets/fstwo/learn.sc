import fs2.Stream
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.Chunk

val sc1 = Stream.chunk(Chunk.array(Array(1.0, 2.0, 3.0)))
val eff = Stream.eval(IO { println("BEGIN RUN!!"); 1+1 })

eff.compile.toVector.unsafeRunSync()

sc1.map(_ + 1).toList

val appendEx1 = Stream(1,2,3) ++ Stream.emit(42)
appendEx1.toList

appendEx1.flatMap(i => Stream.emits(List(i, i))).toList

Stream.emits(List(1,3,4,5,6,7)).toVector

val err = Stream.raiseError[IO](new Exception("oh noes!"))
val count = new java.util.concurrent.atomic.AtomicLong(0)
// count: java.util.concurrent.atomic.AtomicLong = 0
val acquire = IO { println("incremented: " + count.incrementAndGet); () }
// acquire: IO[Unit] = IO(...)
val release = IO { println("decremented: " + count.decrementAndGet); () }

//Stream.bracket(acquire)(_ => release).flatMap(_ => Stream(1,2,3) ++ err).compile.drain.unsafeRunSync()

count.get()

(Stream(1,2) ++ Stream(3).map(_ => throw new Exception("nooo!!"))).attempt.toList
