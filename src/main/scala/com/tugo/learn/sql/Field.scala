package com.tugo.learn.sql

trait Field {
  def typeName() : String

  def nativeType[T]() : Class[T]
}
