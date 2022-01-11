package com.tugo.learn.types

trait Applicative[F[_]] extends Functor[F] {
  // pure take a value and wraps it inside a context
  def pure[A](a: A): F[A]

  // apply takes a value from a context, and a function from an context
  // and returns resulting value inside the same context
  def apply[A,B](fa:F[A])(ff: F[A => B]): F[B]

  // The map from functor can be implemented using apply and pure method
  // hence a applicative is a functor
  def map[A,B](fa:F[A])(f: A=> B):F[B] =
    apply(fa)(pure(f))
}
