package com.tugo.learn.types

trait Monoid[A] extends Semigroup[A] {
  def empty: A
  def combine(a: A, b: A) : A
}
