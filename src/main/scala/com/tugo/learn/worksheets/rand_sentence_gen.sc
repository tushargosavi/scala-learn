val noel = "Noel"
val cat = "The cat"
val dog = "The dog"

val subjects = List(noel, cat, dog)
val verbs = List("wrote","chased","slept on")
val objects = List("the book","the ball","the bed")

val s_to_v_map = Map(
  noel -> verbs,
  cat -> Seq("mewed at", "slept on", "chased"),
  dog -> Seq("barked at", "slept on", "chased")
)

val v_to_o_map = Map(
  "mewed at" -> Seq(noel, "the door", "the food cupboard"),
  "barked at" -> Seq("the postman", "the car", "the cat"),
  "wrote" -> Seq("the book", "the letter", "the code"),
  "chased" -> Seq("the ball", "the dog", "the cat"),
  "slept on" -> Seq("the bed", "the mat", "the train")
)

(for {
  s <- subjects
  v <- s_to_v_map.get(s).get
  o <- v_to_o_map.get(v).get
} yield Seq(s, v, o).mkString(" "))

final case class Distribution[A](val events : List[(A, Double)]) {

  def normalize() : Distribution[A] = {
    val total = events.map({ case (a,b) => b}).sum
    val evts = events.map({ case (a,b) => (a,b/total)})
    Distribution(evts)
  }

  def map[B](f : A => B) : Distribution[B] = {
    Distribution(events.map({ case (a,b) => (f(a), b) }))
  }

  def flatMap[B](f : A => Distribution[B]) : Distribution[B] = {
    Distribution(
      events.flatMap { case (a,p1) =>
        f(a).events.map { case (b,p2)  => b -> (p1 * p2)}}
    ).compact().normalize()
  }

  def compact(): Distribution[A] = {
    val terms = events.map { case (a,b) => a} . distinct
    def p(e: A) = events.
      filter { case (a,_) => a == e }.
      map { case (_,p) => p }.
      sum

    Distribution(terms.map(e => (e, p(e))))
  }
}

object Distribution {
  def uniform[A](events : List[A]) = {
    val p = 1.0 / events.length
    Distribution(events.map((_, p)))
  }

  def discrete[A](events: List[(A,Double)]): Distribution[A] = Distribution(events).compact.normalize

}

val d = Distribution.uniform[Int](List(1, 2, 3, 4,5, 6))
d.map(_ * 2)

sealed trait Coin
case object Head extends Coin
case object Tail extends Coin

val flip = Distribution.uniform(List(Head, Tail))

for {
  f1 <- flip
  f2 <- flip
  f3 <- flip
} yield (f1, f2,f3)


// Problem 1
sealed trait FoodState
case object Delicius extends FoodState
case object Raw extends FoodState

val cook = Distribution.discrete(List(Delicius -> 0.3, Raw -> 0.7))

sealed trait CatBehavior
case object Harass extends CatBehavior
case object Sleep extends CatBehavior

def catBehavior(f: FoodState) : Distribution[CatBehavior] = {
  f match {
    case Delicius => Distribution.discrete(List(Harass -> 0.8, Sleep -> 0.2))
    case Raw => Distribution.discrete(List(Harass -> 0.4, Sleep -> 0.6))
  }
}

for {
  c <- cook
  s <- catBehavior(c)
} yield (c,s)
