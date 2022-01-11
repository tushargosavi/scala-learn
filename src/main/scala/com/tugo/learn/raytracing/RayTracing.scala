package com.tugo.learn.raytracing

import java.io.PrintWriter

sealed trait Shape
case class Sphere(x :Float, y:Float, z:Float, r:Float) extends Shape
case class Plane(x:Float, y:Float, z:Float, d: Float) extends Shape

// Vector 3D
case class V3d(x: Float, y: Float, z:Float)

// a point in 3d space
case class Point(x:Float, y: Float, z:Float)

// A RGB color template
case class RGB(r: Int, g: Int, b: Int)

case class Image(width: Int, height: Int, data: IndexedSeq[IndexedSeq[RGB]]) {
  def imgWrite(out: PrintWriter): Unit = {
    out.write(s"P3\n${width} ${height}\n255")
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        val p = data(i)(j)
        out.write(s"${p.r} ${p.g} ${p.b}\n")
      }
    }
  }

  def write(filename: String): Unit = {
    val out = new PrintWriter(filename)
    imgWrite(out)
    out.close()
  }
}

object RayTracing {
  def background(width: Int, height: Int): Image = {
    val b = (0.2 * 255).toInt
    val data = Range(0, height).map(i => {
      Range(0, width).map { j =>
        RGB(
          ((255.0 * i) / height).toInt,
          ((255.0 * j) / width).toInt,
          b)
      }
    })
    Image(width, height, data)
  }

  def main(args: Array[String]): Unit = {
    background(200, 300).write("/tmp/test.ppm")
  }
}
