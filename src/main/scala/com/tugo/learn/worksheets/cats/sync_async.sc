import scala.concurrent.Future
import cats.syntax._
import cats.implicits._
import cats.{Applicative, Id, Monad, Traverse}

import scala.concurrent.ExecutionContext.Implicits.global

trait UptimeClient[F[_]] {
  def getUptime(host : String) : F[Int]
}

class UptimeService[F[_]: Applicative](client: UptimeClient[F]) {
  def getTotalUptime(hosts: List[String]) =
    hosts.traverse(client.getUptime).map(_.sum)
}

class TestUptimeClient(hosts : Map[String, Int]) extends UptimeClient[Id] {
  def getUptime(host: String): Int = hosts.getOrElse(host, 0)
}

def totalUpTime = {
  val hosts = Map("host1" -> 10, "host2" -> 6)
  val client = new TestUptimeClient(hosts)
  val service = new UptimeService(client)
  val actual = service.getTotalUptime(hosts.keys.toList)
  val expected = hosts.values.sum
  println(actual)
  println(expected)
}

totalUpTime