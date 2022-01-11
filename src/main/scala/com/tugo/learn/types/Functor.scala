package com.tugo.learn.types

import simulacrum.typeclass

@typeclass trait Functor[F[_]] {
  def map[A,B](fa: F[A])(f: A => B) : F[B]
}

object Functor {
  implicit val listFunctor = new Functor[List] {
    // reuse method from list function
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  implicit val optionFunctor = new Functor[Option] {
    // reuse map function from Option
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }
}