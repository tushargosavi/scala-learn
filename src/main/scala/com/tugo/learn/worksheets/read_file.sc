import scala.io.Source
import scalaj.http._
val url = "https://en.wikipedia.org/w/api.php"
var resp = Http(url).params(List(
  ("action", "query"),
  ("titles", "Albert Einstein"),
  ("prop", "links"),
  ("format", "json"))
).asString

println(resp)