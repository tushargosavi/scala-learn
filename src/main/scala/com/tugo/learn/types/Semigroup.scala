package com.tugo.learn.types

trait Semigroup[A] {
  def combine(a: A, b: A) : A
}
