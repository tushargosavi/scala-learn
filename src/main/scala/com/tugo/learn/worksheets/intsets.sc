abstract class IntSet {
  def contains(a: Int) : Boolean
  def inc(a : Int) : IntSet
}
case class NonEmpty(x : Int, left : IntSet, right : IntSet) extends IntSet {
  override def contains(a: Int): Boolean =
    if (x == a) true
    else if (x < a) right.contains(a) else left.contains(a)

  override def inc(a: Int): IntSet =
    if (a < x) NonEmpty(x, left.inc(a), right) else NonEmpty(x, left, right.inc(a))

}

object EmptySet extends IntSet {
  override def contains(a: Int) = false
  override def inc(a : Int) = NonEmpty(a, EmptySet, EmptySet)

  override def toString: String = "EmptySet"
}

val a = EmptySet
val n = a.inc(10).inc(20).inc(50).inc(30).inc(40)
println(n)