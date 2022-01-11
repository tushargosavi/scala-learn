type Graph = List[(String, String)]

val g = List(
  ("m", "n"),
  ("m", "o"),
  ("n", "p"),
  ("p", "o")
)

def succ(n : String, g : Graph) =
  g filter (_._1 == n) map (_._2)

succ("m", g)

def depthFirst(s: String, g: Graph) : List[String] = {
  def depthFirstH(nodes: List[String], visited: List[String]) : List[String]  = {
    nodes match {
      case Nil => visited
      case x :: xs if visited.contains(x) => depthFirstH(xs, visited)
      case x :: xs => depthFirstH(succ(x, g) ++ xs, x :: visited)
    }
  }
  val res = depthFirstH(List(s), List())
  res.reverse
}

depthFirst("m", g)

/*
  This functions does depth first traversal but avoid expensive "++"
  operation, it does so by recursively calling dps1 on each successive
  element
 */
def dfs(s: String, g: Graph) : List[String] = {
  def dfs1(nodes: List[String], visited: List[String]): List[String] =
    nodes match {
      case Nil => visited
      case x :: xs => dfs1(xs,
        if (visited.contains(x)) visited
        else dfs1(succ(x, g), x :: visited)
      )
    }
  val res = dfs1(List(s), List())
  res.reverse
}

dfs("m", g)


def topologySort(g : Graph) : List[String] = {
  def sort(nodes: List[String], visited: List[String]) : List[String] = {
    nodes match {
      case Nil => visited
      case x :: xs => sort(xs,
        if (visited.contains(x)) visited
        else x :: sort(xs, visited))
    }
  }
  sort(g map (_._1), List())
}

topologySort(g)