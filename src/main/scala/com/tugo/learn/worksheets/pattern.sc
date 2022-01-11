
"tushar@gamil.com".split("@")

object Email {
  def unapply(str: String) : Option[(String, String)] = {
    val parts = str.split("@")
    if (parts.length == 2) Some((parts(0), parts(1))) else None
  }
}

object OtherMath {
  def unapply(str : String) : Option[String] = {
    if (str != null) Some(str.toUpperCase) else None
  }
}

"tushar@gmail.com" match {
  //case Email(n, d) => println(s"name $n and domain $d")
  case OtherMath(h) => println(s"converted $h")
  case _ => println("Does not match")
}